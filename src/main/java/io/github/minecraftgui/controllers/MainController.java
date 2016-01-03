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

import io.github.minecraftgui.models.UserInterface;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.network.NetworkInterface;
import io.github.minecraftgui.models.network.packets.PacketClientInitiated;
import io.github.minecraftgui.models.network.packets.PacketGuiEvent;
import io.github.minecraftgui.models.network.packets.PacketInterfaceInitiated;
import io.github.minecraftgui.views.ModInterface;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainController {

    private final Screen screen;
    private final Render render;
    private final Mouse mouse;
    private final KeyBoard keyBoard;
    private final ImageRepository imageRepository;
    private final FontRepository fontRepository;
    private int imagesDownload = 0;
    private int fontsDownload = 0;
    private boolean clientInitiated = false;
    private boolean interfaceInitiated = false;
    private UserInterface userInterface;
    private NetworkInterface networkInterface;

    public MainController(ModInterface modInterface) {
        this.imageRepository = new ImageRepository(this);
        this.fontRepository = new FontRepository(this);
        this.screen = new Screen();
        this.render = new Render(modInterface);
        this.mouse = new Mouse(screen);
        this.keyBoard = new KeyBoard();
    }

    public FontRepository getFontRepository() {
        return fontRepository;
    }

    public ImageRepository getImageRepository() {
        return imageRepository;
    }

    public void initClient(ArrayList<String> fontsToDownload, HashMap<String, String> imagesToDownload, HashMap<String, HashMap<Color, ArrayList<Integer>>> fontsToGenerate){
        clientInitiated = true;

        for(Map.Entry pairs : fontsToGenerate.entrySet()){
            String name = (String) pairs.getKey();
            for(Map.Entry fontSpecs : ((HashMap<Color, ArrayList<Integer>>)pairs.getValue()).entrySet()) {
                Color color = (Color) fontSpecs.getKey();
                ArrayList<Integer> sizes = (ArrayList<Integer>) fontSpecs.getValue();

                for(int size : sizes)
                    fontRepository.addFontToGenerate(name, color, size);
            }
        }

        imagesDownload = imagesToDownload.size();
        fontsDownload = fontsToDownload.size();

        for(String url : fontsToDownload)
            fontRepository.downloadFont(url);

        for(Map.Entry pairs : imagesToDownload.entrySet())
            imageRepository.downloadImage((String) pairs.getKey(), (String) pairs.getValue());
    }

    public void initInterface(){
        this.userInterface = new UserInterface(imageRepository, fontRepository, screen, render, mouse, keyBoard);
        interfaceInitiated = true;
        networkInterface.sendPacket(new PacketInterfaceInitiated());
    }

    public void stopInterface(){
        interfaceInitiated = false;
        this.userInterface = null;
    }

    public void setNetworkInterface(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }

    public Component getComponent(String id){
        if(interfaceInitiated)
            return userInterface.getComponent(id);
        else
            return null;
    }

    public void addComponent(String parentId, Component component){
        if(interfaceInitiated) {
            userInterface.addComponent(parentId, component);

            for(Component child : component.getChildren())
                addComponent(component.getId(), child);
        }
    }

    public void removeComponent(String componentId){
        if(interfaceInitiated)
            userInterface.removeComponent(componentId);
    }

    protected void fontDownloadFinished(){
        fontsDownload--;
    }

    protected void imageDownloadFinished(){
        imagesDownload--;
    }

    public void setCanPlayerInteractWithGUI(boolean canPlayerInteractWithGUI) {
        if(userInterface != null) {
            userInterface.setCanPlayerInteractWithGUI(canPlayerInteractWithGUI);

            if(canPlayerInteractWithGUI)
                networkInterface.sendPacket(new PacketGuiEvent.OnOpen());
            else
                networkInterface.sendPacket(new PacketGuiEvent.OnClose());
        }
    }

    public void setScreen(int width, int height, int scaleFactor){
        screen.setHeight(height);
        screen.setWidth(width);
        screen.setScaleFactor(scaleFactor);
    }

    public void update(){
        if(clientInitiated == true && imagesDownload == 0 && fontsDownload == 0) {
            imageRepository.generateImages();
            fontRepository.generateFonts(screen);
            clientInitiated = false;
            networkInterface.sendPacket(new PacketClientInitiated());
        }

        if(interfaceInitiated)
            userInterface.update();
    }

    public void draw(){
        if(interfaceInitiated)
            userInterface.draw();
    }

}
