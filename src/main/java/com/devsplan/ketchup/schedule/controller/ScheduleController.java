package com.devsplan.ketchup.schedule.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/content")
public class ScheduleController {

    @GetMapping("/schedules")
    public String allSchedule() {
        return "content/schedule";
    }

}