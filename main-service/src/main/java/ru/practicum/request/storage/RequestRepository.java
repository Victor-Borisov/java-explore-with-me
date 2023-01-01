package ru.practicum.request.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.request.model.Request;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long requesterId);

    List<Request> findAllByEventId(Long eventId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE requests SET status = 'REJECTED' WHERE event_id = ?1", nativeQuery = true)
    void rejectAll(Long eventId);

    @Query(value = "SELECT COUNT(*) FROM requests WHERE status = 'CONFIRMED' AND event_id = ?1", nativeQuery = true)
    Long getCountConfirmedByEventId(long eventId);

    @Query(value = "SELECT e.event_id, COALESCE(r.count_confirmed, 0) AS count_confirmed " +
            "FROM events e " +
            "LEFT JOIN (SELECT event_id, COUNT(*) AS count_confirmed " +
                "FROM requests r WHERE status = 'CONFIRMED' GROUP BY event_id) r ON r.event_id = e.event_id " +
            "WHERE e.event_id IN ?1",
            nativeQuery = true)
    List<Object[]> getCountConfirmedByEventIdList(List<Long> events);
}
