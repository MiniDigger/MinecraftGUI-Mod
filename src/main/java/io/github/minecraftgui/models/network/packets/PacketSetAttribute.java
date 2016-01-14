package io.github.minecraftgui.models.network.packets;

import io.github.minecraftgui.controllers.MainController;
import io.github.minecraftgui.controllers.Mouse;
import io.github.minecraftgui.models.attributes.*;
import io.github.minecraftgui.models.components.*;
import io.github.minecraftgui.models.components.Component;
import io.github.minecraftgui.models.components.List;
import io.github.minecraftgui.models.components.TextArea;
import io.github.minecraftgui.models.network.NetworkInterface;
import io.github.minecraftgui.models.shapes.PolygonColor;
import io.github.minecraftgui.models.shapes.RectangleImage;
import io.github.minecraftgui.models.shapes.Shape;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;

/**
 * Created by Samuel on 2015-12-06.
 */
public abstract class PacketSetAttribute extends PacketIn {

    protected final Component component;
    protected final Shape shape;
    protected final State state;
    protected final JSONObject attribute;

    public PacketSetAttribute(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
        super(jsonObject, mainController, networkInterface);
        component = mainController.getComponent(jsonObject.getString(NetworkInterface.COMPONENT_ID));
        state = State.valueOf(jsonObject.getString(NetworkInterface.STATE).toUpperCase());
        attribute = jsonObject.getJSONObject(NetworkInterface.ATTRIBUTE);
        shape = getShape(component, jsonObject.getString(NetworkInterface.SHAPE));
    }

    protected Shape getShape(Component component, String shape){
        switch (shape){
            case NetworkInterface.SHAPE_NORMAL: return component.getShape();
            case NetworkInterface.SHAPE_ON_VALUE_FALSE:
                if(component instanceof CheckBox) return ((CheckBox) component).getShapeOnValueFalse();
                break;
            case NetworkInterface.SHAPE_ON_VALUE_TRUE:
                if(component instanceof CheckBox) return ((CheckBox) component).getShapeOnValueTrue();
                break;
            case NetworkInterface.SHAPE_ON_PROGRESS:
                if(component instanceof ProgressBar) return ((ProgressBar) component).getProgressShape();
                if(component instanceof Slider) return ((Slider) component).getProgressBar().getProgressShape();
                break;
        }
        return null;
    }

    protected AttributeVariableDouble getAttributeVariableDouble(Shape shape, String attribute, State state){
        switch (attribute){
            case NetworkInterface.RELATIVE_X: return component.getPositionX().getAttributeRelative().getAttribute(state);
            case NetworkInterface.RELATIVE_Y: return component.getPositionY().getAttributeRelative().getAttribute(state);
            case NetworkInterface.WIDTH: return shape.getAttributeWidth().getAttribute(state);
            case NetworkInterface.HEIGHT: return shape.getAttributeHeight().getAttribute(state);
            case NetworkInterface.BORDER_TOP: return shape.getAttributeBorder(io.github.minecraftgui.models.shapes.Border.TOP).getAttribute(state);
            case NetworkInterface.BORDER_LEFT: return shape.getAttributeBorder(io.github.minecraftgui.models.shapes.Border.LEFT).getAttribute(state);
            case NetworkInterface.BORDER_RIGHT: return shape.getAttributeBorder(io.github.minecraftgui.models.shapes.Border.RIGHT).getAttribute(state);
            case NetworkInterface.BORDER_BOTTOM: return shape.getAttributeBorder(io.github.minecraftgui.models.shapes.Border.BOTTOM).getAttribute(state);
            case NetworkInterface.PADDING_TOP: return shape.getAttributePadding(io.github.minecraftgui.models.shapes.Padding.TOP).getAttribute(state);
            case NetworkInterface.PADDING_LEFT: return shape.getAttributePadding(io.github.minecraftgui.models.shapes.Padding.LEFT).getAttribute(state);
            case NetworkInterface.PADDING_RIGHT: return shape.getAttributePadding(io.github.minecraftgui.models.shapes.Padding.RIGHT).getAttribute(state);
            case NetworkInterface.PADDING_BOTTOM: return shape.getAttributePadding(io.github.minecraftgui.models.shapes.Padding.BOTTOM).getAttribute(state);
            case NetworkInterface.MARGIN_TOP: return shape.getAttributeMargin(io.github.minecraftgui.models.shapes.Margin.TOP).getAttribute(state);
            case NetworkInterface.MARGIN_LEFT: return shape.getAttributeMargin(io.github.minecraftgui.models.shapes.Margin.LEFT).getAttribute(state);
            case NetworkInterface.MARGIN_RIGHT: return shape.getAttributeMargin(io.github.minecraftgui.models.shapes.Margin.RIGHT).getAttribute(state);
            case NetworkInterface.MARGIN_BOTTOM: return shape.getAttributeMargin(io.github.minecraftgui.models.shapes.Margin.BOTTOM).getAttribute(state);
            default: return null;
        }
    }

