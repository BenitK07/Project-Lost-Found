package com.campus.lostfound.security;

import com.campus.lostfound.entity.User;
import com.campus.lostfound.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    public JwtInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        if (
                request.getRequestURI().startsWith("/api/auth") ||
                        request.getRequestURI().startsWith("/ws")
        ) {
            return true;
        }


        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(401);
            response.getWriter().write("Unauthorized");
            return false;
        }

        try {
            String email = JwtUtil.validateAndGetEmail(header.substring(7));
            User user = userRepository.findByEmail(email).orElseThrow();

            request.setAttribute("userEmail", user.getEmail());
            request.setAttribute("userRole", user.getRole());
            return true;

        } catch (Exception e) {
            response.setStatus(401);
            response.getWriter().write("Invalid token");
            return false;
        }
    }
}
