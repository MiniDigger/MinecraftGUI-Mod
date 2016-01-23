package io.github.minecraftgui.models.network.packets;

import io.github.minecraftgui.controllers.MainController;
import io.github.minecraftgui.models.network.NetworkInterface;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Samuel on 2015-12-12.
 */
public class PacketInitClient extends PacketIn {

    public PacketInitClient(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
        super(jsonObject, mainController, networkInterface);
        ArrayList<String> fonts = new ArrayList<>();
        HashMap<String, String>images = new HashMap<>();
        HashMap<String, HashMap<Color, ArrayList<Integer>>> fontsGenerate = new HashMap<>();
        JSONArray fontsJSON = jsonObject.getJSONArray(NetworkInterface.FONTS);
        JSONArray imagesJSON = jsonObject.getJSONArray(NetworkInterface.IMAGES);
        JSONArray fontsGenerateJSON = jsonObject.getJSONArray(NetworkInterface.FONTS_TO_GENERATE);

        for(int i = 0; i < fontsJSON.length(); i++)
            fonts.add(fontsJSON.getString(i));

        for(int i = 0; i < imagesJSON.length(); i++) {
            JSONObject objImage = imagesJSON.getJSONObject(i);
            images.put(objImage.getString(NetworkInterface.URL), objImage.getString(NetworkInterface.NAME));
        }

        for(int i = 0; i < fontsGenerateJSON.length(); i++) {
            JSONObject font = fontsGenerateJSON.getJSONObject(i);
            JSONArray colorList = font.getJSONArray(NetworkInterface.LIST);
            HashMap<Color, ArrayList<Integer>> colors = new HashMap<>();

            for(int j = 0; j < colorList.length(); j++){
                JSONObject colorObj = colorList.getJSONObject(j);
                JSONArray sizeList = colorObj.getJSONArray(NetworkInterface.LIST);
                Color color = new Color(colorObj.getInt(NetworkInterface.R), colorObj.getInt(NetworkInterface.G), colorObj.getInt(NetworkInterface.B), colorObj.getInt(NetworkInterface.A));
                ArrayList<Integer> sizes = new ArrayList<>();

                for(int s = 0; s < sizeList.length(); s++)
                    sizes.add(sizeList.getInt(s));

                colors.put(color, sizes);
            }

            fontsGenerate.put(font.getString(NetworkInterface.NAME), colors);
        }

        mainController.initClient(fonts, images, fontsGenerate);
    }

}
