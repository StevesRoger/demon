package org.jarvis.ws.medicine.component;

import org.jarvis.core.util.LogSuffix;
import org.jarvis.ws.medicine.service.AlertService;
import org.jarvis.ws.medicine.service.ScheduledService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created: chheng
 * Date: 27-Jun-2020 Sat
 * Time: 14:11
 */
@Component
//@EnableAsync
public class ScheduledTasks {

    private static LogSuffix log = LogSuffix.of(LoggerFactory.getLogger(ScheduledTasks.class));

    @Autowired
    private ScheduledService service;

    @Autowired
    private AlertService alertService;

    //@Async(value = "taskExecutor")
    //@Scheduled(fixedDelay = 1000)
    //@Scheduled(cron = "0 0 * ? * *", zone = ICore.ASIA_PHONE_PENH)//cron run every one hours any day,month,year
    //@Scheduled(cron = "0 0 6 ? * *", zone = ICore.ASIA_PHONE_PENH)//cron run at 6 am every day
    //@Scheduled(cron = "0 */1 * * * *", zone = ICore.ASIA_PHONE_PENH)//cron run after 1 minute
    public void pushNotification6AM() {
    }

    //@Scheduled(cron = "0 0 11 ? * *", zone = ICore.ASIA_PHONE_PENH)//cron run at 11 AM

    //@Scheduled(cron = "0 0 4 ? * *", zone = ICore.ASIA_PHONE_PENH)//cron run at 4 PM

}
