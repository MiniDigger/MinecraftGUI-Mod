package io.github.minecraftgui.models.repositories;

import io.github.minecraftgui.controllers.MainController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2016-01-03.
 */
public class FileRepository {

    private final ArrayList<String> mimeTypesAllowed;
    protected final CopyOnWriteArrayList<String> urlLoaded;

    public FileRepository(ArrayList<String> mimeTypesAllowed) {
        this.mimeTypesAllowed = mimeTypesAllowed;
        this.urlLoaded = new CopyOnWriteArrayList<>();
    }

    public InputStream getFile(String url) throws Exception {
        String fileName = generateFileName(url);
        File file = new File(MainController.PATH+"resources/"+fileName);

        if(file.exists() && getFileLength(url) == file.length())
            return new FileInputStream(file);
        else{
            ByteArrayInputStream bais = downloadFileByUrl(url);
            FileOutputStream fos;

            file.createNewFile();

            fos = new FileOutputStream(file);
            int i;

            while ((i = bais.read()) != -1)
                fos.write(i);

            fos.close();

            bais.reset();
            return bais;
        }
    }

    private long getFileLength(String fileUrl) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        long length = con.getContentLengthLong();

        con.disconnect();

        return length;
    }

    private ByteArrayInputStream downloadFileByUrl(String fileUrl) throws Exception {
        URL url = new URL(fileUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        if(con.getContentLengthLong() <= 3145728 && mimeTypesAllowed.contains(con.getContentType())) {
            InputStream is = con.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream((int) con.getContentLengthLong());
            int i;

            while((i = is.read()) != -1)
                baos.write(i);

            return new ByteArrayInputStream(baos.toByteArray());
        }

        return null;
    }

    private String generateFileName(String url) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(url.getBytes());
        byte array[] = messageDigest.digest();
        String name = "";

        for(int i = 0; i < array.length; i++)
            name += array[i]+"";

        return name;
    }

}
