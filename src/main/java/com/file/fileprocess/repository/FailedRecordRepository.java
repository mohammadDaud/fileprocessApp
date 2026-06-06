package com.file.fileprocess.repository;

import com.file.fileprocess.model.FailedRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedRecordRepository extends JpaRepository<FailedRecord, Long> {
}