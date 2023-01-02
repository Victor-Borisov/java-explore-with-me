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

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
            endpointHits = endpointHitRepository.findAllByTimestampBetweenAndUriIn(formatter.stringToDate(criteria.getStart()),
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
        List<ViewStatsDto> viewStats = endpointHitMapper.toDto(endpointHits);
        for (ViewStatsDto viewStat : viewStats) {
            viewStat.setHits(endpointHitRepository.getHits(viewStat.getUri()));
        }
        log.info("List of statistics retrieved {}", viewStats);

        return viewStats;
    }

    private String dateDecoder(String date) {
        return URLDecoder.decode(date, StandardCharsets.UTF_8);
    }
}
