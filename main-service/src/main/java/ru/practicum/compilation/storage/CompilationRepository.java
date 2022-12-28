package ru.practicum.compilation.storage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    List<Compilation> findAllByPinnedIs(boolean pinned, Pageable pageable);

    @Modifying
    @Query(value = "DELETE FROM event_compilation WHERE compilation_id = ?1 AND event_id = ?2", nativeQuery = true)
    void deleteEvent(Long compilationId, Long eventId);

    @Modifying
    @Query(value = "INSERT INTO event_compilation (compilation_id, event_id) VALUES (?1, ?2)", nativeQuery = true)
    void addEvent(Long compilationId, Long eventId);

    @Modifying
    @Query(value = "UPDATE compilations SET pinned = ?1 WHERE compilation_id = ?2", nativeQuery = true)
    void pinningCompilation(boolean pin, Long compilationId);

}
