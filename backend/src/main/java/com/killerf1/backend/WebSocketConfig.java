package com.killerf1.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * This class provides configurations for mapping of the WebSocketHandler to specific URL
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * Registers a WebSocketHandler to a specified URL path
     * 
     * @param registry Provides methods for configuring WebSocketHandler request mappings
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(moveHandler(), "/move").setAllowedOrigins("*");
        registry.addHandler(findGameHandler(), "/findGame").setAllowedOrigins("*");
    }

    /**
     * Creates a bean for MoveHandler
     * 
     * @return An instance of MoveHandler
     */
    @Bean
    public WebSocketHandler moveHandler() {
        return new MoveHandler();
    }

    @Bean
    public WebSocketHandler findGameHandler() {
        return new FindGameHandler();
    }
}
