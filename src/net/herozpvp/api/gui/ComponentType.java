package net.herozpvp.api.gui;

public enum ComponentType {
    COMPONENT("Component"), FORM("Form")
    ;

    private final String name;
        
    private ComponentType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
