package ru.job4j.github.analysis.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.job4j.github.analysis.dto.RepositoryDTO;
import ru.job4j.github.analysis.mapper.RepositoryMapper;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;
import ru.job4j.github.analysis.repository.RepositoryStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class RepositoryService {

    private final RepositoryStorage repositoryStorage;
    private final CommitService commitService;
    private final GitHubService gitHubService;
    private final RepositoryMapper repositoryMapper;

    @Async("createRepositoryPool")
    @Transactional
    public Repository create(RepositoryDTO repositoryDTO) throws DataAccessException {
        Repository repository = repositoryMapper.getRepositoryFromRepositoryDTO(repositoryDTO);
        repositoryStorage.save(repository);
        List<Commit> commits = gitHubService.fetchCommits(repository.getAuthor(), repository.getName());
        commits.forEach(commit -> commit.setRepository(repository));
        commitService.saveAll(commits);
        return repository;
    }

    @Async("findAllRepositoriesPool")
    public List<Repository> findAll() throws DataAccessException {
        return repositoryStorage.findAll();
    }
}
