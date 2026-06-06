package com.file.fileprocess.process;

import com.file.fileprocess.util.ReflectionMapperUtil;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PdfProcessor<T>
        extends AbstractFileProcessor<T> {

    @Override
    public List<T> read(
            InputStream inputStream,
            Class<T> clazz)
            throws Exception {

        List<T> records =
                new ArrayList<>();

        try (PDDocument document =
                     Loader.loadPDF(
                             inputStream.readAllBytes())) {

            PDFTextStripper stripper =
                    new PDFTextStripper();

            String text =
                    stripper.getText(document);

            String[] lines =
                    text.split("\\R");

            List<String> headers =
                    new ArrayList<>();

            String delimiterRegex =
                    "\\s+";

            for (String line : lines) {

                String trimmedLine =
                        line.trim();

                if (trimmedLine.isBlank()) {
                    continue;
                }

                if (isPageTitle(trimmedLine)) {
                    continue;
                }

                if (headers.isEmpty()) {

                    if (!looksLikeHeader(trimmedLine)) {
                        continue;
                    }

                    delimiterRegex =
                            resolveDelimiter(trimmedLine);

                    headers =
                            splitLine(
                                    trimmedLine,
                                    delimiterRegex);

                    continue;
                }

                if (looksLikeHeader(trimmedLine)) {
                    continue;
                }

                List<String> values =
                        splitLine(
                                trimmedLine,
                                delimiterRegex);

                if (values.size() < headers.size()
                        || !isRecord(values)) {
                    continue;
                }

                Map<String, String> rowData =
                        new HashMap<>();

                for (int i = 0;
                     i < headers.size() && i < values.size();
                     i++) {

                    rowData.put(
                            headers.get(i),
                            values.get(i));
                }

                records.add(
                        ReflectionMapperUtil
                                .mapRow(
                                        rowData,
                                        clazz));
            }
        }

        return records;
    }

    private boolean looksLikeHeader(
            String line) {

        String normalizedLine =
                line.toLowerCase();

        return normalizedLine.contains("age")
                && normalizedLine.contains("job")
                && normalizedLine.contains("marital");
    }

    private boolean isPageTitle(
            String line) {

        return line.toLowerCase()
                .matches(".*\\.xlsx\\s+-\\s+page\\s+\\d+.*");
    }

    private boolean isRecord(
            List<String> values) {

        if (values.isEmpty()) {
            return false;
        }

        return values.get(0)
                .matches("\\d+");
    }

    private String resolveDelimiter(
            String line) {

        if (line.contains("|")) {
            return "\\|";
        }

        if (line.contains(",")) {
            return ",";
        }

        if (line.contains(";")) {
            return ";";
        }

        if (line.contains("\t")) {
            return "\\t";
        }

        return "\\s+";
    }

    private List<String> splitLine(
            String line,
            String delimiterRegex) {

        String[] parts =
                line.trim()
                        .split(delimiterRegex);

        List<String> values =
                new ArrayList<>();

        for (String part : parts) {

            String value =
                    part.trim();

            if (!value.isEmpty()) {
                values.add(value);
            }
        }

        return values;
    }

    @Override
    public void validate(
            T record) {

    }

    @Override
    protected void process(
            T record) {

    }
}
