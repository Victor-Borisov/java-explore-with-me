package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

    String query = "SELECT DISTINCT ON(ip) * FROM endpoint_hit  WHERE timestamp BETWEEN ?1 AND ?2 ";

    List<EndpointHit> findAllByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = query, nativeQuery = true)
    List<EndpointHit> findAllByUniqueIp(LocalDateTime start, LocalDateTime end);

    @Query(value = query + "AND uri IN(?3)", nativeQuery = true)
    List<EndpointHit> findAllByUrisAndUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    List<EndpointHit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);

    @Query(value = "SELECT COUNT(ip) FROM endpoint_hit WHERE uri LIKE ?1", nativeQuery = true)
    int getHits(String uri);

}
