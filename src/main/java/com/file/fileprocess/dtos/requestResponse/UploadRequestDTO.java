package com.file.fileprocess.dtos.requestResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadRequestDTO {

    private String fileName;

    private String fileType;
}
