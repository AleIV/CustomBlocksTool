package me.aleiv.core.paper;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.aleiv.core.paper.objects.CustomBlock;

public class CustomBlocksManager{

    Core instance;
    @Getter HashMap<String, CustomBlock> customBlocks = new HashMap<>();

    public CustomBlocksManager(Core instance) {
        this.instance = instance;

    }

    public boolean hasCustomBlock(String blockID) {
        return !customBlocks.values().stream().filter(customBlock -> customBlock.getBlockID() == blockID).toList().isEmpty();
    }

    public CustomBlock getCustomBlockByBlockID(String blockID) {
        return customBlocks.values().stream().filter(customBlock -> customBlock.getBlockID().equals(blockID)).findAny()
                .orElse(null);
    }

    public boolean isCustomBlock(ItemStack item) {
        if (item.getType() != Material.NOTE_BLOCK || !item.hasItemMeta() || !item.getItemMeta().hasCustomModelData())
            return false;
        var meta = item.getItemMeta();
        if (meta.hasCustomModelData()) {
            var data = meta.getCustomModelData();
            return !customBlocks.values().stream().filter(deco -> deco.getCustomModelData() == data).toList().isEmpty();
        }
        return false;
    }

    public CustomBlock getCustomBlock(ItemStack item) {
        
        if (isCustomBlock(item)) {
            var meta = item.getItemMeta();
            var data = meta.getCustomModelData();
            return customBlocks.values().stream().filter(deco -> deco.getCustomModelData() == data).findAny().orElse(null);
        }
        return null;
    }

    public CustomBlock getCustomBlock(Location loc) {
        var block = loc.getBlock();
        if(isCustomBlock(block)){
            var noteBlockManager = instance.getNoteBlockManager();

            var blockID = noteBlockManager.getBlockID(block);
            return getCustomBlock(blockID);
        }
        return null;
    }

    public CustomBlock getCustomBlock(String blockID){
        return customBlocks.values().stream().filter(customBlock -> customBlock.getBlockID() == blockID).findAny().orElse(null);
    }

    public boolean isCustomBlock(Block block){
        var noteBlockManager = instance.getNoteBlockManager();
        if(!noteBlockManager.isNoteBlock(block)) return false;

        var blockID = noteBlockManager.getBlockID(block);
        return !customBlocks.values().stream().filter(customBlock -> customBlock.getBlockID() == blockID).toList().isEmpty();
    }

}
