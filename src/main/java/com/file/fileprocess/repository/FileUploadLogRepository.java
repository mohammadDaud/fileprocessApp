package com.file.fileprocess.repository;

import com.file.fileprocess.model.FileUploadLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileUploadLogRepository extends JpaRepository<FileUploadLog, Long> {
}