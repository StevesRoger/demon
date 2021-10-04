package org.jarvis.ws.medicine.service;

import org.jarvis.core.I18N;
import org.jarvis.core.ICore;
import org.jarvis.core.exception.FatalException;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.core.util.LogSuffix;
import org.jarvis.ws.medicine.helper.SecurityHelper;
import org.jarvis.ws.medicine.helper.ValidationHelper;
import org.jarvis.ws.medicine.model.entity.ImageEntity;
import org.jarvis.ws.medicine.model.entity.MediaEntity;
import org.jarvis.ws.medicine.model.entity.enums.MediaTypes;
import org.jarvis.ws.medicine.repository.ImageRepository;
import org.jarvis.ws.medicine.repository.MediaRepository;
import org.jarvis.ws.medicine.repository.SystemRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created: chheng
 * Date: 14-Jun-2020 Sun
 * Time: 09:33
 */
@Service
public class MediaService {

    private static final LogSuffix LOG = LogSuffix.of(LoggerFactory.getLogger(MediaService.class));

    @Autowired
    private MediaRepository mediaRepo;

    @Autowired
    private ImageRepository imageRepo;

    @Autowired
    private SystemRepository sysRepo;

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public JResponseEntity addMedia(String json, MultipartFile file) throws IOException {
        LOG.info("add media");
        MediaEntity media = ICore.OBJECT_MAPPER.readValue(json, MediaEntity.class);
        if (media.getLink() != null)
            ValidationHelper.isValid(media.getLink());
        if (MediaTypes.NEWS.equals(media.getType())) {
            if (media.getWidth() == null || media.getWidth() < 0)
                media.setWidth(Long.parseLong(sysRepo.getConfig("media.news.width")));
            if (media.getHeight() == null || media.getHeight() < 0)
                media.setWidth(Long.parseLong(sysRepo.getConfig("media.news.height")));
        }
        ImageEntity image = new ImageEntity(file);
        imageRepo.addImage(image);
        media.setImage(image);
        mediaRepo.addMedia(media);
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("id", media.getId()).build();
    }

    public JResponseEntity listMedia(int page, int limit) {
        if (page <= 0 || limit <= 0)
            throw new FatalException(I18N.getMessage("fail"), "List media fail page number must greater then 0");
        int adsPos = Integer.parseInt(sysRepo.getConfig("media.ads.position"));
        int adsLimit = limit / adsPos;
        int adsOffSet = (page - 1) * adsLimit;
        //int newsLimit = adsLimit > 0 ? limit - adsLimit : limit;
        int newsOffSet = (page - 1) * limit;
        int total = mediaRepo.totalMedia();
        int totalPage = (int) Math.ceil((float) total / limit);
        Map<String, Object> param = new HashMap<>();
        param.put("limit", limit);
        param.put("offset", newsOffSet);
        param.put("type", MediaTypes.NEWS);
        List<MediaEntity> result = mediaRepo.listMedia(param);
        param.put("limit", adsLimit);
        param.put("offset", adsOffSet);
        param.put("type", MediaTypes.ADVERTISE);
        List<MediaEntity> ads = mediaRepo.listMedia(param);
        int step = 1;
        int adsIndex = 1;
        while (ads.size() != 0 && result.size() != 0) {
            if (step != adsPos) {
                step++;
                adsIndex++;
            } else {
                MediaEntity adsEntity = ads.remove(0);
                if (adsIndex > result.size())
                    result.add(adsEntity);
                else
                    result.add(adsIndex, adsEntity);
                step = 0;
                adsIndex++;
            }
        }
        for (MediaEntity entity : result) {
            MediaEntity.buildUrl(entity);
            int isSave = mediaRepo.getSavedMedia(SecurityHelper.getUserId(), entity.getId());
            entity.setSaved(isSave != 0);
        }
        return JResponseEntity.ok(I18N.getMessage("successful"))
                .jsonObject("medias", result)
                .put("current_page", page).put("limit", limit).put("has_next", page < totalPage)
                .put("total", total).put("total_page", totalPage).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public JResponseEntity deleteMedia(int id) {
        int result = mediaRepo.deleteMedia(id);
        if (result == 0)
            throw new FatalException(I18N.getMessage("media.was.delete"), "No media was deleted");
        return JResponseEntity.ok(I18N.getMessage("successful"));
    }

    public JResponseEntity getById(int id, MediaTypes type) {
        MediaEntity entity = mediaRepo.getById(id, type);
        if (entity == null)
            throw new FatalException(I18N.getMessage("fail"), "Get media by id " + id + " not found");
        return JResponseEntity.ok(I18N.getMessage("successful"), entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public JResponseEntity saveMedia(Integer mediaId, Boolean delete) {
        int result;
        if (delete) {
            LOG.info("delete saved save media:{},user id:{}", mediaId, SecurityHelper.getUserId());
            result = mediaRepo.deleteSavedMedia(SecurityHelper.getUserId(), mediaId);
        } else {
            LOG.info("user save media:{},user id:{}", mediaId, SecurityHelper.getUserId());
            result = mediaRepo.saveMedia(SecurityHelper.getUserId(), mediaId);
        }
        if (result == 0)
            return JResponseEntity.fail(I18N.getMessage("fail"));
        return JResponseEntity.ok(I18N.getMessage("successful"))
                .jsonObject("user_id", SecurityHelper.getUserId())
                .put("media_id", mediaId).build();
    }

    public JResponseEntity listSavedMedia() {
        LOG.info("list user saved media by user id:{}", SecurityHelper.getUserId());
        List<MediaEntity> list = mediaRepo.listSavedMedia(SecurityHelper.getUserId());
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("medias", list).build();
    }
}
