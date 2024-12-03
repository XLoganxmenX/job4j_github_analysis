package ru.job4j.github.analysis.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.github.analysis.dto.RepositoryDTO;
import ru.job4j.github.analysis.model.Repository;

@Mapper(componentModel = "spring")
public interface RepositoryMapper {
    @Mapping(source = "author", target = "author")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "url", target = "url")
    Repository getRepositoryFromRepositoryDTO(RepositoryDTO repositoryDto);
}
