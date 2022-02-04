package com.demo.customer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created: KimChheng
 * Date: 07-Jan-2022 Fri
 * Time: 11:28 AM
 */
@RestController
@RequestMapping("/dummy")
public class DummyController {

    private static Logger logger = LoggerFactory.getLogger(DummyController.class);

    @GetMapping
    public ResponseEntity<?> test() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uuid", UUID.randomUUID().toString());
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            logger.info("Host-name:" + hostName);
            map.put("hostname", hostName);
            map.put("date_time", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(map);
    }
}
