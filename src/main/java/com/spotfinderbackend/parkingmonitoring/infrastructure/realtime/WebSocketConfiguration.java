package com.spotfinderbackend.parkingmonitoring.infrastructure.realtime;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Native STOMP-over-WebSocket endpoint used by the Flutter mobile client
        // (stomp_dart_client connects directly to /ws/parking/websocket).
        registry.addEndpoint("/ws/parking").setAllowedOriginPatterns("*");
        // SockJS fallback for browsers / Angular dashboard that cannot use raw WebSocket.
        registry.addEndpoint("/ws/parking-sockjs").setAllowedOriginPatterns("*").withSockJS();
    }
}
