package com.file.fileprocess.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_upload_log")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private Long totalRecords;

    private Long successRecords;

    private Long failedRecords;

    private String status;

    private LocalDateTime uploadTime;
}