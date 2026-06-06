package com.file.fileprocess.process;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class AbstractFileProcessor<T>
        implements FileProcessor<T> {

    @Override
    public void save(List<T> records) {

        records.forEach(record -> {

            try {

                validate(record);

                process(record);

            } catch (Exception ex) {

                log.error(
                        "Processing failed : {}",
                        record,
                        ex);
            }
        });
    }

    protected abstract void process(
            T record);
}