package com.file.fileprocess.services;

import com.file.fileprocess.Exceptions.FileParsingException;
import com.file.fileprocess.dtos.BankMarketingDTO;
import com.file.fileprocess.dtos.FileProcessingResultDTO;
import com.file.fileprocess.dtos.ProcessingErrorDTO;
import com.file.fileprocess.model.FailedRecord;
import com.file.fileprocess.model.FileUploadLog;
import com.file.fileprocess.process.ExcelProcessor;
import com.file.fileprocess.process.FileProcessor;
import com.file.fileprocess.process.ProcessorFactory;
import com.file.fileprocess.repository.FailedRecordRepository;
import com.file.fileprocess.repository.FileUploadLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileProcessingService {

    private static final int BATCH_SIZE = 1000;

    private final ProcessorFactory processorFactory;
    private final BankMarketingService bankMarketingService;
    private final FailedRecordRepository failedRecordRepository;
    private final FileUploadLogRepository fileUploadLogRepository;

    @Async
    public void process(byte[] fileBytes, String fileName) {
        LocalDateTime startTime = LocalDateTime.now();
        FileUploadLog uploadLog = FileUploadLog.builder()
                .fileName(fileName)
                .status("IN_PROGRESS")
                .uploadTime(LocalDateTime.now())
                .build();

        uploadLog = fileUploadLogRepository.save(uploadLog);

        try {

            validateFile(fileBytes, fileName);

            String extension =
                    FilenameUtils.getExtension(fileName);

            FileProcessor<BankMarketingDTO> processor =
                    getProcessor(extension);

            List<BankMarketingDTO> records;

            try (java.io.InputStream inputStream =
                         new java.io.ByteArrayInputStream(fileBytes)) {

                records = processor.read(
                        inputStream,
                        BankMarketingDTO.class);
            }

            FileProcessingResultDTO result =
                    processRecords(records, fileName);

            uploadLog.setTotalRecords(result.getTotalRecords());
            uploadLog.setSuccessRecords(result.getSuccessRecords());
            uploadLog.setFailedRecords(result.getFailedRecords());
            uploadLog.setStatus("COMPLETED");

            fileUploadLogRepository.save(uploadLog);

            log.info(
                    "File {} processed successfully. Total={}, Success={}, Failed={}",
                    fileName,
                    result.getTotalRecords(),
                    result.getSuccessRecords(),
                    result.getFailedRecords());

            LocalDateTime endTime = LocalDateTime.now();
            long executionTimeInSeconds =
                    Duration.between(startTime, endTime).getSeconds();

            log.debug("Execution Time: " +executionTimeInSeconds + " seconds");
        } catch (Exception ex) {

            uploadLog.setStatus("FAILED");

            fileUploadLogRepository.save(uploadLog);

            log.error(
                    "File processing failed for {}",
                    fileName,
                    ex);

            throw new FileParsingException(
                    "Failed to process file: " + fileName,
                    ex);
        }
    }

    private FileProcessingResultDTO processRecords(
            List<BankMarketingDTO> records,
            String fileName) {

        long successCount = 0;
        long failedCount = 0;

        int rowNumber = 1;

        List<ProcessingErrorDTO> errors =
                new ArrayList<>();

        List<BankMarketingDTO> validRecords =
                new ArrayList<>(BATCH_SIZE);

        List<FailedRecord> failedRecords =
                new ArrayList<>();

        for (BankMarketingDTO dto : records) {

            try {

                validateRecord(dto);

                validRecords.add(dto);

                successCount++;

                if (validRecords.size() >= BATCH_SIZE) {

                    bankMarketingService.saveAll(validRecords);

                    log.info(
                            "Batch inserted successfully. Batch Size={}",
                            validRecords.size());

                    validRecords.clear();
                }

            } catch (Exception ex) {

                failedCount++;

                ProcessingErrorDTO error =
                        ProcessingErrorDTO.builder()
                                .rowNumber((long) rowNumber)
                                .recordData(dto.toString())
                                .errorMessage(ex.getMessage())
                                .timestamp(LocalDateTime.now())
                                .build();

                errors.add(error);

                failedRecords.add(
                        FailedRecord.builder()
                                .fileName(fileName)
                                .rowNumber((long) rowNumber)
                                .recordData(dto.toString())
                                .errorMessage(ex.getMessage())
                                .failedAt(LocalDateTime.now())
                                .build());

                log.error(
                        "Validation failed at row {} : {}",
                        rowNumber,
                        ex.getMessage());
            }

            rowNumber++;
        }

        // save remaining records
        if (!validRecords.isEmpty()) {

            bankMarketingService.saveAll(validRecords);

            log.info(
                    "Final batch inserted successfully. Batch Size={}",
                    validRecords.size());
        }

        // save failed records in batch
        if (!failedRecords.isEmpty()) {

            failedRecordRepository.saveAll(failedRecords);
        }

        return FileProcessingResultDTO.builder()
                .totalRecords((long) records.size())
                .successRecords(successCount)
                .failedRecords(failedCount)
                .errors(errors)
                .build();
    }

    private void validateRecord(
            BankMarketingDTO dto) {

        if (dto == null) {

            throw new IllegalArgumentException(
                    "Record is null");
        }

        if (dto.getAge() == null) {

            throw new IllegalArgumentException(
                    "Age is mandatory");
        }

        if (dto.getJob() == null ||
                dto.getJob().isBlank()) {

            throw new IllegalArgumentException(
                    "Job is mandatory");
        }

        if (dto.getMarital() == null ||
                dto.getMarital().isBlank()) {

            throw new IllegalArgumentException(
                    "Marital status is mandatory");
        }
    }

    private void validateFile(
            byte[] fileBytes,
            String fileName) {

        if (fileBytes == null) {

            throw new IllegalArgumentException(
                    "File is null");
        }

        if (fileBytes.length == 0) {

            throw new IllegalArgumentException(
                    "File is empty");
        }

        log.info("File Name: {}", fileName);
        log.info("File Size: {} bytes", fileBytes.length);

        String extension =
                FilenameUtils.getExtension(fileName);

        if (!"xlsx".equalsIgnoreCase(extension)
                && !"xls".equalsIgnoreCase(extension)
                && !"pdf".equalsIgnoreCase(extension)) {

            throw new IllegalArgumentException(
                    "Unsupported file type : " + extension);
        }
    }

    private FileProcessor<BankMarketingDTO> getProcessor(
            String extension) {

        if ("pdf".equalsIgnoreCase(extension)) {
            return processorFactory.getPdfProcessor();
        }

        return processorFactory.getExcelProcessor();
    }
}
