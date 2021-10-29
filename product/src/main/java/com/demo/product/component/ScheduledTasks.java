package com.demo.product.component;

import org.springframework.stereotype.Component;

@Component
//@EnableAsync
public class ScheduledTasks {

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
