package ru.practicum.comments.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comments.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByUserId(Long userId, Pageable pageable);

    @Query("SELECT e FROM Comment e " +
            " WHERE e.user.id IN :users " +
            " AND e.event.id IN :events " +
            " AND e.createdOn BETWEEN :startDate AND :endDate "
    )
    List<Comment> findAllByUsersAndEvents(Long[] users, Long[] events,
                                                     LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query
    List<Comment> getAllByUserId(Long userId);

    @Query
    List<Comment> getAllByEventId(Long eventId);

    Optional<Comment> findByIdAndUserId(long commentId, long userId);
}