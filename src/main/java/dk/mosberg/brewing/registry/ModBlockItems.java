package dk.mosberg.brewing.registry;

import java.util.HashMap;
import java.util.Map;
import dk.mosberg.brewing.Brewing;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/**
 * Registers block items for all blocks in the mod.
 */
public class ModBlockItems {
    private static final Map<String, BlockItem> BLOCK_ITEMS = new HashMap<>();

    /**
     * Registers block items for all blocks.
     */
    public static void register() {
        Brewing.LOGGER.info("Registering block items...");

        for (Map.Entry<String, Block> entry : ModBlocks.getAllBlocks().entrySet()) {
            registerBlockItem(entry.getKey(), entry.getValue());
        }

        Brewing.LOGGER.info("Registered {} block items", BLOCK_ITEMS.size());
    }

    /**
     * Registers a block item for a block.
     */
    private static void registerBlockItem(String id, Block block) {
        Item.Settings settings = new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(Brewing.MOD_ID, id)))
                .useBlockPrefixedTranslationKey();

        BlockItem blockItem = new BlockItem(block, settings);
        Registry.register(Registries.ITEM, Identifier.of(Brewing.MOD_ID, id), blockItem);
        BLOCK_ITEMS.put(id, blockItem);
    }

    /**
     * Gets a registered block item by ID.
     */
    public static BlockItem getBlockItem(String id) {
        return BLOCK_ITEMS.get(id);
    }

    /**
     * Gets all registered block items.
     */
    public static Map<String, BlockItem> getAllBlockItems() {
        return Map.copyOf(BLOCK_ITEMS);
    }
}

