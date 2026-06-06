package com.file.fileprocess.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessingErrorDTO {

    private Long rowNumber;

    private String recordData;

    private String errorMessage;

    private LocalDateTime timestamp;
}