    protected void setAttributeDouble(AttributeVariableDouble attributeVariableDouble, JSONObject attribute, MainController mainController){
        if(attribute.has(NetworkInterface.COMPONENT_ID) && attribute.has(NetworkInterface.ATTRIBUTE) && attribute.has(NetworkInterface.SHAPE)){
            Component compToLink = mainController.getComponent(attribute.getString(NetworkInterface.COMPONENT_ID));

            attributeVariableDouble.setAttribute(getAttributeDouble(getShape(compToLink, attribute.getString(NetworkInterface.SHAPE)), attribute.getString(NetworkInterface.ATTRIBUTE)));
            attributeVariableDouble.setPercentage(attribute.getDouble(NetworkInterface.PERCENTAGE));
            attributeVariableDouble.setTime(attribute.getLong(NetworkInterface.TIME));
        }
        else if(attribute.has(NetworkInterface.VALUE)){
            attributeVariableDouble.setAttribute(new AttributeDouble(attribute.getDouble(NetworkInterface.VALUE)));
            attributeVariableDouble.setPercentage(attribute.getDouble(NetworkInterface.PERCENTAGE));
            attributeVariableDouble.setTime(attribute.getLong(NetworkInterface.TIME));
        }
    }

    protected Attribute<Double> getAttributeDouble(Shape shape, String attribute){
        switch (attribute){
            case NetworkInterface.RELATIVE_X: return component.getPositionX().getAttributeRelative();
            case NetworkInterface.RELATIVE_Y: return component.getPositionY().getAttributeRelative();
            case NetworkInterface.WIDTH: return shape.getAttributeWidth();
            case NetworkInterface.HEIGHT: return shape.getAttributeHeight();
            case NetworkInterface.BORDER_TOP: return shape.getAttributeBorder(io.github.minecraftgui.models.shapes.Border.TOP);
            case NetworkInterface.BORDER_LEFT: return shape.getAttributeBorder(io.github.minecraftgui.models.shapes.Border.LEFT);
            case NetworkInterface.BORDER_RIGHT: return shape.getAttributeBorder(io.github.minecraftgui.models.shapes.Border.RIGHT);
            case NetworkInterface.BORDER_BOTTOM: return shape.getAttributeBorder(io.github.minecraftgui.models.shapes.Border.BOTTOM);
            case NetworkInterface.PADDING_TOP: return shape.getAttributePadding(io.github.minecraftgui.models.shapes.Padding.TOP);
            case NetworkInterface.PADDING_LEFT: return shape.getAttributePadding(io.github.minecraftgui.models.shapes.Padding.LEFT);
            case NetworkInterface.PADDING_RIGHT: return shape.getAttributePadding(io.github.minecraftgui.models.shapes.Padding.RIGHT);
            case NetworkInterface.PADDING_BOTTOM: return shape.getAttributePadding(io.github.minecraftgui.models.shapes.Padding.BOTTOM);
            case NetworkInterface.MARGIN_TOP: return shape.getAttributeMargin(io.github.minecraftgui.models.shapes.Margin.TOP);
            case NetworkInterface.MARGIN_LEFT: return shape.getAttributeMargin(io.github.minecraftgui.models.shapes.Margin.LEFT);
            case NetworkInterface.MARGIN_RIGHT: return shape.getAttributeMargin(io.github.minecraftgui.models.shapes.Margin.RIGHT);
            case NetworkInterface.MARGIN_BOTTOM: return shape.getAttributeMargin(io.github.minecraftgui.models.shapes.Margin.BOTTOM);
            case NetworkInterface.TEXT:
                if(component instanceof Paragraph)
                    return ((Paragraph) component).getTextField().getTextHeight();
                if(component instanceof TextArea)
                    return ((TextArea) component).getTextField().getTextHeight();
            default: return null;
        }
    }

