package org.jarvis.ws.medicine.service;

import org.jarvis.core.I18N;
import org.jarvis.core.model.http.response.JResponseEntity;
import org.jarvis.core.service.ScheduleTaskService;
import org.jarvis.core.service.ScheduleTaskService.TaskParam;
import org.jarvis.core.util.LogSuffix;
import org.jarvis.ws.medicine.model.entity.MedicineAlertEntity;
import org.jarvis.ws.medicine.model.entity.enums.AlertType;
import org.jarvis.ws.medicine.model.request.Alert;
import org.jarvis.ws.medicine.repository.MedicineRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

/**
 * Created: KimChheng
 * Date: 25-Oct-2020 Sun
 * Time: 1:46 PM
 */
@Service
public class ScheduledService {

    private static LogSuffix log = LogSuffix.of(LoggerFactory.getLogger(ScheduledService.class));

    @Autowired
    private MedicineRepository medicineRepo;

    @Autowired
    private AlertService alertService;

    @Autowired
    private ScheduleTaskService taskService;

    @Autowired
    private ScheduledAnnotationBeanPostProcessor postProcessor;

    private final Map<String, TaskParam> taskParamMap = new HashMap<>();

    @PostConstruct
    public void init() {
        taskParamMap.put("pushNotification6AM", TaskParam.cron(this::taskStockAlert, AlertType.NOTIFICATION, "0 0 6 ? * *"));
        taskParamMap.put("stockAlert11AM", TaskParam.cron(this::taskStockAlert, AlertType.ALERT, "0 */1 * * * *"));
        //taskParamMap.put("stockAlert11AM", TaskParam.cron(this::taskStockAlert, AlertType.ALERT, "0 0 11 ? * *"));
        //taskParamMap.put("stockAlert4PM", TaskParam.cron(this::taskStockAlert, AlertType.ALERT, "0 0 4 ? * *"));
        //taskParamMap.put("stockAlert4PM", TaskParam.cron(this::taskStockAlert, AlertType.ALERT, "0 15 22 ? * *"));
        taskService.start("stockAlert11AM", taskParamMap.get("stockAlert11AM"));
        //taskService.start("stockAlert4PM", taskParamMap.get("stockAlert4PM"));
        taskService.start("pushNotification6AM", taskParamMap.get("pushNotification6AM"));
    }

    public JResponseEntity start(List<String> ids) {
        List<String> result = new ArrayList<>();
        for (String id : ids) {
            try {
                log.info("start task schedule:{}", ids);
                ScheduledFuture<?> scheduled = taskService.start(id, taskParamMap.get(id));
                if (scheduled == null)
                    log.info("task schedule:{} not found", ids);
                String message = id + ":" + (I18N.getMessage(scheduled == null ? "not.found" : "started"));
                result.add(message);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.add(id + I18N.getMessage("failed"));
            }
        }
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("result", result).build();
    }

    public JResponseEntity stop(List<String> ids) {
        List<String> result = new ArrayList<>();
        for (String id : ids) {
            try {
                log.info("stop task schedule:{}", id);
                ScheduledFuture<?> scheduled = taskService.stop(id);
                if (scheduled == null)
                    log.info("task schedule:{} not found", id);
                String message = id + ":" + (I18N.getMessage(scheduled == null ? "not.found" : "stopped"));
                result.add(message);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                result.add(id + I18N.getMessage("failed"));
            }
        }
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("result", result).build();
    }

    public JResponseEntity listSchedule() {
        List<String> scheduled = taskService.listScheduled().stream().map((name) -> name + ":running").collect(Collectors.toList());
        return JResponseEntity.ok(I18N.getMessage("successful")).jsonObject("schedules", scheduled).build();
    }

    private void taskStockAlert(AlertType type) {
        log.info("Stock Alert");
        log.info(Thread.currentThread().getName());
        List<Alert> list = listStockRemainingAlert(type);
        for (Alert alert : list) {
            try {
                alertService.send(alert);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    private List<Alert> listStockRemainingAlert(AlertType type) {
        Set<MedicineAlertEntity> entitySet = medicineRepo.listMedicineAlert(0);
        Map<Integer, Alert> alertMap = new HashMap<>();
        for (MedicineAlertEntity entity : entitySet) {
            alertMap.computeIfPresent(entity.getUserId(), (key, alert) -> {
                StringBuilder builder = new StringBuilder(alert.getMessage());
                builder.append(",").append(entity.getName() + " remaining " + entity.getPostQuantity());
                String message = builder.toString();
                if (message.endsWith(","))
                    message = message.substring(0, message.length() - 1);
                alert.setMessage(message);
                return alert;
            });
            alertMap.computeIfAbsent(entity.getUserId(), (key) -> {
                Alert alert = new Alert();
                alert.setUserId(entity.getUserId());
                alert.setTitle("Stock Alert");
                alert.setMessage(entity.getName() + " remaining " + entity.getPostQuantity());
                alert.setCreatedBy("system");
                alert.setType(type);
                return alert;
            });
        }
        return new ArrayList<>(alertMap.values());
    }
}
