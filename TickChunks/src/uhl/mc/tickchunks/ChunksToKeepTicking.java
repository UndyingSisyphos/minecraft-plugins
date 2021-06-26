package uhl.mc.tickchunks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Manages the collection of chunk coordinates that are to be kept ticking. Such an object can be constructed from a
 * {@link YamlConfiguration} object if such object was produced by an instance of this class.
 */
public class ChunksToKeepTicking {
    private final Map<String, Set<ChunkCoordinate>> coordinatesOfChunksToKeepTickingPerWorldName;
    private static final String DATA_KEY = "world-dimensions";
    private static final String X_KEY = "x";
    private static final String Z_KEY = "z";

    public ChunksToKeepTicking() {
        coordinatesOfChunksToKeepTickingPerWorldName = Collections.synchronizedMap(new HashMap<>());
    }

    void addChunk(String worldName, int x, int z) {
        Set<ChunkCoordinate> set = coordinatesOfChunksToKeepTickingPerWorldName.get(worldName);
        if (set == null) {
            set = new HashSet<>();
            coordinatesOfChunksToKeepTickingPerWorldName.put(worldName, set);
        }
        set.add(new ChunkCoordinate(x, z));
    }

    void removeChunk(String worldName, int x, int z) {
        Set<ChunkCoordinate> set = coordinatesOfChunksToKeepTickingPerWorldName.get(worldName);
        if (set != null) {
            set.remove(new ChunkCoordinate(x, z));
            if (set.isEmpty()) {
                coordinatesOfChunksToKeepTickingPerWorldName.remove(worldName);
            }
        }
    }

    void load(YamlConfiguration data) {
        final ConfigurationSection coordinates = data.getConfigurationSection(DATA_KEY);
        for (final String worldName : coordinates.getKeys(/* deep */ false)) {
            final Set<ChunkCoordinate> chunkCoordinatePairs = new HashSet<>();
            for (final Map<?, ?> coordinate : coordinates.getMapList(worldName)) {
                final ChunkCoordinate c = new ChunkCoordinate(((Number) coordinate.get(X_KEY)).intValue(),
                        ((Number) coordinate.get(Z_KEY)).intValue());
                chunkCoordinatePairs.add(c);
            }
            coordinatesOfChunksToKeepTickingPerWorldName.put(worldName, chunkCoordinatePairs);
        }
    }

    YamlConfiguration getData() {
        final YamlConfiguration result = new YamlConfiguration();
        final Map<String, List<Map<String, Integer>>> coordinatesAsMap = new HashMap<>();
        synchronized (coordinatesOfChunksToKeepTickingPerWorldName) {
            for (final Entry<String, Set<ChunkCoordinate>> c : coordinatesOfChunksToKeepTickingPerWorldName.entrySet()) {
                final List<Map<String, Integer>> coordinatesList = new ArrayList<>();
                for (final ChunkCoordinate coord : c.getValue()) {
                    final Map<String, Integer> mapForCoordinate = new HashMap<>();
                    mapForCoordinate.put(X_KEY, coord.getX());
                    mapForCoordinate.put(Z_KEY, coord.getZ());
                    coordinatesList.add(mapForCoordinate);
                }
                coordinatesAsMap.put(c.getKey(), coordinatesList);
            }
        }
        result.createSection(DATA_KEY, coordinatesAsMap);
        return result;
    }

    public Iterable<Map.Entry<String, Set<ChunkCoordinate>>> getTickingChunksPerWorldName() {
        return Collections.unmodifiableMap(coordinatesOfChunksToKeepTickingPerWorldName).entrySet();
    }
}
