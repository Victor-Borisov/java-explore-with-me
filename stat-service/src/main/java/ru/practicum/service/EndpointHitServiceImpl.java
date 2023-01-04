package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.storage.EndpointHitRepository;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.EndpointHitMapper;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.StatViewForSpecific;
import ru.practicum.utils.DateFormatterCustom;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {

    private final EndpointHitMapper endpointHitMapper;
    private final EndpointHitRepository endpointHitRepository;
    private final DateFormatterCustom formatter;

    @Override
    public void addEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = endpointHitRepository.save(endpointHitMapper.fromDto(endpointHitDto));
        log.info("Endpoint hit {} added", endpointHit);
    }

    @Override
    public List<ViewStatsDto> getStats(StatViewForSpecific criteria) {
        criteria.setStart(dateDecoder(criteria.getStart()));
        criteria.setEnd(dateDecoder(criteria.getEnd()));
        List<EndpointHit> endpointHits;
        if (criteria.getUris() != null && !criteria.isUnique()) {
            endpointHits = endpointHitRepository
                    .findAllByTimestampBetweenAndUriIn(formatter.stringToDate(criteria.getStart()),
                    formatter.stringToDate(criteria.getEnd()), List.of(criteria.getUris()));
        } else if (criteria.getUris() == null && criteria.isUnique()) {
            endpointHits = endpointHitRepository.findAllByUniqueIp(formatter.stringToDate(criteria.getStart()),
                    formatter.stringToDate(criteria.getEnd()));
        } else if (criteria.getUris() != null && criteria.isUnique()) {
            endpointHits = endpointHitRepository.findAllByUrisAndUniqueIp(formatter.stringToDate(criteria.getStart()),
                    formatter.stringToDate(criteria.getEnd()), List.of(criteria.getUris()));
        } else {
            endpointHits = endpointHitRepository.findAllByTimestampBetween(formatter.stringToDate(criteria.getStart()),
                    formatter.stringToDate(criteria.getEnd()));
        }
        List<String> endpointUris = endpointHits.stream().map(endpointHitMapper::toUri).collect(Collectors.toList());
        Map<String, Integer> hitCounts = getHitByUriList(endpointUris);
        List<ViewStatsDto> viewStats = endpointHits
                .stream()
                .map((EndpointHit endpointHit) -> endpointHitMapper
                        .toDto(endpointHit, hitCounts.get(endpointHit.getUri())))
                .collect(Collectors.toList());
        log.info("List of statistics retrieved {}", viewStats);

        return viewStats;
    }

    @Override
    public Map<String, Integer> getHitByUriList(List<String> uris) {
        Map<String, Integer> results = new HashMap<>();
        List<Object[]> countList = endpointHitRepository.getCountHitByUriList(uris);
        for (Object[] count: countList) {
            results.put(((String) count[0]), ((BigInteger) count[1]).intValue());
        }
        return results;
    }


    private String dateDecoder(String date) {
        return URLDecoder.decode(date, StandardCharsets.UTF_8);
    }
}
