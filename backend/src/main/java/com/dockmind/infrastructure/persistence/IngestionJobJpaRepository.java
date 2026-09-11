package com.dockmind.infrastructure.persistence;

import com.dockmind.domain.model.IngestionJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface IngestionJobJpaRepository extends JpaRepository<IngestionJob, UUID> {
    Optional<IngestionJob> findByRepositoryIdOrderByStartedAtDesc(UUID repositoryId);
}
