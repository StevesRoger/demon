package org.jarvis.ws.medicine.controller;

import org.jarvis.core.collection.LazyMap;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.core.util.LogSuffix;
import org.jarvis.ws.medicine.model.entity.ImageEntity;
import org.jarvis.ws.medicine.repository.ImageRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: KimChheng
 * Date: 29-Nov-2020 Sun
 * Time: 3:39 PM
 */
@RestController
@RequestMapping(value = "/image")
public class ImageController {

    private final LogSuffix log = LogSuffix.of(LoggerFactory.getLogger(ImageController.class));

    @Autowired
    private ImageRepository imageRepo;

    @GetMapping(value = "/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public HttpEntity<byte[]> viewImage(@PathVariable int id) throws IOException {
        log.info("view medicine image");
        ImageEntity image = imageRepo.getById(id);
        if (image == null || image.getBytes() == null || image.getBytes().length <= 0)
            image = ImageEntity.imageFromClassPath("image/no_image.png");
        byte[] bytes = image.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Disposition", "inline; filename=\"" + image.getName() + "\"");
        headers.set("Content-Type", image.getMineType());
        headers.setContentLength(bytes.length);
        return new HttpEntity<>(bytes, headers);
    }

    /*@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JResponseEntity upload(@RequestPart(value = "files") MultipartFile[] files) {
        log.info("upload {} images", files.length);
        try {
            List<LazyMap> list = new ArrayList<>();
            for (int i = 0; i < files.length; i++) {
                LazyMap map = new LazyMap(new ImageEntity(files[i]).toMap()).append("bytes", files[i].getBytes());
                list.add(map);
            }
            for (LazyMap map : list) {
                try {
                    imageRepo.addImage(map);
                } catch (Exception ex) {
                    log.warn(ex.getMessage(), ex);
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);

        }
        return JResponseEntity.ok("success");
    }*/
}
