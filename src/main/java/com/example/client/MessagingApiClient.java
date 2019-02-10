package com.example.client;

import java.io.IOException;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.config.LineBotProperties;

import lombok.RequiredArgsConstructor;

@Component
public class MessagingApiClient {
    private static final String API_URL = "https://api.line.me";
    private final RestTemplate restTemplate;

    public MessagingApiClient(RestTemplateBuilder builder, LineBotProperties properties) {
        restTemplate = builder.interceptors(new ClientHttpRequestInterceptorImpl(properties))
                              .build();
    }

    public DeliveryResponse getReplyDelivery(String date) {
        String uri = UriComponentsBuilder.fromHttpUrl(API_URL)
                                         .path("/v2/bot/message/delivery/reply")
                                         .queryParam("date", date)
                                         .toUriString();
        return restTemplate.getForObject(uri, DeliveryResponse.class);
    }

    public DeliveryResponse getPushDelivery(String date) {
        String uri = UriComponentsBuilder.fromHttpUrl(API_URL)
                                         .path("/v2/bot/message/delivery/push")
                                         .queryParam("date", date)
                                         .toUriString();
        return restTemplate.getForObject(uri, DeliveryResponse.class, date);
    }

    public DeliveryResponse getMulticastDelivery(String date) {
        String uri = UriComponentsBuilder.fromHttpUrl(API_URL)
                                         .path("/v2/bot/message/delivery/multicast")
                                         .queryParam("date", date)
                                         .toUriString();
        return restTemplate.getForObject(uri, DeliveryResponse.class);
    }

    @RequiredArgsConstructor
    private static class ClientHttpRequestInterceptorImpl implements ClientHttpRequestInterceptor {
        private final LineBotProperties properties;

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body,
                                            ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            request.getHeaders().set(HttpHeaders.AUTHORIZATION, "Bearer " + properties.getChannelToken());
            return execution.execute(request, body);
        }
    }
}
