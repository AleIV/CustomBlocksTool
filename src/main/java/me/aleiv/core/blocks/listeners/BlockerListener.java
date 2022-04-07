package me.aleiv.core.blocks.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import me.aleiv.core.blocks.CoreBlocks;

public class BlockerListener implements Listener{
    
    CoreBlocks instance;

    public BlockerListener(CoreBlocks instance){
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPhysics(BlockPhysicsEvent e) {
        var block = e.getBlock();
        var aboveBlock = block.getLocation().add(0, 1, 0).getBlock();
        if (aboveBlock.getType() == Material.NOTE_BLOCK) {
            updateAndCheck(block.getLocation());
            e.setCancelled(true);
        }
        if (block.getType() == Material.NOTE_BLOCK)
            e.setCancelled(true);

        if (block.getType().toString().toLowerCase().contains("sign"))
            return;
        block.getState().update(true, false);

    }

    public void updateAndCheck(Location loc) {
        Block b = loc.add(0, 1, 0).getBlock();
        if (b.getType() == Material.NOTE_BLOCK)
            b.getState().update(true, true);
        Location nextBlock = b.getLocation().add(0, 1, 0);
        if (nextBlock.getBlock().getType() == Material.NOTE_BLOCK)
            updateAndCheck(b.getLocation());
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        var hand = e.getHand();
        var player = e.getPlayer();
        var equip = player.getEquipment();
        
        if(hand == null) return;

        var item = equip.getItem(hand);

        var block = e.getClickedBlock();
        var action = e.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK && block != null && block.getType() == Material.NOTE_BLOCK) {

            if(player.isSneaking() && item.getType() != Material.AIR) return;
                
            e.setCancelled(true);

            var tool = instance.getNoteBlockManager();
            if (tool.isDefaultNoteBlock(block)) {
                //TODO: vanilla noteblock
                //player.sendMessage("VANILLA BLOCK");
            }
        }
    }

    @EventHandler
    public void onNote(NotePlayEvent e) {
        e.setCancelled(true);
    }

}
