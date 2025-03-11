package com.popov.app;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class ScheduledTasks {
    @Scheduled(initialDelay = 600, fixedRate = 2800)
    public void logEventInfo() {
        System.out.println("Scheduled Task Executed at: " + LocalDateTime.now());
        System.out.println("API for the event management system");
        
    }
}
