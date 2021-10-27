package org.jarvis.ws.medicine.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.ws.medicine.model.entity.enums.MediaTypes;
import org.jarvis.ws.medicine.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created: chheng
 * Date: 14-Jun-2020 Sun
 * Time: 09:29
 */
@RestController
@RequestMapping(value = "/media", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Accept-Language", value = "Accept-Language Description", paramType = "header", defaultValue = "kh")
    })
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JResponseEntity addMedia(@RequestPart("json") String json, @RequestPart(value = "file") MultipartFile file) throws IOException {
        return mediaService.addMedia(json, file);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Accept-Language", value = "Accept-Language Description", paramType = "header")
    })
    @GetMapping
    public JResponseEntity getById(@RequestParam(value = "id") int id,
                                   @RequestParam(value = "type", defaultValue = "NEWS") MediaTypes type) {
        return mediaService.getById(id, type);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Accept-Language", value = "Accept-Language Description", paramType = "header")
    })
    @GetMapping("/list")
    public JResponseEntity listMedia(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return mediaService.listMedia(page, limit);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Accept-Language", value = "Accept-Language Description", paramType = "header")
    })
    @DeleteMapping("/delete/{id}")
    public JResponseEntity deleteMedia(@PathVariable int id) {
        return mediaService.deleteMedia(id);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Accept-Language", value = "Accept-Language Description", paramType = "header")
    })
    @PostMapping("/save/{mediaId}")
    public JResponseEntity saveMedia(@PathVariable int mediaId, @RequestParam(required = false, value = "delete") boolean delete) {
        return mediaService.saveMedia(mediaId, delete);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Accept-Language", value = "Accept-Language Description", paramType = "header")
    })
    @GetMapping("/list-saved")
    public JResponseEntity listSavedMedia() {
        return mediaService.listSavedMedia();
    }

}
