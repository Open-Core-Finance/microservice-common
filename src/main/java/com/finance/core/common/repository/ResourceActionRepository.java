package com.finance.core.common.repository;

import com.finance.core.common.model.ResourceAction;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ResourceActionRepository<T extends ResourceAction> extends CommonResourceRepository<T, String>,
        ListPagingAndSortingRepository<T, String> {

}
