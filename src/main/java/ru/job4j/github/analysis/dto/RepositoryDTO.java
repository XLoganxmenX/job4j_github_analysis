package ru.job4j.github.analysis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepositoryDTO {
    @NotBlank(message = "Поле author не должно быть пустым")
    private String author;

    @NotBlank(message = "Поле name не должно быть пустым")
    private String name;

    @NotBlank(message = "Поле url не должно быть пустым")
    private String url;
}
