package ru.practicum.event.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Event findByInitiatorIdAndId(Long initiatorId, Long eventId);

    Event findByIdAndStateLike(Long eventId, State state);

    @Query("SELECT e FROM Event e " +
            " WHERE e.initiator.id IN :users " +
            " AND e.state IN :states " +
            " AND e.category.id IN :categories " +
            " AND e.eventDate BETWEEN :startDate AND :endDate "
    )
    List<Event> findAllByUsersAndStatesAndCategories(Long[] users, List<State> states, Long[] categories,
        LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("SELECT e FROM Event e " +
            " WHERE e.state = ru.practicum.event.model.State.PUBLISHED " +
            " AND (e.annotation LIKE CONCAT('%',:text,'%') OR e.description LIKE CONCAT('%',:text,'%')) " +
            " AND e.category.id IN :categories " +
            " AND e.paid = :paid " +
            " AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd) " +
            " AND (" +
            " (:onlyAvailable = true AND e.participantLimit = 0) " +
            "OR (:onlyAvailable = true AND e.participantLimit > " +
            "(SELECT COUNT(r.id) FROM Request r WHERE r.status = 'CONFIRMED' AND r.event = e)) " +
            "OR (:onlyAvailable = false)" +
            ") "
    )
    List<Event> findAllByParam(String text,
                               Long[] categories,
                               Boolean paid,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               Boolean onlyAvailable,
                               Pageable pageable);
    @Query("SELECT e FROM Event e WHERE e.id IN :events")
    Set<Event> findAllByEvents(Set<Long> events);
}
