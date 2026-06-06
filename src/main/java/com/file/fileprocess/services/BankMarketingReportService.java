package com.file.fileprocess.services;

import com.file.fileprocess.model.BankMarketing;
import com.file.fileprocess.repository.BankMarketingRepository;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BankMarketingReportService {

    private static final int PAGE_SIZE = 1000;
    private static final float PDF_MARGIN = 24;
    private static final float PDF_FONT_SIZE = 5;
    private static final float PDF_LINE_HEIGHT = 7;

    private static final List<String> HEADERS = List.of(
            "age",
            "job",
            "marital",
            "education",
            "default",
            "housing",
            "loan",
            "contact",
            "month",
            "day_of_week",
            "duration",
            "campaign",
            "pdays",
            "previous",
            "poutcome",
            "emp.var.rate",
            "cons.price.idx",
            "cons.conf.idx",
            "euribor3m",
            "nr.employed",
            "y");

    private final BankMarketingRepository repository;

    public byte[] generateExcelReport()
            throws IOException {

        try (SXSSFWorkbook workbook =
                     new SXSSFWorkbook(100);
             ByteArrayOutputStream outputStream =
                     new ByteArrayOutputStream()) {

            workbook.setCompressTempFiles(true);

            Sheet sheet =
                    workbook.createSheet("Bank Marketing Report");

            CellStyle headerStyle =
                    createHeaderStyle(workbook);

            Row headerRow =
                    sheet.createRow(0);

            for (int i = 0; i < HEADERS.size(); i++) {
                Cell cell =
                        headerRow.createCell(i);
                cell.setCellValue(HEADERS.get(i));
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 18 * 256);
            }

            int rowIndex =
                    1;
            int pageNumber =
                    0;

            Page<BankMarketing> page;

            do {
                Pageable pageable =
                        PageRequest.of(
                                pageNumber,
                                PAGE_SIZE);

                page =
                        repository.findAll(pageable);

                for (BankMarketing record : page.getContent()) {
                    Row row =
                            sheet.createRow(rowIndex++);
                    writeExcelRow(row, record);
                }

                pageNumber++;
            } while (page.hasNext());

            workbook.write(outputStream);
            workbook.dispose();

            return outputStream.toByteArray();
        }
    }

    public byte[] generatePdfReport()
            throws IOException {

        try (PDDocument document =
                     new PDDocument();
             ByteArrayOutputStream outputStream =
                     new ByteArrayOutputStream()) {

            PDRectangle pageSize =
                    new PDRectangle(
                            PDRectangle.A2.getHeight(),
                            PDRectangle.A2.getWidth());

            PdfPageState state =
                    createPdfPage(
                            document,
                            pageSize,
                            1);

            int pageNumber =
                    0;

            Page<BankMarketing> page;

            do {
                Pageable pageable =
                        PageRequest.of(
                                pageNumber,
                                PAGE_SIZE);

                page =
                        repository.findAll(pageable);

                for (BankMarketing record : page.getContent()) {

                    if (state.y <= PDF_MARGIN) {
                        state.contentStream.close();
                        state =
                                createPdfPage(
                                        document,
                                        pageSize,
                                        state.pageNumber + 1);
                    }

                    state.contentStream.showText(
                            toPdfLine(record));
                    state.contentStream.newLineAtOffset(
                            0,
                            -PDF_LINE_HEIGHT);
                    state.y -= PDF_LINE_HEIGHT;
                }

                pageNumber++;
            } while (page.hasNext());

            state.contentStream.close();
            document.save(outputStream);

            return outputStream.toByteArray();
        }
    }

    private CellStyle createHeaderStyle(
            SXSSFWorkbook workbook) {

        CellStyle style =
                workbook.createCellStyle();

        Font font =
                workbook.createFont();

        font.setBold(true);
        style.setFont(font);

        return style;
    }

    private void writeExcelRow(
            Row row,
            BankMarketing record) {

        writeCell(row, 0, record.getAge());
        writeCell(row, 1, record.getJob());
        writeCell(row, 2, record.getMarital());
        writeCell(row, 3, record.getEducation());
        writeCell(row, 4, record.getDefaultStatus());
        writeCell(row, 5, record.getHousing());
        writeCell(row, 6, record.getLoan());
        writeCell(row, 7, record.getContact());
        writeCell(row, 8, record.getMonth());
        writeCell(row, 9, record.getDayOfWeek());
        writeCell(row, 10, record.getDuration());
        writeCell(row, 11, record.getCampaign());
        writeCell(row, 12, record.getPdays());
        writeCell(row, 13, record.getPrevious());
        writeCell(row, 14, record.getPoutcome());
        writeCell(row, 15, record.getEmpVarRate());
        writeCell(row, 16, record.getConsPriceIdx());
        writeCell(row, 17, record.getConsConfIdx());
        writeCell(row, 18, record.getEuribor3m());
        writeCell(row, 19, record.getNrEmployed());
        writeCell(row, 20, record.getYesNo());
    }

    private void writeCell(
            Row row,
            int columnIndex,
            Object value) {

        Cell cell =
                row.createCell(columnIndex);

        if (value == null) {
            cell.setBlank();
        } else if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else {
            cell.setCellValue(value.toString());
        }
    }

    private PdfPageState createPdfPage(
            PDDocument document,
            PDRectangle pageSize,
            int pageNumber)
            throws IOException {

        PDPage page =
                new PDPage(pageSize);

        document.addPage(page);

        PDPageContentStream contentStream =
                new PDPageContentStream(
                        document,
                        page);

        float startY =
                pageSize.getHeight() - PDF_MARGIN;

        contentStream.beginText();
        contentStream.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.COURIER),
                8);
        contentStream.newLineAtOffset(
                PDF_MARGIN,
                startY);
        contentStream.showText(
                "Bank Marketing Report - page " + pageNumber);
        contentStream.newLineAtOffset(
                0,
                -12);

        contentStream.setFont(
                new PDType1Font(
                        Standard14Fonts.FontName.COURIER),
                PDF_FONT_SIZE);
        contentStream.showText(
                String.join("|", HEADERS));
        contentStream.newLineAtOffset(
                0,
                -PDF_LINE_HEIGHT);

        return new PdfPageState(
                contentStream,
                pageNumber,
                startY - 12 - PDF_LINE_HEIGHT);
    }

    private String toPdfLine(
            BankMarketing record) {

        return String.join(
                "|",
                text(record.getAge()),
                text(record.getJob()),
                text(record.getMarital()),
                text(record.getEducation()),
                text(record.getDefaultStatus()),
                text(record.getHousing()),
                text(record.getLoan()),
                text(record.getContact()),
                text(record.getMonth()),
                text(record.getDayOfWeek()),
                text(record.getDuration()),
                text(record.getCampaign()),
                text(record.getPdays()),
                text(record.getPrevious()),
                text(record.getPoutcome()),
                text(record.getEmpVarRate()),
                text(record.getConsPriceIdx()),
                text(record.getConsConfIdx()),
                text(record.getEuribor3m()),
                text(record.getNrEmployed()),
                text(record.getYesNo()));
    }

    private String text(
            Object value) {

        if (value == null) {
            return "";
        }

        return value.toString()
                .replace("|", " ")
                .replace("\n", " ")
                .replace("\r", " ")
                .trim();
    }

    private static class PdfPageState {

        private final PDPageContentStream contentStream;
        private final int pageNumber;
        private float y;

        private PdfPageState(
                PDPageContentStream contentStream,
                int pageNumber,
                float y) {

            this.contentStream = contentStream;
            this.pageNumber = pageNumber;
            this.y = y;
        }
    }
}