    protected void setAttributeColor(AttributeVariableColor attributeVariableColor, JSONObject attribute, MainController mainController){
        if(attribute.has(NetworkInterface.COMPONENT_ID) && attribute.has(NetworkInterface.ATTRIBUTE) && attribute.has(NetworkInterface.SHAPE)){
            Component compToLink = mainController.getComponent(attribute.getString(NetworkInterface.COMPONENT_ID));

            attributeVariableColor.setAttribute(getAttributeGroupColor(getShape(compToLink, attribute.getString(NetworkInterface.SHAPE)), attribute.getString(NetworkInterface.ATTRIBUTE)));
            attributeVariableColor.setPercentage(attribute.getDouble(NetworkInterface.PERCENTAGE));
            attributeVariableColor.setTime(attribute.getLong(NetworkInterface.TIME));
        }
        else if(attribute.has(NetworkInterface.VALUE)){
            attributeVariableColor.setAttribute(new AttributeColor(getColor(attribute.getJSONObject(NetworkInterface.VALUE))));
            attributeVariableColor.setPercentage(attribute.getDouble(NetworkInterface.PERCENTAGE));
            attributeVariableColor.setTime(attribute.getLong(NetworkInterface.TIME));
        }
    }

    protected Color getColor(JSONObject j){
        return new Color(j.getInt("r"), j.getInt("g"), j.getInt("b"), j.getInt("a"));
    }

    protected AttributeVariableColor getAttributeVariableColor(Shape shape, String attribute, State state){
        switch (attribute.toUpperCase()){
            case NetworkInterface.BACKGROUND_COLOR: return (AttributeVariableColor) shape.getAttributeBackground().getAttribute(state);
            case NetworkInterface.BORDER_TOP_COLOR: return shape.getAttributeBorderColor(io.github.minecraftgui.models.shapes.Border.TOP).getAttribute(state);
            case NetworkInterface.BORDER_LEFT_COLOR: return shape.getAttributeBorderColor(io.github.minecraftgui.models.shapes.Border.LEFT).getAttribute(state);
            case NetworkInterface.BORDER_RIGHT_COLOR: return shape.getAttributeBorderColor(io.github.minecraftgui.models.shapes.Border.RIGHT).getAttribute(state);
            case NetworkInterface.BORDER_BOTTOM_COLOR: return shape.getAttributeBorderColor(io.github.minecraftgui.models.shapes.Border.BOTTOM).getAttribute(state);
            default: return null;
        }
    }

    protected AttributeGroupColor getAttributeGroupColor(Shape shape, String attribute){
        switch (attribute.toUpperCase()){
            case NetworkInterface.BACKGROUND_COLOR: return (AttributeGroupColor) shape.getAttributeBackground();
            case NetworkInterface.BORDER_TOP_COLOR: return shape.getAttributeBorderColor(io.github.minecraftgui.models.shapes.Border.TOP);
            case NetworkInterface.BORDER_LEFT_COLOR: return shape.getAttributeBorderColor(io.github.minecraftgui.models.shapes.Border.LEFT);
            case NetworkInterface.BORDER_RIGHT_COLOR: return shape.getAttributeBorderColor(io.github.minecraftgui.models.shapes.Border.RIGHT);
            case NetworkInterface.BORDER_BOTTOM_COLOR: return shape.getAttributeBorderColor(io.github.minecraftgui.models.shapes.Border.BOTTOM);
            default: return null;
        }
    }

