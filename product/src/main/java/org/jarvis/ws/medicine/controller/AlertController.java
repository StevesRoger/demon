package org.jarvis.ws.medicine.controller;

import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.ws.medicine.model.request.Alert;
import org.jarvis.ws.medicine.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/alert")
public class AlertController {

    @Autowired
    private AlertService alertService;

    @PostMapping(value = "/send")
    public JResponseEntity send(@RequestBody Alert request) {
        return alertService.send(request);
    }

    @GetMapping
    public JResponseEntity list(@RequestParam(defaultValue = "notification") String type) {
        return alertService.list(type);
    }

    @GetMapping(value = "/badge")
    public JResponseEntity getBadge() {
        return alertService.getBadge();
    }

    @DeleteMapping(value = "/badge")
    public JResponseEntity clearBadge() {
        return alertService.clearBadge();
    }

}
