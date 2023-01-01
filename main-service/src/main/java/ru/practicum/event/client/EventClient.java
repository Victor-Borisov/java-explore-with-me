package ru.practicum.event.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.event.dto.EndpointHitDto;
import ru.practicum.event.dto.ViewStatsDto;
import ru.practicum.utils.DateFormatterCustom;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventClient {

    private final RestTemplate template;
    private final DateFormatterCustom formatter;

    private final String appName;

    public EventClient(@Value("${ewm-stat.url}") String url,
                       @Value("${application.name}") String appName,
                       RestTemplateBuilder template,
                       DateFormatterCustom formatter) {
        this.appName = appName;
        this.formatter = formatter;
        this.template = template
                .uriTemplateHandler(new DefaultUriBuilderFactory(url))
                .build();
    }

    public void addHit(HttpServletRequest request) {
        template.postForEntity("/hit",
                getHttpEntity(makeEndpointHit(request)),
                EndpointHitDto.class);
    }

    public ResponseEntity<List<ViewStatsDto>> getHits(LocalDateTime start,
                                                      LocalDateTime end,
                                                      String[] uris,
                                                      boolean unique) {
        return template.exchange("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                HttpMethod.GET,
                getHttpEntity(null),
                new ParameterizedTypeReference<>() {
                },
                encodeDate(start), encodeDate(end), uris, unique);
    }

    private EndpointHitDto makeEndpointHit(HttpServletRequest request) {
        return new EndpointHitDto(appName,
                request.getRequestURI(),
                request.getRemoteAddr(),
                formatter.dateToString(LocalDateTime.now()));
    }

    private <T> HttpEntity<T> getHttpEntity(T dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        return dto == null ? new HttpEntity<>(headers) : new HttpEntity<>(dto, headers);
    }

    private String encodeDate(LocalDateTime date) {
        return URLEncoder.encode(formatter.dateToString(date), StandardCharsets.UTF_8);
    }
}
