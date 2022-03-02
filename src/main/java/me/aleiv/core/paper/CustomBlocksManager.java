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

    public CustomBlock getCustomBlock(String name) {
        return customBlocks.containsKey(name) ? customBlocks.get(name) : null;
    }

    public CustomBlock getDecoItemByBlockID(String blockID) {
        return customBlocks.values().stream().filter(decoItem -> decoItem.getBlockID().equals(blockID)).findAny()
                .orElse(null);
    }

    public boolean isCustomBlock(ItemStack item) {
        if (!item.hasItemMeta())
            return false;
        var meta = item.getItemMeta();
        if ((meta.hasCustomModelData() && (item.getType() == Material.RABBIT_HIDE || item.getType() == Material.NOTE_BLOCK)) || isDecoHammer(item)) {
            var data = meta.getCustomModelData();
            return !customBlocks.values().stream().filter(deco -> deco.getCustomModelData() == data).toList().isEmpty();
        }
        return false;
    }

    public CustomBlock getCustomBlock(ItemStack item) {
        var meta = item.getItemMeta();
        if (isDecoItem(item)) {
            var data = meta.getCustomModelData();
            return customBlocks.values().stream().filter(deco -> deco.getCustomModelData() == data).findAny().orElse(null);
        }
        return null;
    }

    public CustomBlock getCustomBlock(Location loc) {
        var world = loc.getWorld();
        var noteBlockManager = instance.getNoteBlockManager();
        var block = world.getBlockAt(loc);
        if(isCustomBlock(block)){
            
        }
        return null;

    }

    public boolean isCustomBlock(Block block){
        var noteBlockManager = instance.getNoteBlockManager();
        noteBlockManager.isNoteBlock(block);
        return false;
    }

}
