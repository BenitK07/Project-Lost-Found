package com.campus.lostfound.security;

import com.campus.lostfound.entity.User;
import com.campus.lostfound.repository.UserRepository;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final UserRepository userRepository;

    public WebSocketAuthInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authHeader =
                    accessor.getFirstNativeHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new IllegalArgumentException("Missing JWT token");
            }

            String email =
                    JwtUtil.validateAndGetEmail(authHeader.substring(7));

            User user = userRepository.findByEmail(email)
                    .orElseThrow();

            accessor.getSessionAttributes()
                    .put("userEmail", user.getEmail());

            accessor.getSessionAttributes()
                    .put("userRole", user.getRole());
        }

        return message;
    }
}
