package ru.practicum.event.model;

import java.util.Optional;

public enum State {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static Optional<State> from(String stringState) {
        for (State state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
