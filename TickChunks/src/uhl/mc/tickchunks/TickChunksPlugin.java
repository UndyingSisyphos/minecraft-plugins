package uhl.mc.tickchunks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TickChunksPlugin extends JavaPlugin {
    private ChunksToKeepTicking chunksToKeepTicking;
    private boolean debug;

    public TickChunksPlugin() {
        chunksToKeepTicking = new ChunksToKeepTicking();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final boolean result;
        if (args.length == 1) {
            if (sender instanceof Player) {
                final Player player = (Player) sender;
                final org.bukkit.World world = player.getWorld();
                final Chunk chunk = player.getLocation().getChunk();
                if (args[0].toLowerCase().startsWith("k")) {
                    result = checkPermission(player, "tickchunks.keep");
                    if (result) {
                        if (chunk.isSticky()) {
                            sender.sendMessage("Chunk (" + chunk.getX() + "," + chunk.getZ() + ") is already being kept in PlayerChunkMap.");
                        } else {
                            keepChunkTicking(world.getName(), chunk);
                            chunk.setSticky(true);
                            sender.sendMessage("Chunk (" + chunk.getX() + "," + chunk.getZ()
                                    + ") is now being kept in PlayerChunkMap even when no player has it in sight.");
                            final Plugin keepChunksPlugin = Bukkit.getServer().getPluginManager()
                                    .getPlugin("KeepChunks");
                            if (keepChunksPlugin != null) {
                                if (debug) {
                                    getLogger()
                                            .info("Found KeepChunks plugin. Requesting chunk [" +
                                                    chunk.getX() + "," + chunk.getZ() + "] to keep loaded");
                                }
                                String commandLine = "kc keepchunk coords " + chunk.getX() + " " + chunk.getZ() + " " + world.getName();
                                final boolean keepChunksResult = Bukkit.getServer().dispatchCommand(sender, commandLine);
                                if (debug) {
                                    getLogger().info(
                                            "KeepChunks command \"" + commandLine + "\" returned " + keepChunksResult);
                                }
                            }
                        }
                    }
                } else if (args[0].toLowerCase().startsWith("r")) {
                    result = checkPermission(player,  "tickchunks.release");
                    if (result) {
                        if (!chunk.isSticky()) {
                            sender.sendMessage("Chunk (" + chunk.getX() + "," + chunk.getZ() + ") was not being kept in PlayerChunkMap.");
                        } else {
                            releaseChunk(world.getName(), chunk);
                            sender.sendMessage("Chunk (" + chunk.getX() + "," + chunk.getZ()
                                    + ") is now being released from PlayerChunkMap when no player has it in sight.");
                            if (Bukkit.getServer().getPluginManager().getPlugin("KeepChunks") != null) {
                                sender.sendMessage(
                                        "Consider releasing the chunk also from the KeepChunks plugin using command /kc rc coords "
                                                + chunk.getX() + " " + chunk.getZ() + " " + world.getName());
                            }
                        }
                    }
                } else {
                    sender.sendMessage("Sub-Command " + args[0] + " not understood.");
                    result = false;
                }
            } else {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }

    private boolean checkPermission(final Player player, String permission) {
        boolean result = player.hasPermission(permission);
        if (!result) {
            player.sendMessage(ChatColor.RED
                    + "I'm sorry, but you need permission "+permission+" to perform this command. Please contact the server administrators if you believe that this is in error.");
        }
        return result;
    }

    private void keepChunkTicking(String worldName, Chunk chunk) {
        chunk.setSticky(true);
        chunksToKeepTicking.addChunk(worldName, chunk.getX(), chunk.getZ());
        updateDataFile();
    }

    private void releaseChunk(String worldName, Chunk chunk) {
        chunk.setSticky(false);
        chunksToKeepTicking.removeChunk(worldName, chunk.getX(), chunk.getZ());
        updateDataFile();
    }

    private void updateDataFile() {
        try {
            final FileWriter dataFileWriter = new FileWriter(getDataFile());
            dataFileWriter.write(chunksToKeepTicking.getData().saveToString());
            dataFileWriter.close();
        } catch (IOException e) {
            getLogger().severe("Couldn't write to data file " + getDataFile());
        }
    }

    @Override
    public void onEnable() {
        if (debug) {
            getLogger().info("onEnable");
        }
        for (final Entry<String, Set<ChunkCoordinate>> chunkCoordinatesForWorldDimension : chunksToKeepTicking.getTickingChunksPerWorldName()) {
            final World worldServer = Bukkit.getServer().getWorld(chunkCoordinatesForWorldDimension.getKey().toString());
            if (worldServer != null) {
                for (final ChunkCoordinate coord : chunkCoordinatesForWorldDimension.getValue()) {
                    if (debug) {
                        getLogger().info("keeping chunk " + coord + " in world '"+worldServer.getName()+"' ticking");
                    }
                    worldServer.getChunkAt(coord.getX(), coord.getZ()).setSticky(true);
                }
            } else {
                getLogger().warning("Couldn't keep chunks for world "+chunkCoordinatesForWorldDimension.getKey().toString()+
                        " ticking because world was not found");
            }
        }
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
        debug = getConfig().getBoolean("general.debug");
        if (debug) {
            getLogger().info("loading data...");
        }
        final File dataFile = getDataFile();
        try {
            if (dataFile.exists()) {
                final YamlConfiguration data = YamlConfiguration.loadConfiguration(new FileReader(dataFile));
                chunksToKeepTicking.load(data);
            }
        } catch (FileNotFoundException e) {
            getLogger().warning(
                    "The data file " + dataFile + " just existed but still cannot be found. Not loading any data.");
        }
    }

    private File getDataFile() {
        return new File(getDataFolder(), "data.yml");
    }
}
