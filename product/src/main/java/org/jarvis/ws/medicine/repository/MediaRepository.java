package org.jarvis.ws.medicine.repository;

import org.apache.ibatis.annotations.Param;
import org.jarvis.orm.mybatis.annotation.IBatisRepository;
import org.jarvis.ws.medicine.model.entity.MediaEntity;
import org.jarvis.ws.medicine.model.entity.enums.MediaTypes;

import java.util.List;
import java.util.Map;

/**
 * Created: chheng
 * Date: 14-Jun-2020 Sun
 * Time: 09:40
 */
@IBatisRepository
public interface MediaRepository {

    int addMedia(MediaEntity entity);

    MediaEntity getById(@Param("id") int id, @Param("type") MediaTypes type);

    List<MediaEntity> listMedia(Map<String, Object> map);

    int deleteMedia(@Param("id") int id);

    int totalMedia();

    int saveMedia(@Param("userId") int userId, @Param("mediaId") int mediaId);

    List<MediaEntity> listSavedMedia(@Param("userId") int userId);

    int deleteSavedMedia(@Param("userId") int userId, @Param("mediaId") int mediaId);

    int getSavedMedia(@Param("userId") int userId, @Param("mediaId") int mediaId);
}

