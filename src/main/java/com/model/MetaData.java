package com.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MetaData {

    private String fileName;
    private String fileSize;
    private String fileCreationDate;

    /**
     * Initializing fields by file attributes
     *
     * @param file
     */
    public MetaData(File file) {
        try {
            if (file.exists()) {
                double fileSize = file.length();

                Path path = Paths.get(file.getAbsolutePath());

                BasicFileAttributes attr;

                attr = Files.readAttributes(path, BasicFileAttributes.class);
                this.fileName = file.getName();

                this.fileSize = fileSize / 1000 + "KB";

                Date fileCreationDate = new Date(attr.creationTime().toMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy 'at' hh:mma");

                this.fileCreationDate = dateFormat.format(fileCreationDate);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileCreationDate() {
        return fileCreationDate;
    }

    public void setFileCreationDate(String fileCreationDate) {
        this.fileCreationDate = fileCreationDate;
    }

}