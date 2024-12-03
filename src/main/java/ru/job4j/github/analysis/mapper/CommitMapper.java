package ru.job4j.github.analysis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.github.analysis.dto.RepositoryDTO;
import ru.job4j.github.analysis.dto.response.GitHubCommitResponseDTO;
import ru.job4j.github.analysis.dto.RepositoryCommits;
import ru.job4j.github.analysis.model.Commit;

@Mapper(componentModel = "spring")
public interface CommitMapper {
    @Mapping(source = "commit.message", target = "message")
    @Mapping(source = "commit.author.name", target = "author")
    @Mapping(source = "commit.author.date", target = "date")
    @Mapping(source = "sha", target = "sha")
    Commit getCommitFromGitHubCommitResponseDTO(GitHubCommitResponseDTO commitDto);

    @Mapping(source = "repository.name", target = "repositoryName")
    @Mapping(source = "message", target = "message")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "date", target = "date")
    RepositoryCommits getRepositoryCommitsFromCommit(Commit commit);
}
