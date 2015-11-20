package minecraftgui.models.components;

import minecraftgui.models.fonts.Font;

/**
 * Created by Samuel on 2015-11-15.
 */
public interface ComponentText {

    Font getFont();
    double getStringWidth(String str);
    double getStringHeight();

}
