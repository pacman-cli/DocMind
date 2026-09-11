package com.dockmind.infrastructure.persistence;

import com.dockmind.domain.model.RepositorySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RepositorySourceJpaRepository extends JpaRepository<RepositorySource, UUID> {
}
