package me.aleiv.core.blocks;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import com.google.gson.JsonObject;

import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.event.player.PlayerInteractEvent;

import lombok.Getter;
import me.aleiv.core.blocks.objects.NoteBlockID;
import me.aleiv.core.blocks.utilities.JsonConfig;
import net.md_5.bungee.api.ChatColor;

public class NoteBlockManager {

    CoreBlocks instance;
    @Getter HashMap<String, NoteBlockID> noteblocks = new HashMap<>();

    public NoteBlockManager(CoreBlocks instance) {
        this.instance = instance;

        pullJsonCode();
    }

    public boolean isDefaultNoteBlock(Block block) {
        var noteBlock = getNoteBlockData(block);
        var thisID = getBlockID(noteBlock);
        var defaultID = getBlockIDbyData("harp", 0, false);
        return thisID == defaultID;
    }

    public boolean isNoteBlock(Block block){
        return block.getType() == Material.NOTE_BLOCK;
    }

    public BlockData getDefaultBlockData() {
        return getNoteBlockData("harp", 0, false);
    }

    public NoteBlock getNoteBlockData(String instrument, int note, boolean powered){
        return getNoteBlockData(getBlockIDbyData(instrument, note, powered));
    }

    public void setBlockData(Block block, NoteBlock noteBlock) {
        block.setBlockData(noteBlock);
    }

    public NoteBlock getNoteBlockData(Block block) {
        return (NoteBlock) block.getBlockData();
    }

    public NoteBlockID getNoteBlockID(String blockID) {
        return noteblocks.containsKey(blockID) ? noteblocks.get(blockID) : null;
    }

    public NoteBlock getNoteBlockData(String blockID) {
        var noteblockID = getNoteBlockID(blockID);

        if (noteblockID == null)
            return null;

        NoteBlock noteBlock = (NoteBlock) Material.NOTE_BLOCK.createBlockData();

        var octave = noteblockID.getOctave();
        var tone = noteblockID.getTone();
        var sharped = noteblockID.isSharped();

        var n = new Note(octave, tone, sharped);

        noteBlock.setNote(n);
        noteBlock.setInstrument(noteblockID.getInstrument());
        noteBlock.setPowered(noteblockID.isPowered());

        return noteBlock;

    }

    public NoteBlockID getNoteBlockID(NoteBlock noteBlock) {
        var nt = noteBlock.getAsString();

        var val1 = nt.replace("minecraft:note_block[", "");
        var val2 = val1.replace("]", "");
        var vals = val2.split(",");

        var instrumentF3 = vals[0].split("=")[1];

        var noteF3 = vals[1].split("=")[1];

        var instrument = noteBlock.getInstrument();
        var note = noteBlock.getNote();
        var tone = note.getTone();
        var octave = note.getOctave();
        var sharped = note.isSharped();
        var powered = noteBlock.isPowered();

        return new NoteBlockID(instrumentF3, Integer.parseInt(noteF3), powered, instrument, tone, octave, sharped);

    }

    public String getBlockID(Block block){
        return getBlockID(getNoteBlockData(block));
    }

    public String getBlockID(NoteBlock noteBlock) {
        var nt = getNoteBlockID(noteBlock);

        var format = new StringBuilder();
        format.append(nt.getInstrumentF3() + ";");
        format.append(nt.getNoteF3() + ";");
        format.append(nt.isPowered() + ";");
        format.append(nt.getInstrument().toString() + ";");
        format.append(nt.getTone().toString() + ";");
        format.append(nt.getOctave() + ";");
        format.append(nt.isSharped() + ";");

        return format.toString();

    }

    public String getBlockIDbyData(String instrument, int note, boolean powered) {
        return noteblocks.keySet().stream()
                .filter(noteblock -> noteblock.startsWith(instrument + ";" + note + ";" + powered)).findAny()
                .orElse("");
    }

    // @EventHandler
    public void addNoteBlocks(PlayerInteractEvent e) {
        var block = e.getClickedBlock();
        var player = e.getPlayer();

        if (block != null && block.getType() == Material.NOTE_BLOCK) {

            NoteBlock noteBlock = (NoteBlock) block.getBlockData();

            var blockID = getBlockID(noteBlock);

            var noteBlockID = getNoteBlockID(noteBlock);

            if (!noteblocks.containsKey(blockID)) {
                noteblocks.put(blockID, noteBlockID);
                player.sendMessage(ChatColor.YELLOW + "(" + noteblocks.size() + ") Added "
                        + noteBlockID.getInstrumentF3() + " " + noteBlockID.getNoteF3() + " " + noteBlock.isPowered());
                pushJson();

            }
        }
    }

    public void pushJson() {
        try {
            var gson = instance.getGson();
            var jsonConfig = new JsonConfig("noteblocks.json");
            var json = gson.toJson(noteblocks);
            var obj = gson.fromJson(json, JsonObject.class);
            jsonConfig.setJsonObject(obj);
            jsonConfig.save();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void pullJson() {
        try {
            var gson = instance.getGson();
            var jsonConfig = new JsonConfig("noteblocks.json");
            var list = jsonConfig.getJsonObject();
            var iter = list.entrySet().iterator();
            var map = noteblocks;

            while (iter.hasNext()) {
                var entry = iter.next();
                var key = entry.getKey();
                var value = entry.getValue();
                var obj = gson.fromJson(value, NoteBlockID.class);
                map.put(key, obj);

            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void pullJsonCode() {  
        try {
            var gson = instance.getGson();
            var jsonConfig = new String(CoreBlocks.getInstance().getResource("noteblocks.json").readAllBytes(), StandardCharsets.UTF_8);
            var list = gson.fromJson(jsonConfig, JsonObject.class);
            var iter = list.entrySet().iterator();
            var map = noteblocks;

            while (iter.hasNext()) {
                var entry = iter.next();
                var key = entry.getKey();
                var value = entry.getValue();
                var obj = gson.fromJson(value, NoteBlockID.class);

                map.put(key, obj);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}
