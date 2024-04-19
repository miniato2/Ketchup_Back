package com.devsplan.ketchup.schedule;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/content")
public class SkdController {

    @GetMapping("/schedule")
    public String findSkd() {
        return "content/schedule";
    }

    @GetMapping("/fullCalendar")
    public String findFullCalendar() {
        return "content/fullCalendar";
    }

    @GetMapping("/schedule2")
    public String findFullCalendar2() {
        return "content/schedule2";
    }


}
