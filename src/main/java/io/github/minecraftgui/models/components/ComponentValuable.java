package io.github.minecraftgui.models.components;

import io.github.minecraftgui.models.listeners.OnInputListener;
import io.github.minecraftgui.models.listeners.OnValueChangeListener;
import io.github.minecraftgui.models.shapes.Shape;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Samuel on 2015-12-11.
 */
public abstract class ComponentValuable<V> extends Component{

    private final CopyOnWriteArrayList<OnValueChangeListener> listeners;

    public abstract V getValue();
    public abstract void setValue(V value);

    public ComponentValuable(String id, Class<? extends Shape> shape) {
        super(id, shape);
        this.listeners = new CopyOnWriteArrayList<>();
    }

    public void addOnValueChangeListener(OnValueChangeListener listener){
        this.listeners.add(listener);
    }

    protected void valueChanged(){
        for(OnValueChangeListener listener : listeners)
            listener.onValueChange(this);
    }

}
