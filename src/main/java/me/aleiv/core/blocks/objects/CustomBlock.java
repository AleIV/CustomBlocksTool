package me.aleiv.core.blocks.objects;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import fr.mrmicky.fastinv.ItemBuilder;
import lombok.Data;
import net.md_5.bungee.api.ChatColor;

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

    public ItemStack getItemStack() {
        return new ItemBuilder(Material.NOTE_BLOCK).meta(meta -> meta.setCustomModelData(customModelData))
        .name(ChatColor.WHITE + name).build();
    }

}