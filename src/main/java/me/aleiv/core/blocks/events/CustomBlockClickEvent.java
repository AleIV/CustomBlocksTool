package me.aleiv.core.blocks.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;

import lombok.Getter;

public class CustomBlockClickEvent extends Event {
    
    private static final @Getter HandlerList HandlerList = new HandlerList();
    @SuppressWarnings({"java:S116", "java:S1170"})
    private final @Getter HandlerList Handlers = HandlerList;

    private final @Getter String blockID;
    private final @Getter Block block;
    private final @Getter Player player;
    private final @Getter Action action;

    public CustomBlockClickEvent(String blockID, Block block, Player player, Action action){

        this.blockID = blockID;
        this.block = block;
        this.player = player;
        this.action = action;
    }

}