package org.jarvis.ws.medicine.controller;

import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.ws.medicine.service.ScheduledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created: KimChheng
 * Date: 25-Oct-2020 Sun
 * Time: 10:56 AM
 */
@RestController
@RequestMapping("/schedule")
public class SchedulesController {

    @Autowired
    private ScheduledService service;

    @GetMapping(value = "/start")
    public JResponseEntity startSchedule(@RequestParam(name = "names") List<String> taskNames) {
        return service.start(taskNames);
    }

    @GetMapping(value = "/stop")
    public JResponseEntity stopSchedule(@RequestParam(name = "names") List<String> taskNames) {
        return service.stop(taskNames);
    }

    @GetMapping(value = "/list")
    public JResponseEntity listSchedules() {
        return service.listSchedule();
    }
}
