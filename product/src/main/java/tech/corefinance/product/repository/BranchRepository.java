package tech.corefinance.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.product.entity.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, String>,
        CommonResourceRepository<Branch, String> {
}
