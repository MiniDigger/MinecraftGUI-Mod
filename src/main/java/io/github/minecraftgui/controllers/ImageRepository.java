package io.github.minecraftgui.controllers;

import io.github.minecraftgui.models.images.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Samuel on 2015-12-11.
 */
public class ImageRepository {

    private final HashMap<String, Image> images;
    private final ArrayList<ImageData> imagesData;
    private final MainController mainController;

    public ImageRepository(MainController mainController) {
        this.mainController = mainController;
        images = new HashMap<>();
        imagesData = new ArrayList<>();
    }

    public void addImage(String name, Image image){
        images.put(name.toLowerCase(), image);
    }

    public Image getImage(String name){
        return images.get(name.toLowerCase());
    }

    public void generateImages(){
        for(ImageData imageData : imagesData) {
            if (imageData.getExtension().endsWith("jpg")) {
                try {
                    Texture texture = TextureLoader.getTexture("JPG", new ByteArrayInputStream(imageData.frames.get(0).bytes));
                    addImage(imageData.getName(), new StaticImage(texture));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (imageData.getExtension().endsWith("png")) {
                try {
                    Texture texture = TextureLoader.getTexture("PNG", new ByteArrayInputStream(imageData.frames.get(0).bytes));
                    addImage(imageData.getName(), new StaticImage(texture));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (imageData.getExtension().endsWith("gif")) {
                try {
                    AnimatedImage gifImage = new AnimatedImage();

                    for (int i = 0; i < imageData.frames.size(); i++) {
                        ImageData.Frame frame = imageData.frames.get(i);

                        Texture texture = TextureLoader.getTexture("GIF", new ByteArrayInputStream(frame.bytes));
                        gifImage.addFrame(new Frame(frame.time, texture));
                    }

                    addImage(imageData.getName(), gifImage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        imagesData.clear();
    }

    public void downloadImage(final String imageUrl, final String name){
        new Thread(new Runnable() {

            @Override
            public void run() {
                DataInputStream dis = null;
                String extension = name.substring(name.lastIndexOf(".")+1);

                try {
                    URL url = new URL(imageUrl);
                    InputStream is;

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    is = con.getInputStream();

                    dis = new DataInputStream(is);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    int b;
                    int bytes = 0;

                    while ((b = dis.read()) != -1) {
                        bytes++;

                        if(bytes > 3145728)//3mb
                            throw new Exception();

                        baos.write(b);
                    }

                    dis.close();

                    if(extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png")){
                        ImageData imageData = new ImageData(extension, name);
                        imageData.frames.add(new ImageData.Frame(0, baos.toByteArray()));

                        imagesData.add(imageData);
                    }
                    else if(extension.equalsIgnoreCase("gif")) {
                        try {
                            ImageData imageData = new ImageData(extension, name);
                            GifDecoder.GifImage gifImage = GifDecoder.read(baos.toByteArray());

                            for(b = 0; b < gifImage.getFrameCount(); b++){
                                BufferedImage bufferedImage = gifImage.getFrame(b);

                                ByteArrayOutputStream baosImage = new ByteArrayOutputStream();
                                ImageIO.write(bufferedImage, "GIF", baosImage);

                                imageData.frames.add(new ImageData.Frame(gifImage.getDelay(b) * 10, baosImage.toByteArray()));

                                baosImage.close();
                            }

                            imagesData.add(imageData);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    baos.close();
                } catch (Exception ex) {
                    try {
                        dis.close();
                    } catch (IOException e) {}
                    ex.printStackTrace();
                }

                mainController.imageDownloadFinished();
            }
        }).start();
    }

    private static class ImageData{

        private final String extension;
        private final String name;
        private final ArrayList<Frame> frames;

        public ImageData(String extension, String name) {
            this.extension = extension;
            this.name = name;
            this.frames = new ArrayList<>();
        }

        public String getExtension() {
            return extension;
        }

        public String getName() {
            return name;
        }

        public ArrayList<Frame> getFrames() {
            return frames;
        }

        private static class Frame{

            private final long time;
            private final byte[] bytes;

            public Frame(long time, byte[] bytes) {
                this.time = time;
                this.bytes = bytes;
            }
        }

    }

}
