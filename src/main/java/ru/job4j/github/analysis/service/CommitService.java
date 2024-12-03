package ru.job4j.github.analysis.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.dto.RepositoryCommits;
import ru.job4j.github.analysis.mapper.CommitMapper;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.repository.CommitStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class CommitService {
    private final CommitStorage commitStorage;
    private final CommitMapper commitMapper;

    @Async("findRepositoryCommitsPool")
    public List<RepositoryCommits> findRepositoryCommitsByName(String name) throws DataAccessException {
        return commitStorage.findAllByRepositoryNameOrderByDateDesc(name).stream()
                .map(commitMapper::getRepositoryCommitsFromCommit)
                .toList();
    }

    @Async("saveAllCommitsPool")
    @Transactional
    public List<Commit> saveAll(List<Commit> commits) throws DataAccessException {
        return commitStorage.saveAll(commits);
    }

    public List<Commit> findAllRecentCommits() throws DataAccessException {
        return commitStorage.findAllRecentCommits();
    }
}
