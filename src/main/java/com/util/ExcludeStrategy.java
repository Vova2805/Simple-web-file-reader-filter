package com.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class ExcludeStrategy implements ExclusionStrategy {

    private boolean includeMetaData;

    public ExcludeStrategy(boolean includeMetaData) {
        this.includeMetaData = includeMetaData;
    }

    /**
     * Exclude some fields
     * @param fieldAttributes
     * @return If true - field will be excluded
     */
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {

        boolean result = false;

        if (!includeMetaData) {
            if (fieldAttributes.getName().equals("metaData") ||
                    fieldAttributes.getName().equals("fileName") ||
                    fieldAttributes.getName().equals("fileSize") ||
                    fieldAttributes.getName().equals("fileCreationDate")) {
                result = true;
            }
        }

        if (fieldAttributes.getName().equals("BUFFER_SIZE") ||
                fieldAttributes.getName().equals("file") ||
                fieldAttributes.getName().equals("DEFUALT_LIMIT")) {
            result = true;
        }

        return result;
    }

    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}