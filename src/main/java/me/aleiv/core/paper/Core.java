package me.aleiv.core.paper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import co.aikar.commands.PaperCommandManager;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import me.aleiv.core.paper.commands.CustomBlocksCMD;
import me.aleiv.core.paper.listeners.CustomBlocksListener;
import net.kyori.adventure.text.minimessage.MiniMessage;

@SpigotPlugin
public class Core extends JavaPlugin {

    private static @Getter Core instance;
    private @Getter PaperCommandManager commandManager;
    private @Getter static MiniMessage miniMessage = MiniMessage.get();
    private @Getter CustomBlocksManager customBlocksManager;
    private @Getter NoteBlockManager noteBlockManager;
    private @Getter Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public void onEnable() {
        instance = this;

        //LISTENERS

        Bukkit.getPluginManager().registerEvents(new CustomBlocksListener(this), this);

        //COMMANDS
        
        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new CustomBlocksCMD(this));

        //MANAGER

        this.customBlocksManager = new CustomBlocksManager(this);
        this.noteBlockManager = new NoteBlockManager(this);

        //TODO: Events to listen from API the custom blocks

    }

    @Override
    public void onDisable() {

    }

}