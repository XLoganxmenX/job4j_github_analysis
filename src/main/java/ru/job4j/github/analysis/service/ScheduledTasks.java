package ru.job4j.github.analysis.service;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.model.Commit;

import java.util.List;


@Service
@AllArgsConstructor
public class ScheduledTasks {
    private final CommitService commitService;
    private final GitHubService gitHubService;

    @Scheduled(fixedRateString = "${scheduler.fixedRate}")
    public void fetchCommits() throws DataAccessException {
        List<Commit> recentCommits = commitService.findAllRecentCommits();
        for (Commit recentCommit : recentCommits) {
            List<Commit> commitsFromGit = gitHubService
                    .fetchCommits(recentCommit.getAuthor(), recentCommit.getRepository().getName());
            List<Commit> newCommits = findNewCommits(commitsFromGit, recentCommit.getSha());
            newCommits.forEach(commit -> commit.setRepository(recentCommit.getRepository()));
            commitService.saveAll(newCommits);
        }
    }

    private List<Commit> findNewCommits(List<Commit> commitsFromGit, String currentSha) {
        return commitsFromGit.stream()
                .takeWhile(commitFromGit -> !commitFromGit.getSha().equals(currentSha))
                .toList();
    }
}
