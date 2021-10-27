package org.jarvis.ws.medicine.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.jarvis.core.I18N;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.core.util.LogSuffix;
import org.jarvis.core.util.StringUtil;
import org.jarvis.ws.medicine.helper.SecurityHelper;
import org.jarvis.ws.medicine.model.entity.AlertEntity;
import org.jarvis.ws.medicine.model.entity.InboxEntity;
import org.jarvis.ws.medicine.model.entity.UserEntity;
import org.jarvis.ws.medicine.model.entity.enums.Status;
import org.jarvis.ws.medicine.model.request.Alert;
import org.jarvis.ws.medicine.model.request.fcm.Notification;
import org.jarvis.ws.medicine.repository.AlertRepository;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AlertService {

    @Autowired
    private AlertRepository repository;

    @Autowired
    private FCMService fcmService;

    @Autowired
    private UserService userService;

    private static LogSuffix log = LogSuffix.of(LoggerFactory.getLogger(AlertService.class));

    @SuppressWarnings("unchecked")
    public JResponseEntity send(Alert request) {
        String to = request.getTopic();
        if (StringUtil.isEmpty(to)) {
            UserEntity user = userService.getUserById(request.getUserId());
            to = user.getDevice().getFcmToken();
        }
        Notification notification = new Notification(to, request);
        AlertEntity alertLog = new AlertEntity(request.getCreatedBy(), StringUtil.oneLine(notification.toJsonString()), request.getType());
        try {
            log.info("send alert to:{}", to);
            log.info("send by:{}", request.getCreatedBy());
            JSONObject response = fcmService.pushNotification(notification);
            alertLog.setStatus(response.optInt("failure") <= 0 ? Status.SUCCESSFUL : Status.FAILED);
            alertLog.setResponse(StringUtil.oneLine(response.toString()));
            log.info("response:{}", alertLog.getResponse());
            saveMessage(request, alertLog);
            return Status.SUCCESSFUL.equals(alertLog.getStatus()) ?
                    JResponseEntity.ok("successful").data(response.toMap()) :
                    JResponseEntity.fail("failed").data(response.toMap());
        } finally {
            repository.createAlertLog(alertLog);
        }
    }

    public JResponseEntity list(String type) {
        log.info("list notification user id:{}", SecurityHelper.getUserId());
        List<InboxEntity> list = repository.list(SecurityHelper.getUserId(), type);
        log.info("there are " + list.size() + " messages");
        return JResponseEntity.ok("").jsonObject("messages", list).build();
    }

    public JResponseEntity getBadge() {
        log.info("get inbox badge");
        int badge = repository.getBadge(SecurityHelper.getUserId());
        log.info("badge:{}", badge);
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("badge", badge).build();
    }

    public JResponseEntity clearBadge() {
        log.info("clear badge");
        int row = repository.clearBadge(SecurityHelper.getUserId());
        log.info(row + " badge clear");
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("badge", row).build();
    }

    private void saveMessage(Alert request, AlertEntity alertLog) {
        if (Status.SUCCESSFUL.equals(alertLog.getStatus())) {
            CompletableFuture.runAsync(() -> {
                List<InboxEntity> inboxes = new ArrayList<>();
                if (Objects.nonNull(request.getUserId()))
                    inboxes.add(creatInbox(request, request.getUserId()));
                else
                    inboxes.addAll(userService.listUser().stream().map(user -> creatInbox(request, user.getId())).collect(Collectors.toList()));
                for (InboxEntity inbox : inboxes) {
                    try {
                        repository.createInbox(inbox);
                    } catch (Exception ex) {
                        log.info(ex.getMessage());
                    }
                }
            }).exceptionally((ex) -> {
                log.error(ex.getMessage(), ex);
                return null;
            });
        }
    }

    private InboxEntity creatInbox(Alert request, Integer userId) {
        InboxEntity inboxEntity = new InboxEntity(request.getTitle(), request.getMessage());
        inboxEntity.setCreatedBy(request.getCreatedBy());
        inboxEntity.setType(request.getType());
        inboxEntity.setUserId(userId);
        return inboxEntity;
    }

    public static void main(String[] args) {
        String num = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
        String text = RandomStringUtils.randomAlphabetic(8).toUpperCase();
        System.out.println(num);
        System.out.println(text);
    }
}
