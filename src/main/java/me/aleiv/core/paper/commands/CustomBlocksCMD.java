package me.aleiv.core.paper.commands;

import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("customblocks")
@CommandPermission("customblocks.cmd")
public class CustomBlocksCMD extends BaseCommand {

    private @NonNull Core instance;

    public CustomBlocksCMD(Core instance) {
        this.instance = instance;

    }

    @Subcommand("give")
    public void give(Player sender, String customBlock){

        
        sender.sendMessage(ChatColor.BLUE + "Give sender" + customBlock);

    }

}
