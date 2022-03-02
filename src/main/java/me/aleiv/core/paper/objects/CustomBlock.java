package me.aleiv.core.paper.objects;

import lombok.Data;

@Data
public class CustomBlock {

    String name;
    String blockID;

    public CustomBlock(String name, String blockID) {
        this.name = name;
        this.blockID = blockID;
        
    }

}