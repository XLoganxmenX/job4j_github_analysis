package ru.job4j.github.analysis.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import ru.job4j.github.analysis.dto.response.CommitAuthorDTO;
import ru.job4j.github.analysis.dto.response.CommitDetailsDTO;
import ru.job4j.github.analysis.dto.response.GitHubCommitResponseDTO;
import ru.job4j.github.analysis.mapper.CommitMapper;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableAsync
class GitHubServiceTest {
    @InjectMocks
    private GitHubService gitHubService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private CommitMapper commitMapper;

    @Test
    public void whenFetchCommitsThenGetCommitList() {
        List<GitHubCommitResponseDTO> dtos = List.of(
                new GitHubCommitResponseDTO(
                        "jasdug111",
                        new CommitDetailsDTO(
                                new CommitAuthorDTO("author", now()), "message")
                )
        );
        Commit commit = new Commit(1L, "message", "author", now(), new Repository(), "jasdug111");
        ResponseEntity<List<GitHubCommitResponseDTO>> responseDtos = ResponseEntity.ok(dtos);
        when(restTemplate.exchange(
                eq("https://api.github.com/repos/author/repository/commits"),
                eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(responseDtos);
        when(commitMapper.getCommitFromGitHubCommitResponseDTO(any())).thenReturn(commit);
        List<Commit> actualCommits = gitHubService.fetchCommits("author", "repository");
        assertThat(actualCommits).isEqualTo(List.of(commit));
    }

    @Test
    public void whenOnFetchCommitsGitHubReturnNullThenGetEmptyList() {
        ResponseEntity<List<GitHubCommitResponseDTO>> responseDtos = ResponseEntity.ok(null);
        when(restTemplate.exchange(
                eq("https://api.github.com/repos/author/repository/commits"),
                eq(HttpMethod.GET), isNull(),
                any(ParameterizedTypeReference.class))
        ).thenReturn(responseDtos);
        List<Commit> actualCommits = gitHubService.fetchCommits("author", "repository");
        assertThat(actualCommits).isEmpty();
    }
}