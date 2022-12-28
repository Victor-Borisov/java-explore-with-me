package ru.practicum.compilation.dto;

import lombok.*;
import ru.practicum.event.dto.ShortEventDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private List<ShortEventDto> events;

    @NotNull
    private Long id;

    @NotNull
    private boolean pinned;

    @NotBlank
    private String title;
}