    private static class PacketSetAttributeDouble extends PacketSetAttribute {

        public PacketSetAttributeDouble(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface, String attribute) {
            super(jsonObject, mainController, networkInterface);
            setAttributeDouble(getAttributeVariableDouble(shape, attribute, state), this.attribute, mainController);
        }

    }

    private static class PacketSetAttributeColor extends PacketSetAttribute {

        public PacketSetAttributeColor(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface, String attribute) {
            super(jsonObject, mainController, networkInterface);
            setAttributeColor(getAttributeVariableColor(shape, attribute, state), this.attribute, mainController);
        }

    }

    public static class BackgroundImage extends PacketSetAttribute {

        public BackgroundImage(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);

            if(shape instanceof RectangleImage)
                shape.getAttributeBackground().getAttribute(state).setValue(mainController.getImageRepository().getImage(attribute.getString(NetworkInterface.VALUE)));
        }
    }

    public static class BackgroundColor extends PacketSetAttributeColor {

        public BackgroundColor(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.BACKGROUND_COLOR);
        }
    }

    public static class BorderTopColor extends PacketSetAttributeColor{

        public BorderTopColor(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.BORDER_TOP_COLOR);
        }
    }

    public static class BorderLeftColor extends PacketSetAttributeColor{

        public BorderLeftColor(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.BORDER_LEFT_COLOR);
        }

    }

    public static class BorderRightColor extends PacketSetAttributeColor{

        public BorderRightColor(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.BORDER_RIGHT_COLOR);
        }

    }

    public static class BorderBottomColor extends PacketSetAttributeColor{

        public BorderBottomColor(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.BORDER_BOTTOM_COLOR);
        }

    }

    public static class BorderLeft extends PacketSetAttributeDouble{

        public BorderLeft(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.BORDER_LEFT);
        }
    }

    public static class BorderTop extends PacketSetAttributeDouble{

        public BorderTop(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.BORDER_TOP);
        }
    }

    public static class BorderRight extends PacketSetAttributeDouble{

        public BorderRight(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.BORDER_RIGHT);
        }
    }

    public static class BorderBottom extends PacketSetAttributeDouble{

        public BorderBottom(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.BORDER_BOTTOM);
        }
    }

    public static class PaddingTop extends PacketSetAttributeDouble {

        public PaddingTop(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.PADDING_TOP);
        }
    }

    public static class PaddingLeft extends PacketSetAttributeDouble {

        public PaddingLeft(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.PADDING_LEFT);
        }
    }

    public static class PaddingRight extends PacketSetAttributeDouble {

        public PaddingRight(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.PADDING_RIGHT);
        }
    }

    public static class PaddingBottom extends PacketSetAttributeDouble {

        public PaddingBottom(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.PADDING_BOTTOM);
        }
    }

    public static class MarginLeft extends PacketSetAttributeDouble {

        public MarginLeft(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.MARGIN_LEFT);
        }
    }

    public static class MarginTop extends PacketSetAttributeDouble {

        public MarginTop(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.MARGIN_TOP);
        }
    }

    public static class MarginBottom extends PacketSetAttributeDouble {

        public MarginBottom(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.MARGIN_BOTTOM);
        }
    }

    public static class MarginRight extends PacketSetAttributeDouble {

        public MarginRight(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.MARGIN_RIGHT);
        }
    }

    public static class Width extends PacketSetAttributeDouble {

        public Width(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.WIDTH);
        }
    }

    public static class Height extends PacketSetAttributeDouble {

        public Height(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.HEIGHT);
        }
    }

    public static class TextAlignment extends PacketSetAttribute{

        public TextAlignment(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);

            if(component instanceof TextArea)
                ((TextArea) component).setAlignment(Text.TextAlignement.valueOf(attribute.getString(NetworkInterface.VALUE)));
            else if(component instanceof Paragraph)
                ((Paragraph) component).setAlignment(Text.TextAlignement.valueOf(attribute.getString(NetworkInterface.VALUE)));
        }
    }

    public static class Cursor extends PacketSetAttribute{

        public Cursor(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);
            component.setCursor(state, Mouse.Cursor.valueOf(attribute.getString(NetworkInterface.VALUE)));
        }
    }

    public static class Visibility extends PacketSetAttribute{

        public Visibility(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);
            component.setVisibility(io.github.minecraftgui.models.components.Visibility.valueOf(attribute.getString(NetworkInterface.VALUE)));
        }
    }

    public static class XRelativeTo extends PacketSetAttribute {

        public XRelativeTo(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);
            AttributeVariableDouble attributeVariableDouble = new AttributeVariableDouble(null, null);

            jsonObject.put(NetworkInterface.TIME, 0);

            setAttributeDouble(attributeVariableDouble, attribute, mainController);

            component.getPositionX().getRelativeToAttributes().add(attributeVariableDouble);
        }

    }

    public static class YRelativeTo extends PacketSetAttribute {

        public YRelativeTo(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);
            AttributeVariableDouble attributeVariableDouble = new AttributeVariableDouble(null, null);

            jsonObject.put(NetworkInterface.TIME, 0);

            setAttributeDouble(attributeVariableDouble, attribute, mainController);

            component.getPositionY().getRelativeToAttributes().add(attributeVariableDouble);
        }

    }

    public static class YRelative extends PacketSetAttributeDouble {

        public YRelative(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.RELATIVE_Y);
        }

    }

    public static class XRelative extends PacketSetAttributeDouble {

        public XRelative(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface, NetworkInterface.RELATIVE_X);
        }

    }

    public static class Font extends PacketSetAttribute{

        public Font(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);

            if(component instanceof ComponentText)
                ((ComponentText) component).setFont(state, mainController.getFontRepository().getFont(attribute.getString(NetworkInterface.VALUE)));
        }
    }

    public static class FontSize extends PacketSetAttribute{

        public FontSize(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);

            if(component instanceof ComponentText)
                ((ComponentText) component).setFontSize(state, attribute.getInt(NetworkInterface.VALUE));
        }
    }

    public static class FontColor extends PacketSetAttribute{

        public FontColor(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);

            if(component instanceof ComponentText)
                ((ComponentText) component).setFontColor(state, getColor(attribute.getJSONObject(NetworkInterface.VALUE)));
        }
    }

    public static class CursorColor extends PacketSetAttribute{

        public CursorColor(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);

            if(component instanceof ComponentEditableText)
                ((ComponentEditableText) component).getTextCursorColor(state).setAttribute(new AttributeColor(getColor(attribute.getJSONObject(NetworkInterface.VALUE))));
        }
    }

    public static class Value extends PacketSetAttribute{

        public Value(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);

            if(component instanceof ComponentValuable)
                ((ComponentValuable) component).setValue(attribute.get(NetworkInterface.VALUE));
        }
    }

    public static class Positions extends PacketSetAttribute{

        public Positions(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);

            if(shape instanceof PolygonColor){
                PolygonColor polygonColor = (PolygonColor) shape;
                JSONArray p = attribute.getJSONArray(NetworkInterface.VALUE);
                double positions[][] = new double[p.length()][2];

                for(int i = 0; i < p.length(); i++){
                    JSONObject j = p.getJSONObject(i);

                    positions[i][0] = j.getDouble(NetworkInterface.X);
                    positions[i][1] = j.getDouble(NetworkInterface.Y);
                }

                polygonColor.setPositions(positions);
            }
        }
    }

    public static class TextNbLine extends PacketSetAttribute{

        public TextNbLine(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);

            if(component instanceof TextArea)
                ((TextArea) component).setNbLinesToDisplay(attribute.getInt(NetworkInterface.VALUE));
            else if(component instanceof Paragraph)
                ((Paragraph) component).setNbLinesToDisplay(attribute.getInt(NetworkInterface.VALUE));
        }
    }

    public static class UpdateList extends PacketSetAttribute{

        public UpdateList(JSONObject jsonObject, MainController mainController, NetworkInterface networkInterface) {
            super(jsonObject, mainController, networkInterface);

            if(component instanceof List)
                ((List) component).updateLists();
        }
    }

}
