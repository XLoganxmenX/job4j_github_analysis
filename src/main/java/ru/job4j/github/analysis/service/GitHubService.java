package ru.job4j.github.analysis.service;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.job4j.github.analysis.dto.response.GitHubCommitResponseDTO;
import ru.job4j.github.analysis.mapper.CommitMapper;
import ru.job4j.github.analysis.model.Commit;
import ru.job4j.github.analysis.model.Repository;

import java.util.List;

@Service
@AllArgsConstructor
public class GitHubService {
    private final RestTemplate restTemplate;
    private final CommitMapper commitMapper;

    public List<Repository> fetchRepositories(String username) {
        String url = "https://api.github.com/users/" + username + "/repos";
        ResponseEntity<List<Repository>> response = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Repository>>() { });
        return response.getBody();
    }

    public List<Commit> fetchCommits(String author, String repoName) {
        String url = String.format("https://api.github.com/repos/%s/%s/commits", author, repoName);
        ResponseEntity<List<GitHubCommitResponseDTO>> response = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<GitHubCommitResponseDTO>>() { });
        List<GitHubCommitResponseDTO> gitHubCommitDtos = response.getBody();
        if (gitHubCommitDtos != null) {
            return gitHubCommitDtos.stream()
                    .map(commitMapper::getCommitFromGitHubCommitResponseDTO)
                    .toList();
        }

        return List.of();
    }

    public List<Commit> fetchCommitsStartWith(Commit commit) {
        String url = String.format("https://api.github.com/repos/%s/%s/commits?sha=%s",
                commit.getAuthor(), commit.getRepository().getName(), commit.getSha());
        ResponseEntity<List<Commit>> response = restTemplate.exchange(
                url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Commit>>() { });
        return response.getBody();
    }
}