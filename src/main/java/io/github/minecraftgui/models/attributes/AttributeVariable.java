package io.github.minecraftgui.models.attributes;

/**
 * Created by Samuel on 2015-12-13.
 */
public abstract class AttributeVariable<V> extends Attribute<V>{

    private final AttributeGroup<V> attributeGroup;
    private Attribute<V> valueEndTransition;//This attribute is the value the transition will be at the end or the value changed by the percentage.
    private double percentage = 1;
    private double time = 0;//millisecond
    private long timeStarted = System.currentTimeMillis();
    private V valueAtStartOfAnimation;
    private double timePercentage = 0;
    private double percentageLastUpdate = 0;
    private V valueEndTransitionLastUpdate;
    private long lastUpdateId = Long.MIN_VALUE;

    protected abstract V getValue(V value, double percentage);
    protected abstract V getValue(V valueAtTheEnd, V valueAtStart, double percentage, double timePercentage);

    public AttributeVariable(Attribute<V> value, AttributeGroup<V> attributeGroup) {
        super(null);
        this.valueEndTransition = value;
        this.attributeGroup = attributeGroup;
    }

    public AttributeVariable(Attribute<V> value, AttributeGroup<V> attributeGroup, double percentage) {
        super(null);
        this.valueEndTransition = value;
        this.attributeGroup = attributeGroup;
        this.percentage = percentage;
    }

    public AttributeVariable(Attribute<V> value, AttributeGroup<V> attributeGroup, long time) {
        super(null);
        this.valueEndTransition = value;
        this.attributeGroup = attributeGroup;
        this.time = time;
    }

    public AttributeVariable(Attribute<V> value, AttributeGroup<V> attributeGroup, double percentage, long time) {
        super(null);
        this.valueEndTransition = value;
        this.attributeGroup = attributeGroup;
        this.percentage = percentage;
        this.time = time;
    }

    public void setAttribute(Attribute<V> value) {
        this.valueEndTransition = value;
        this.value = value.getValue();
    }

    public double getTime() {
        return time;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    @Override
    public void update(long updateId){
        if(lastUpdateId != updateId) {
            if (valueEndTransition != null) {
                valueEndTransition.update(updateId);
                boolean attrChanged = valueEndTransitionLastUpdate == null ? true : !valueEndTransitionLastUpdate.equals(valueEndTransition.getValue());
                boolean percentageChanged = percentage != percentageLastUpdate;

                if (time > 0 || (attrChanged && time > 0) || (percentageChanged && time > 0)) {
                    long currentTime = System.currentTimeMillis();

                    if (lastUpdateId + 1 != updateId || attrChanged || percentageChanged) {
                        timeStarted = currentTime;
                        valueAtStartOfAnimation = attributeGroup.getValue();
                        timePercentage = 0;
                    }
                    double lastTimePercentage = timePercentage;

                    valueAtStartOfAnimation = valueAtStartOfAnimation == null?attributeGroup.getDefaultValue():valueAtStartOfAnimation;
                    timePercentage = (currentTime - timeStarted) / time;

                    if (lastTimePercentage != 1) {
                        timePercentage = timePercentage > 1 ? 1 : timePercentage;
                        this.value = getValue(valueEndTransition.getValue(), valueAtStartOfAnimation, percentage, timePercentage);
                    }

                } else if (percentageChanged || attrChanged)
                    this.value = getValue(valueEndTransition.getValue(), percentage);

                if (percentageChanged)
                    percentageLastUpdate = percentage;

                valueEndTransitionLastUpdate = valueEndTransition.getValue();
            }

            lastUpdateId = updateId;
        }
    }

}
