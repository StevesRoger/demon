package org.jarvis.ws.medicine.service;

import org.jarvis.core.util.HttpUtil;
import org.jarvis.core.util.LogSuffix;
import org.jarvis.ws.medicine.model.request.fcm.Notification;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created: KimChheng
 * Date: 20-Apr-2021 Tue
 * Time: 9:36 PM
 */
@Service
public class FCMService {

    @Value("${fcm.server.key}")
    private String fcmKey;
    @Value("${fcm.url}")
    private String fcmUrl;

    private static LogSuffix log = LogSuffix.of(LoggerFactory.getLogger(FCMService.class));

    private HttpHeaders fcmHeaders;

    @PostConstruct
    public void init() {
        fcmHeaders = new HttpHeaders();
        fcmHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        fcmHeaders.add(HttpHeaders.AUTHORIZATION, "key=" + fcmKey);
    }

    @SuppressWarnings("unchecked")
    public JSONObject pushNotification(Notification request) {
        log.info("send message to fire base {}", request.toJsonString());
        HttpEntity<String> entity = new HttpEntity<>(request.toJsonString(), fcmHeaders);
        ResponseEntity<JSONObject> response = HttpUtil.exchange(fcmUrl, HttpMethod.POST, entity, JSONObject.class, 0);
        return response.getBody();
    }
}
