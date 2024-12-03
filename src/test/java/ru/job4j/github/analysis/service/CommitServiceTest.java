package ru.job4j.github.analysis.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.job4j.github.analysis.dto.RepositoryCommits;
import ru.job4j.github.analysis.mapper.CommitMapper;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.CommitStorage;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@EnableAsync
class CommitServiceTest {
    @InjectMocks
    private CommitService commitService;

    @Mock
    private CommitStorage commitStorage;

    @Mock
    private CommitMapper commitMapper;

    @Test
    public void whenFindRepositoryCommitsByNameThenGetRepCommitsList() {
        String repositoryName = "test repository";
        Repository repository = new Repository(1L, "test", repositoryName, "http://test");
        List<Commit> commits = List.of(
                new Commit(1L, "message", "test author", now(), repository, "a0801hdseg"),
                new Commit(2L, "message2", "test author2", now(), repository, "ssfdkj1213")
        );
        RepositoryCommits repositoryCommits =
                new RepositoryCommits(repositoryName, "message", "test author", now());
        when(commitStorage.findAllByRepositoryNameOrderByDateDesc(repositoryName)).thenReturn(commits);
        when(commitMapper.getRepositoryCommitsFromCommit(any(Commit.class))).thenReturn(repositoryCommits);

        List<RepositoryCommits> result = commitService.findRepositoryCommitsByName(repositoryName);
        assertThat(result).hasSize(2);
        assertThat(result).contains(repositoryCommits);
    }

    @Test
    public void whenSaveAllThenReturnCommitList() {
        Repository repository = new Repository(1L, "test", "test repository", "http://test");
        List<Commit> expectedCommits = List.of(
                new Commit(1L, "message", "test author", now(), repository, "a0801hdseg"),
                new Commit(2L, "message2", "test author2", now(), repository, "ssfdkj1213")
        );
        when(commitStorage.saveAll(expectedCommits)).thenReturn(expectedCommits);
        List<Commit> actualCommit = commitService.saveAll(expectedCommits);
        assertThat(actualCommit).isEqualTo(expectedCommits);
    }

    @Test
    public void whenFindAllRecentCommitsThenGetCommitList() {
        Repository repository = new Repository(1L, "test", "test repository", "http://test");
        List<Commit> expectedCommits = List.of(
                new Commit(1L, "message", "test author", now(), repository, "a0801hdseg"),
                new Commit(2L, "message2", "test author2", now(), repository, "ssfdkj1213")
        );
        when(commitStorage.findAllRecentCommits()).thenReturn(expectedCommits);
        List<Commit> actualCommit = commitService.findAllRecentCommits();
        assertThat(actualCommit).isEqualTo(expectedCommits);
    }
}