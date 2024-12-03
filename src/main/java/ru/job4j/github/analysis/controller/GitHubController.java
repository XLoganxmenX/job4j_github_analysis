package ru.job4j.github.analysis.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.job4j.github.analysis.dto.RepositoryDTO;
import ru.job4j.github.analysis.dto.RepositoryCommits;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.service.CommitService;
import ru.job4j.github.analysis.service.RepositoryService;

import java.util.List;

@RestController
@RequestMapping("/api")
@Validated
@AllArgsConstructor
public class GitHubController {

    private final RepositoryService repositoryService;
    private final CommitService commitService;

    @GetMapping("/repositories")
    @ResponseStatus(HttpStatus.OK)
    public List<Repository> getAllRepositories() {
        return repositoryService.findAll();
    }

    @GetMapping("/commits/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<RepositoryCommits> getCommits(@PathVariable(value = "name")
                                              @NotBlank
                                              String name) {
        return commitService.findRepositoryCommitsByName(name);
    }

    @PostMapping("/repository")
    public ResponseEntity<Void> create(@Valid @RequestBody RepositoryDTO repositoryDto) {
        repositoryService.create(repositoryDto);
        return ResponseEntity.noContent().build();
    }
}
