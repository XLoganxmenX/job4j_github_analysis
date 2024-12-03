package ru.job4j.github.analysis.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.job4j.github.analysis.dto.RepositoryDTO;
import ru.job4j.github.analysis.mapper.RepositoryMapper;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.RepositoryStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableAsync
class RepositoryServiceTest {
    @InjectMocks
    private RepositoryService repositoryService;

    @Mock
    private RepositoryStorage repositoryStorage;

    @Mock
    private CommitService commitService;

    @Mock
    private GitHubService gitHubService;

    @Mock
    private RepositoryMapper repositoryMapper;

    @Test
    public void whenCreateThenReturnRepository() {
        RepositoryDTO dto = new RepositoryDTO("author", "test repo", "http://test");
        Repository repository = new Repository(1L, dto.getAuthor(), dto.getName(), dto.getUrl());
        List<Commit> commits = List.of(new Commit());
        when(repositoryMapper.getRepositoryFromRepositoryDTO(dto)).thenReturn(repository);
        when(repositoryStorage.save(repository)).thenReturn(repository);
        when(gitHubService.fetchCommits(any(), any())).thenReturn(commits);
        when(commitService.saveAll(commits)).thenReturn(commits);
        assertThat(repositoryService.create(dto)).isEqualTo(repository);
    }

    @Test
    public void whenFindAllThenReturnRepositoryList() {
        List<Repository> repositories = List.of(
                new Repository(1L, "test", "repositoryName", "http://test"),
                new Repository(2L, "test2", "repositoryName2", "http://test2")
        );
        when(repositoryStorage.findAll()).thenReturn(repositories);
        assertThat(repositoryService.findAll()).isEqualTo(repositories);
    }
}