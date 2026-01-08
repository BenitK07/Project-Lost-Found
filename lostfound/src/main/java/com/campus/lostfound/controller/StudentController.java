package com.campus.lostfound.controller;

import com.campus.lostfound.security.RoleUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @GetMapping("/home")
    public String home(HttpServletRequest request) {
        if (!RoleUtil.isStudent(request)) throw new RuntimeException("Forbidden");
        return "STUDENT HOME";
    }
}
