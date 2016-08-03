package com.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFile {

    private final int BUFFER_SIZE = 1024;
    private final int DEFUALT_LIMIT = 10000;
    private List<String> text = new ArrayList<String>();
    private MetaData metaData;
    private File file;


    /**
     * Initializing object by concrete file instance
     *
     * @param file
     */
    public TextFile(File file) {
        this.file = file;
        metaData = new MetaData(file);
    }

    /**
     * Initializing object by concrete file instance
     *
     * @param fileName Name of file which is placed inside resources/files folder
     */
    public TextFile(String directoryName, String fileName) {
        this.file = directoryName.equals("") ? getFile(fileName) : getFile(directoryName, fileName);
        metaData = new MetaData(file);
    }

    /**
     * Fill text attribute according to input parameters
     *
     * @param q
     * @param length
     * @param limit
     * @param useNIO
     * @throws Exception
     */
    public void fill(String q, int length, int limit, boolean useNIO) throws Exception {
        if (q.equals("")) {
            if (useNIO) {
                text.add(readFileNIO(limit > 0 ? limit : DEFUALT_LIMIT));
            } else {
                text.add(readFile(limit > 0 ? limit : DEFUALT_LIMIT));
            }

        } else {
            text.addAll(readFile(q, length, limit > 0 ? limit : DEFUALT_LIMIT));
        }

    }

    /**
     * Querying file content. Return list of all occurrences. *{word}*
     *
     * @param q      Query word
     * @param length Maximum word length
     * @param limit  Total limit of result length
     * @return List of occurrences (String)
     * @throws Exception
     */
    private List<String> readFile(String q, int length, int limit) throws Exception {
        List<String> resultList = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\S*(" + q + ")\\S*");

        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            int totalLength = 0;
            while ((line = bufferedReader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    int start = matcher.start(0);
                    int end = matcher.end(0);
                    int wordLength = end - start;

                    String world = line.substring(start, end);

                    if (end > start && (length < 0 || wordLength <= length))//length<0 if length parameter is skipped
                    {
                        totalLength += wordLength;
                        if (totalLength <= limit) {
                            resultList.add(line.substring(start, end));
                        }
                    }
                }
            }
        } finally {
            bufferedReader.close();
        }
        return resultList;
    }


    /**
     * Reading file up to limit exceeding (Using BufferedReader IO)
     *
     * @param limit Maximum result size
     * @return String result
     * @throws Exception
     */
    private String readFile(int limit) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            int totalLength = 0;
            while ((line = bufferedReader.readLine()) != null) {
                int lineLength = line.length();
                if (totalLength + lineLength <= limit) {
                    totalLength += lineLength;
                    stringBuilder.append(line);
                } else {
                    stringBuilder.append(line.substring(0, (limit - totalLength)));
                    break;
                }
            }
        } finally {
            bufferedReader.close();
        }
        return stringBuilder.toString();
    }

    /**
     * Reading file up to limit exceeding (Using ByteBuffer NIO). Faster than IO
     *
     * @param limit Maximum result size
     * @return String result
     * @throws Exception
     */
    private String readFileNIO(int limit) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();

        FileChannel fileChannel = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            fileChannel = fileInputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            byteBuffer.clear();

            int totalLength = 0;
            int length = 0;
            Charset charset = Charset.forName("UTF-8");

            while ((length = fileChannel.read(byteBuffer)) != -1 && totalLength < limit) {
                byteBuffer.flip();
                if ((totalLength + length) <= limit) {
                    totalLength += length;
                    stringBuilder.append(charset.decode(byteBuffer));
                    byteBuffer.clear();
                } else {
                    byte[] buffer = new byte[limit - totalLength];
                    byteBuffer.get(buffer, 0, limit - totalLength);
                    stringBuilder.append(new String(buffer, "UTF-8"));
                    byteBuffer.clear();
                    break;
                }
            }
        } finally {
            fileChannel.close();
            fileInputStream.close();
        }
        return stringBuilder.toString();
    }

    /**
     * Get file inside /resources/files folder
     *
     * @param fileName
     * @return FIle
     */
    public File getFile(String fileName) {
        return getFile("files", fileName);
    }

    /**
     * Use for receive file inside other directory
     *
     * @param directory Specify folder path which contains file
     * @param fileName  Name of file
     * @return File object
     */
    public File getFile(String directory, String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(directory + "/" + fileName).getFile());
        return file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFile(String fileName) {
        this.file = getFile(fileName);
    }

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }
}
