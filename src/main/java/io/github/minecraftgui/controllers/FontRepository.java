/*
 *     Minecraft GUI mod
 *     Copyright (C) 2015  Samuel Marchildon-Lavoie
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.minecraftgui.controllers;

import io.github.minecraftgui.models.fonts.Font;
import io.github.minecraftgui.models.fonts.MinecraftNormalFont;
import io.github.minecraftgui.models.fonts.MinecraftShadowFont;
import io.github.minecraftgui.models.fonts.UnicodeFont;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Samuel on 2015-11-04.
 */
public class FontRepository {

    private final HashMap<String, Font> fonts = new HashMap<String, Font>();
    private final ArrayList<FontData> fontDatas;
    private final MainController controller;

    public FontRepository(MainController controller) {
        this.controller = controller;
        this.fontDatas = new ArrayList<>();
        addFont("minecraft normal", new MinecraftNormalFont());
        addFont("minecraft shadow", new MinecraftShadowFont());
    }

    public void generateFonts(Screen screen){
        for(FontData fontData : fontDatas){
            Font font = getFont(fontData.name);

            if(font != null && font instanceof UnicodeFont)
                ((UnicodeFont) font).generate(fontData.size, fontData.color);
            else{
                UnicodeFont unicodeFont = new UnicodeFont(screen, java.awt.Font.decode(fontData.name));
                unicodeFont.generate(fontData.size, fontData.color);
                addFont(fontData.name, unicodeFont);
            }
        }

        fontDatas.clear();
    }

    public void addFontToGenerate(String name, Color color, int size){
        fontDatas.add(new FontData(name, color, size));
    }

    public void addFont(String name, Font font){
        fonts.put(name.toLowerCase(), font);
    }

    public Font getFont(String name){
        return fonts.get(name.toLowerCase());
    }

    public void downloadFont(final String fontUrl){
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    URL url = new URL(fontUrl);
                    InputStream is = url.openStream();

                    ZipInputStream zis = new ZipInputStream(is);

                    ZipEntry entry;

                    while((entry = zis.getNextEntry()) != null){
                        String fileName = entry.getName().toLowerCase();

                        if (fileName.endsWith(".ttf") || fileName.endsWith(".otf")) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            int i;

                            while((i = zis.read()) != -1)
                                baos.write(i);

                            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                            java.awt.Font font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new ByteArrayInputStream(baos.toByteArray()));
                            ge.registerFont(font);
                        }
                    }
                    is.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                controller.fontDownloadFinished();
            }
        }).start();
    }

    private class FontData{

        private String name;
        private Color color;
        private int size;

        public FontData(String name, Color color, int size) {
            this.name = name;
            this.color = color;
            this.size = size;
        }
    }
}
