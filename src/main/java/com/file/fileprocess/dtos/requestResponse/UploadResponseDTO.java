package com.file.fileprocess.dtos.requestResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadResponseDTO {

    private String fileName;

    private String status;

    private Long totalRecords;

    private Long successRecords;

    private Long failedRecords;
}