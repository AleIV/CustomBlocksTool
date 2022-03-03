package me.aleiv.core.paper.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.events.CustomBlockClickEvent;
import net.md_5.bungee.api.ChatColor;

public class NoteBlockListener implements Listener {

    Core instance;

    public NoteBlockListener(Core instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onClick(CustomBlockClickEvent e){
        var player = e.getPlayer();
        player.sendMessage(ChatColor.AQUA + e.toString());
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        var block = e.getClickedBlock();

        if (block != null && block.getType() == Material.NOTE_BLOCK) {
            var customBlockManager = instance.getCustomBlocksManager();
            var action = e.getAction();

            if (customBlockManager.isCustomBlock(block) && (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK)) {
                var noteBlockManager = instance.getNoteBlockManager();
                var noteBlock = noteBlockManager.getNoteBlockData(block);
                var blockID = noteBlockManager.getBlockID(noteBlock);
                var player = e.getPlayer();

                Bukkit.getPluginManager().callEvent(new CustomBlockClickEvent(blockID, block, player, action));

            }

        }

    }
    
    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        var block = e.getBlock();
        var item = e.getItemInHand();
        var manager = instance.getCustomBlocksManager();

        if (block.getType() == Material.NOTE_BLOCK && item != null) {
            var tool = instance.getNoteBlockManager();

            if (manager.isCustomBlock(item)) {
                var customBlock = manager.getCustomBlock(item);
                var noteBlock = tool.getNoteBlockData(customBlock.getBlockID());
                if (noteBlock != null) {
                    block.setBlockData(noteBlock);
                }
            } else {
                block.setBlockData(tool.getDefaultBlockData());
            }

        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        var block = e.getBlock();
        var manager = instance.getCustomBlocksManager();
        var tool = instance.getNoteBlockManager();

        if (block.getType() == Material.NOTE_BLOCK) {
            if (tool.isDefaultNoteBlock(block))
                return;
            e.setDropItems(false);

            var noteBlock = tool.getNoteBlockData(block);
            var blockID = tool.getBlockID(noteBlock);
            var customBlock = manager.getCustomBlockByBlockID(blockID);

            if (customBlock != null) {
                var loc = block.getLocation();
                block.getWorld().dropItemNaturally(loc, customBlock.getItemStack());
            }

        }
    }
}