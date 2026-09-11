package com.dockmind.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ingestion_jobs")
@NoArgsConstructor
@Getter
@Setter
public class IngestionJob {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID repositoryId;

    @Enumerated(EnumType.STRING)
    private IngestionStatus status;

    private Instant startedAt;

    private Instant finishedAt;

    private String errorMessage;

    private Integer chunksProcessed;

    private IngestionJob(UUID repositoryId) {
        this.repositoryId = repositoryId;
        this.status = IngestionStatus.QUEUED;
        this.startedAt = Instant.now();
        this.chunksProcessed = 0;
    }

    // Static Method
    public static IngestionJob start(UUID repositoryId) {
        return new IngestionJob(repositoryId);
    }

    // checks
    public void markRunning() {
        this.status = IngestionStatus.RUNNING;
    }

    public void markCompleted(int chunksProcessed) {
        this.status = IngestionStatus.COMPLETED;
        this.finishedAt = Instant.now();
        this.chunksProcessed = chunksProcessed;
    }

    public void markFailed(String message) {
        this.status = IngestionStatus.FAILED;
        this.finishedAt = Instant.now();
        this.errorMessage = message;
    }
}
