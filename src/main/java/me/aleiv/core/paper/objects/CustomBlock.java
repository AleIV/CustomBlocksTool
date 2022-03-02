package me.aleiv.core.paper.objects;

import lombok.Data;

@Data
public class CustomBlock {

    String name;
    String blockID;
    int customModelData;

    public CustomBlock(String name, String blockID, int customModelData) {
        this.name = name;
        this.blockID = blockID;
        this.customModelData = customModelData;
        
    }

}