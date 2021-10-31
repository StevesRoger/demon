package com.demo.auth.controller;


import com.demo.auth.component.RsaBCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping
public class DummyController {

    @Autowired
    private RsaBCryptPasswordEncoder passwordEncoder;

    @GetMapping
    public ModelAndView frontend(){
        return new ModelAndView("index.html");
    }

    @PostMapping(value = "/encrypt")
    public String messages(@RequestParam String raw, Model model) throws GeneralSecurityException, UnsupportedEncodingException {
        model.addAttribute("result", passwordEncoder.encryptText(raw));
        return "index";
    }
}
