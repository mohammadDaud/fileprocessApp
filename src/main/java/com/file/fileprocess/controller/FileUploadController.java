package com.file.fileprocess.controller;

import com.file.fileprocess.dtos.ApiResponseDTO;
import com.file.fileprocess.services.FileProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileProcessingService service;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponseDTO>
    upload(
            @RequestParam MultipartFile file)
            throws Exception {

        service.process(
                file.getBytes(),
                file.getOriginalFilename());

        return ResponseEntity.ok(
                ApiResponseDTO.builder()
                        .success(true)
                        .message(
                                "File uploaded")
                        .build());
    }
}