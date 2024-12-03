package ru.job4j.github.analysis.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import ru.job4j.github.analysis.model.Commit;

import java.util.List;

public interface CommitStorage extends ListCrudRepository<Commit, Long> {
    List<Commit> findAllByRepositoryNameOrderByDateDesc(String name);

    @Query(value = """
            SELECT c FROM Commit c
            JOIN FETCH c.repository r
            WHERE c.date = (SELECT MAX(c2.date) FROM Commit c2 WHERE c2.repository = c.repository)
            """)
    List<Commit> findAllRecentCommits();
}