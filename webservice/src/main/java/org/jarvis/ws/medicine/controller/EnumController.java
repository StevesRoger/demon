package org.jarvis.ws.medicine.controller;

import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.ws.medicine.service.EnumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created: KimChheng
 * Date: 30-Oct-2020 Fri
 * Time: 8:20 PM
 */
@RestController
@RequestMapping(value = "/enums", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class EnumController {

    @Autowired
    private EnumService enumService;

    @GetMapping("/{table}")
    public JResponseEntity list(@PathVariable("table") String table) {
        return enumService.list(table);
    }
}
