package io.github.minecraftgui.models.components;

import io.github.minecraftgui.controllers.Mouse;
import io.github.minecraftgui.models.listeners.OnClickListener;
import io.github.minecraftgui.models.shapes.Border;
import io.github.minecraftgui.models.shapes.Padding;
import io.github.minecraftgui.models.shapes.Rectangle;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2015-11-29.
 */
public class List extends Component {

    private final Component buttonListAfter;
    private final Component buttonListBefore;
    private final ArrayList<CopyOnWriteArrayList<Component>> listsDisplayed;
    private CopyOnWriteArrayList<Component> currentListDisplayed = null;
    private double heightLastUpdate = 0;
    private long nextUpdateIdToUpdateLists = Long.MIN_VALUE;
    private long updateId = Long.MIN_VALUE;

    public List(String id, Class<? extends Rectangle> shape, Component buttonListBefore, Component  buttonListAfter) {
        super(id, shape);
        listsDisplayed = new ArrayList<>();
        listsDisplayed.add(new CopyOnWriteArrayList<Component>());
        currentListDisplayed = listsDisplayed.get(0);
        this.buttonListAfter = buttonListAfter;
        this.buttonListBefore = buttonListBefore;

        this.buttonListAfter.parent = this;
        this.buttonListAfter.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Component component, Mouse mouse) {
                int index = listsDisplayed.indexOf(currentListDisplayed);

                if (listsDisplayed.size() > index + 1)
                    currentListDisplayed = listsDisplayed.get(index + 1);
            }
        });

        this.buttonListBefore.parent = this;
        this.buttonListBefore.addOnClickListener(new OnClickListener() {
            @Override
            public void onClick(Component component, Mouse mouse) {
                int index = listsDisplayed.indexOf(currentListDisplayed);

                if (index != 0)
                    currentListDisplayed = listsDisplayed.get(index - 1);
            }
        });

        updateListsDisplayed();
    }

    public void updateLists(){
        nextUpdateIdToUpdateLists = updateId+2;
    }

    public Component getButtonListAfter() {
        return buttonListAfter;
    }

    public Component getButtonListBefore() {
        return buttonListBefore;
    }

    @Override
    public CopyOnWriteArrayList<Component> getChildren() {
        return currentListDisplayed;
    }

    @Override
    public void add(Component component){
        if(buttonListAfter != component && buttonListBefore != component)
            super.add(component);
    }

    @Override
    protected void remove(Component component){
        if(component != buttonListAfter && component != buttonListBefore ) {
            super.remove(component);
            updateListsDisplayed();
        }
    }

    @Override
    public void update(long updateId) {
        super.update(updateId);
        this.updateId = updateId;

        if(heightLastUpdate != this.getHeight() || nextUpdateIdToUpdateLists == updateId)
            updateListsDisplayed();

        heightLastUpdate = this.getHeight();
    }

    private void updateListsDisplayed(){
        CopyOnWriteArrayList<Component> currentList = new CopyOnWriteArrayList();
        int index = listsDisplayed.indexOf(currentListDisplayed);
        double height = getHeight();
        double currentHeight = 0;

        listsDisplayed.clear();

        for(Component component : children){
            double componentHeight = component.getHeight()+component.getShape().getPadding(Padding.TOP)+component.getShape().getPadding(Padding.BOTTOM)+component.getShape().getBorder(Border.TOP)+component.getShape().getBorder(Border.BOTTOM);

            if(currentHeight+componentHeight >= height){
                listsDisplayed.add(currentList);

                currentList.add(buttonListBefore);
                currentList.add(buttonListAfter);

                currentList = new CopyOnWriteArrayList<>();
                currentHeight = 0;
            }

            currentHeight += componentHeight;

            component.getPositionY().getRelativeToAttributes().clear();

            if(currentList.size() == 0){
                component.getPositionY().getRelativeToAttributes().add(component.getShape().getAttributePadding(Padding.TOP));
                component.getPositionY().getRelativeToAttributes().add(component.getShape().getAttributeBorder(Border.TOP));
            }
            else{
                Component lastChild = currentList.get(currentList.size()-1);

                component.getPositionY().getRelativeToAttributes().addAll(lastChild.getPositionY().getRelativeToAttributes());
                component.getPositionY().getRelativeToAttributes().add(lastChild.getShape().getAttributePadding(Padding.BOTTOM));
                component.getPositionY().getRelativeToAttributes().add(lastChild.getShape().getAttributeBorder(Border.BOTTOM));
                component.getPositionY().getRelativeToAttributes().add(lastChild.getAttributeHeight());
                component.getPositionY().getRelativeToAttributes().add(component.getShape().getAttributePadding(Padding.TOP));
                component.getPositionY().getRelativeToAttributes().add(component.getShape().getAttributeBorder(Border.TOP));
            }

            currentList.add(component);
        }

        listsDisplayed.add(currentList);

        currentList.add(buttonListBefore);
        currentList.add(buttonListAfter);

        while(index >= listsDisplayed.size() && index != 0)
            index--;

        currentListDisplayed = listsDisplayed.get(index);
    }

}
