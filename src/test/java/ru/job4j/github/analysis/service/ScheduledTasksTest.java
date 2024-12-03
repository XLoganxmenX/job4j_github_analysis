package ru.job4j.github.analysis.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@EnableAsync
class ScheduledTasksTest {
    @InjectMocks
    private ScheduledTasks scheduledTasks;

    @Mock
    private CommitService commitService;

    @Mock
    private GitHubService gitHubService;

    @Test
    public void whenFetchCommits() {
        Repository repository = new Repository(1L, "test", "repositoryName", "http://test");
        Commit recentCommit =
                new Commit(1L, "testMessage", "testAuthor", now(), repository, "testSha");
        List<Commit> recentCommits = List.of(recentCommit);
        Commit expectedCommit =
                new Commit(null, "testMessage1", "testAuthor1", now(), null, "1a");
        List<Commit> commitsFromGit = List.of(
                expectedCommit,
                new Commit(null, "testMessage", "testAuthor", now(), null, "testSha")
        );
        when(commitService.findAllRecentCommits()).thenReturn(recentCommits);
        when(gitHubService.fetchCommits("testAuthor", "repositoryName")).thenReturn(commitsFromGit);

        scheduledTasks.fetchCommits();

        verify(commitService, times(1)).findAllRecentCommits();
        verify(gitHubService, times(1)).fetchCommits("testAuthor", "repositoryName");
        verify(commitService, times(1)).saveAll(List.of(expectedCommit));
    }
}