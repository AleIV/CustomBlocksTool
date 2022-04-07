package me.aleiv.core.blocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import me.aleiv.core.blocks.commands.CustomBlocksCMD;
import me.aleiv.core.blocks.listeners.BlockerListener;
import me.aleiv.core.blocks.listeners.NoteBlockListener;

@SpigotPlugin
public class CoreBlocks extends JavaPlugin {

    private static CoreBlocks instance;
    private @Getter PaperCommandManager commandManager;
    //private @Getter static MiniMessage miniMessage = MiniMessage.get();
    private @Getter CustomBlocksManager customBlocksManager;
    private @Getter NoteBlockManager noteBlockManager;
    private @Getter Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onEnable() {
        instance = this;

        //MANAGER

        this.customBlocksManager = new CustomBlocksManager(this);
        this.noteBlockManager = new NoteBlockManager(this);
        
        //LISTENERS

        Bukkit.getPluginManager().registerEvents(new BlockerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new NoteBlockListener(this), this);

        //COMMANDS
        
        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new CustomBlocksCMD(this));

        //TODO: Events to listen from API the custom blocks

    }

    @Override
    public void onDisable() {

    }

    public static CoreBlocks getInstance() {
        return instance;
    }

}