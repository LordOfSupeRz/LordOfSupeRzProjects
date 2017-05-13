package net.herozpvp.api.gui;

public class ComponentGUI implements Component{
    
    private static int LAST_ID = 0;
    
    private final int id;
    private final ComponentType type;
    
    public ComponentGUI(ComponentType type) {
        LAST_ID++;
        this.type = type;
        this.id = LAST_ID;
    }

    @Override
    public int getId(){
        return id;
    }
    
    @Override
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
