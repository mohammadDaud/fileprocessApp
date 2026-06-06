package com.file.fileprocess.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileProcessingResultDTO {

    private Long totalRecords;

    private Long successRecords;

    private Long failedRecords;

    private List<ProcessingErrorDTO> errors;
}
