package org.eulerframework.cloud.file.dto;

import java.io.File;

public class ArchivedFileDTO {
    private String originalFileName;
    private File file;

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
