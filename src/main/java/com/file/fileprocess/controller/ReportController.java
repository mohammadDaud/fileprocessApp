package com.file.fileprocess.controller;

import com.file.fileprocess.services.BankMarketingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/bank-marketing")
@RequiredArgsConstructor
public class ReportController {

    private final BankMarketingReportService reportService;

    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadExcelReport()
            throws Exception {

        byte[] report =
                reportService.generateExcelReport();

        return ResponseEntity.ok()
                .headers(downloadHeaders(
                        "bank-marketing-report.xlsx",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(report);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdfReport()
            throws Exception {

        byte[] report =
                reportService.generatePdfReport();

        return ResponseEntity.ok()
                .headers(downloadHeaders(
                        "bank-marketing-report.pdf",
                        MediaType.APPLICATION_PDF_VALUE))
                .body(report);
    }

    private HttpHeaders downloadHeaders(
            String fileName,
            String contentType) {

        HttpHeaders headers =
                new HttpHeaders();

        headers.setContentType(
                MediaType.parseMediaType(contentType));

        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename(fileName)
                        .build());

        return headers;
    }
}
