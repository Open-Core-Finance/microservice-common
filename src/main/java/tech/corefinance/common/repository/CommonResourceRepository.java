package tech.corefinance.common.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface CommonResourceRepository<T, ID extends Serializable>
        extends ListCrudRepository<T, ID>, ListPagingAndSortingRepository<T, ID> {
}
