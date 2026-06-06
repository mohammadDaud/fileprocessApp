package com.file.fileprocess.process;

import org.springframework.stereotype.Component;

@Component
public class ProcessorFactory {

    public <T> ExcelProcessor<T>
    getExcelProcessor() {

        return new ExcelProcessor<>();
    }

    public <T> PdfProcessor<T>
    getPdfProcessor() {

        return new PdfProcessor<>();
    }
}
