package com.file.fileprocess.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "failed_record")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailedRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long rowNumber;

    @Column(columnDefinition = "TEXT")
    private String recordData;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private LocalDateTime failedAt;

    private String fileName;
}