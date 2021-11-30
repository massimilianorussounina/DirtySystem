package com.example.dirtsystemec;

import java.util.HashMap;
import java.util.Map;

public class Entity {
    private final Map<ComponentType,Component> components = new HashMap<>();



    public void addComponent( Component component){
        component.setOwner(this);
        components.put(component.type(),component);
    }

    public Component getComponent(ComponentType type){
        return components.get(type);
    }

}
