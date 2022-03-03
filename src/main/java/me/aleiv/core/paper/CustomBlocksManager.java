package me.aleiv.core.paper;

import java.util.HashMap;

import com.google.gson.JsonObject;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.aleiv.core.paper.objects.CustomBlock;
import me.aleiv.core.paper.utilities.JsonConfig;

public class CustomBlocksManager{

    Core instance;
    @Getter HashMap<String, CustomBlock> customBlocks = new HashMap<>();

    public CustomBlocksManager(Core instance) {
        this.instance = instance;

        pullJson();
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

    public void pushJson(){
        try {
            var list = customBlocks;
            var gson = instance.getGson();
            var jsonConfig = new JsonConfig("customblocks.json");
            var json = gson.toJson(list);
            var obj = gson.fromJson(json, JsonObject.class);
            jsonConfig.setJsonObject(obj);
            jsonConfig.save();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void pullJson(){
        try {
            var jsonConfig = new JsonConfig("customblocks.json");
            var list = jsonConfig.getJsonObject();
            var iter = list.entrySet().iterator();
            var map = customBlocks;
            var gson = instance.getGson();

            while (iter.hasNext()) {
                var entry = iter.next();
                var name = entry.getKey();
                var value = entry.getValue();
                var obj = gson.fromJson(value, CustomBlock.class);
                map.put(name, obj);

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

}
