package com.campus.lostfound.security;

import com.campus.lostfound.enums.Role;
import jakarta.servlet.http.HttpServletRequest;

public class RoleUtil {

    public static boolean isAdmin(HttpServletRequest request) {
        return request.getAttribute("userRole") == Role.ADMIN;
    }

    public static boolean isStudent(HttpServletRequest request) {
        return request.getAttribute("userRole") == Role.STUDENT;
    }
}
