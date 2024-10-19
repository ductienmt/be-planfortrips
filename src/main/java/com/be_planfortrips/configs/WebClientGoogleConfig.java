package com.be_planfortrips.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientGoogleConfig {

    @Bean
    public WebClient webClient() {
        String introspect_uri = "https://www.googleapis.com";
        return WebClient.builder().baseUrl(introspect_uri).build();
    }

}
