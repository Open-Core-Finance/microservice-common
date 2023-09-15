package tech.corefinance.common.repository;

import tech.corefinance.common.model.AbstractResourceAction;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ResourceActionRepository<T extends AbstractResourceAction> extends CommonResourceRepository<T, String>,
        ListPagingAndSortingRepository<T, String> {

}
