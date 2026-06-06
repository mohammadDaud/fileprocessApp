package com.file.fileprocess.process;

import java.io.InputStream;
import java.util.List;

public interface FileProcessor<T> {

    List<T> read(
            InputStream inputStream,
            Class<T> clazz)
            throws Exception;

    void validate(T record);

    void save(List<T> records);
}
