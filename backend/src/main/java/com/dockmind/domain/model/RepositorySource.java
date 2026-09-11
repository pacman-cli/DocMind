package com.dockmind.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "repository_sources")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepositorySource {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String gitUrl;

    private String branch;

    @Enumerated(EnumType.STRING)
    private RepositoryStatus status = RepositoryStatus.NOT_INGESTED;

    @CreationTimestamp
    private Instant createdAt;

    public void markIngesting(){
        this.status=RepositoryStatus.INGESTING;
    }
    public void markFailed(){
        this.status=RepositoryStatus.FAILED;
    }
    public void markReady(){
        this.status=RepositoryStatus.READY;
    }

}
