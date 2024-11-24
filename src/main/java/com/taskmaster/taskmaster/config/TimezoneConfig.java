package com.taskmaster.taskmaster.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class TimezoneConfig {

    @PostConstruct
    public void TimezoneConfig() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Lisbon"));
    }
}
