package net.herozpvp.api.gui;

public class Component {
    
    private static int LAST_ID = 0;
    
    private final int id;
    private final ComponentType type;
    
    public Component(ComponentType type) {
        LAST_ID++;
        this.type = type;
        this.id = LAST_ID;
    }

    public int getId(){
        return id;
    }
    
    public ComponentType getType(){
        return type;
    }
    
    @Override
    public String toString() {
        return String.format("Component [id:{%s},type:{%s}]"
        , getId(), getType());
    }
    
    public void typeString(){
        System.out.println(toString());
    }
}
