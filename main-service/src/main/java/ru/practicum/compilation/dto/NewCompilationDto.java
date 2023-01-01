package ru.practicum.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    @NotNull
    private Set<Long> events;

    @NotNull
    private boolean pinned;

    @NotBlank
    @Size(max = 512)
    private String title;
}
