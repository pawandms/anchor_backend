package com.anchor.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class HomeController {

     private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
    public String index(Model model) 
    {
        logger.info("Root Controller Called");
        model.addAttribute("envName", "Anchor");
        model.addAttribute("appVersion", "1.0.0");

        return "index";
    }

     @GetMapping("/centrifugo-test")
    public String getCntrifugePage(Model model) 
    {
        logger.info("centrifugo-test Controller Called");
        model.addAttribute("envName", "Anchor");
        model.addAttribute("appVersion", "1.0.0");

        return "centrifugo-test";
    }
}
