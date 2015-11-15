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

package minecraftgui.views;

import minecraftgui.controllers.MainController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.InvocationTargetException;

@Mod(modid = "MinecraftGUI", version = "2.0")
public class ModInterface {

    private static final KeyBinding KEY_OPEN_GUI = new KeyBinding("Open GUI to interact with the components.", Keyboard.KEY_N, "Minecraft GUI");

    private MainController controller;
    private MinecraftGUI minecraftGUI;

    public ModInterface() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        controller = new MainController(this);
    }

    public void bindMinecraftTextures() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(Gui.icons);
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        minecraftGUI = new MinecraftGUI(this);
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        NetworkRegistry.INSTANCE.registerGuiHandler(this, minecraftGUI);
        ClientRegistry.registerKeyBinding(KEY_OPEN_GUI);
    }

    @SubscribeEvent
    public void update(RenderGameOverlayEvent.Post.Text event) {
        update();
        if(Minecraft.getMinecraft().currentScreen == null || Minecraft.getMinecraft().currentScreen instanceof GuiChat || Minecraft.getMinecraft().currentScreen == minecraftGUI.getGui()) {
            controller.setScreen(event.resolution.getScaledWidth(), event.resolution.getScaledHeight(), event.resolution.getScaleFactor());
            controller.update();
            controller.draw();
        }
    }

    public void update(){
        boolean pressed = KEY_OPEN_GUI.isPressed();

        if(pressed){
            minecraftGUI.openGui();
            controller.setCanPlayerInteractWithGUI(true);
        }
    }

    public void minecraftGUIClosed(){
        controller.setCanPlayerInteractWithGUI(false);
    }

}
