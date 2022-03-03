package me.aleiv.core.paper.commands;

import com.google.common.collect.ImmutableList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import lombok.NonNull;
import me.aleiv.core.paper.Core;
import me.aleiv.core.paper.objects.CustomBlock;
import net.md_5.bungee.api.ChatColor;

@CommandAlias("customblocks")
@CommandPermission("customblocks.cmd")
public class CustomBlocksCMD extends BaseCommand {

    private @NonNull Core instance;

    public CustomBlocksCMD(Core instance) {
        this.instance = instance;

        var manager = instance.getCommandManager();
        var customBlocksManager = instance.getCustomBlocksManager();

        manager.getCommandCompletions().registerAsyncCompletion("bool", c -> {
            return ImmutableList.of("true", "false");
        });

        manager.getCommandCompletions().registerAsyncCompletion("instrument", c -> {
            return ImmutableList.of("harp", "banjo", "basedrum", "bass", "bell",
                    "bit", "chime", "cow_bell", "didgeridoo", "flute", "guitar", "hat",
                    "iron_xylophone", "pling", "snare", "xylophone");
        });

        manager.getCommandCompletions().registerAsyncCompletion("customblocks", c -> {
            return customBlocksManager.getCustomBlocks().values().stream().map(cb -> cb.getName()).toList();
        });

        manager.getCommandCompletions().registerStaticCompletion("num", "0");
        manager.getCommandCompletions().registerStaticCompletion("name", "name");

    }

    @Subcommand("give")
    public void give(Player sender, String customBlockName){

        var inv = sender.getInventory();
        var customBlocksManager =  instance.getCustomBlocksManager();
        var customBlocks = customBlocksManager.getCustomBlocks();
        if(customBlocks.containsKey(customBlockName)){
            
            var customBlock = customBlocks.get(customBlockName);
            inv.addItem(customBlock.getItemStack());
            sender.sendMessage(ChatColor.YELLOW + "Given sender" + customBlock + ".");

        }else{
            sender.sendMessage(ChatColor.RED + "Custom block " + customBlockName + " doesn't exist.");
        }
        
        
    }

    @Subcommand("add")
    @CommandCompletion("@bool @instrument @num @num @name")
    public void add(Player sender, boolean powered, String instrument, int note, int customModelData,
            String customBlockName) {

        var customBlocksManager = instance.getCustomBlocksManager();
        var customBlocks = customBlocksManager.getCustomBlocks();

        if (customBlocks.containsKey(customBlockName)) {
            // TODO: ADD OTHER THINGS BLOCKED BY EXIST
            sender.sendMessage(ChatColor.RED + "Can't add custom block " + customBlockName + ".");
        } else {

            var noteBlockManager = instance.getNoteBlockManager();
            var blockID = noteBlockManager.getBlockIDbyData(instrument, note, powered);
            var customBlock = new CustomBlock(customBlockName, blockID, customModelData);

            customBlocks.put(customBlockName, customBlock);
            sender.sendMessage(ChatColor.YELLOW + "Added new custom block " + customBlockName + ".");
        }

    }

    @Subcommand("remove")
    @CommandCompletion("@customblocks")
    public void remove(Player sender, String customBlockName) {
        var customBlocksManager = instance.getCustomBlocksManager();
        var customBlocks = customBlocksManager.getCustomBlocks();

        if (customBlocks.containsKey(customBlockName)) {
            customBlocks.remove(customBlockName);
            sender.sendMessage(ChatColor.YELLOW + "Removed custom block " + customBlockName + ".");
        } else {

            sender.sendMessage(ChatColor.RED + "Custom block " + customBlockName + " doesn't exist.");
        }

    }

    @Subcommand("list")
    public void list(CommandSender sender) {
        var customBlocksManager = instance.getCustomBlocksManager();
        var customBlocks = customBlocksManager.getCustomBlocks();

        sender.sendMessage(ChatColor.YELLOW + "Custom block list: " + ChatColor.WHITE + customBlocks.keySet().toString() + ".");
    }

}
