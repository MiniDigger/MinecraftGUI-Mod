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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by Samuel on 2015-11-12.
 */
public class MinecraftGUI extends GuiScreen implements IGuiHandler {

    private final static int GUI_ID = -1000;

    private final Object mod;

    public MinecraftGUI(Object mod) {
        super();
        this.mod = mod;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed(){
        super.onGuiClosed();
        ((ModInterface) mod).minecraftGUIClosed();
    }

    public GuiScreen getGui() {
        return this;
    }

    public void openGui() {
        Minecraft.getMinecraft().thePlayer.openGui(mod, GUI_ID, Minecraft.getMinecraft().thePlayer.worldObj, Minecraft.getMinecraft().thePlayer.serverPosX, Minecraft.getMinecraft().thePlayer.serverPosY, Minecraft.getMinecraft().thePlayer.serverPosZ);
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == GUI_ID)
            return this;

        return null;
    }
}
