package com.file.fileprocess.process;

import com.file.fileprocess.util.ReflectionMapperUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.util.*;

public class ExcelProcessor<T>
        extends AbstractFileProcessor<T> {

    @Override
    public List<T> read(
            InputStream inputStream,
            Class<T> clazz)
            throws Exception {

        Workbook workbook =
                WorkbookFactory.create(
                        inputStream);

        Sheet sheet =
                workbook.getSheetAt(0);

        Iterator<Row> iterator =
                sheet.iterator();

        Row headerRow =
                iterator.next();

        List<String> headers =
                new ArrayList<>();

        headerRow.forEach(cell ->
                headers.add(
                        cell.getStringCellValue()));

        List<T> records =
                new ArrayList<>();

        while (iterator.hasNext()) {

            Row row =
                    iterator.next();

            Map<String, String> rowData =
                    new HashMap<>();

            for (int i = 0;
                 i < headers.size();
                 i++) {

                Cell cell =
                        row.getCell(i);

                rowData.put(
                        headers.get(i),
                        cell == null
                                ? ""
                                : cell.toString());
            }

            records.add(
                    ReflectionMapperUtil
                            .mapRow(
                                    rowData,
                                    clazz));
        }

        return records;
    }

    @Override
    public void validate(T record) {

    }

    @Override
    protected void process(T record) {

    }
}