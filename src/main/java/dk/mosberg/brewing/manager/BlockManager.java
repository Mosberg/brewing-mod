package dk.mosberg.brewing.manager;

import java.util.Collection;
import java.util.Optional;
import dk.mosberg.brewing.data.BlockData;
import dk.mosberg.brewing.data.DataLoader;

/**
 * Manages blocks in the brewing mod.
 */

public class BlockManager {
    // TODO: BlockManager class implementation goes here

    private static BlockManager INSTANCE;

    public static BlockManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BlockManager();
        }
        return INSTANCE;
    }

    /**
     * Gets all loaded blocks from the content pack.
     */
    public Collection<BlockData> getAllBlocks() {
        return DataLoader.getContentPack().getBlocks().values();
    }

    /**
     * Gets a block by its ID.
     */
    public Optional<BlockData> getBlock(String id) {
        return Optional.ofNullable(DataLoader.getContentPack().getBlock(id));
    }

    /**
     * Checks if a block exists.
     */
    public boolean hasBlock(String id) {
        return getBlock(id).isPresent();
    }
}
