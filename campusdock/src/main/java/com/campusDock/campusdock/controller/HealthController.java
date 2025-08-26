package com.campusDock.campusdock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HealthController
{
    @ResponseBody
    @GetMapping
    public String HealthCheck()
    {
//        return "Campus Dock first prototype " +
//                "\n Everything's working " +
//                "\n Go To v0/devs-->new app";
        return "Watch Tower on ec2";
    }

    @GetMapping("v0/devs")
    public String devs(){
        return "redirect:/index.html";
    }

}
