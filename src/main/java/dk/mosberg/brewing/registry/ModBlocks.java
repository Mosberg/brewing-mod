package dk.mosberg.brewing.registry;

import java.util.HashMap;
import java.util.Map;
import dk.mosberg.brewing.Brewing;
import dk.mosberg.brewing.data.EquipmentData;
import dk.mosberg.brewing.manager.EquipmentManager;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

/**
 * Dynamically registers blocks for the brewing mod based on loaded equipment data.
 */
public class ModBlocks {
    private static final Map<String, Block> BLOCKS = new HashMap<>();
    public static Block KEG_BLOCK;

    /**
     * Registers all blocks dynamically from equipment data.
     */
    public static void register() {
        Brewing.LOGGER.info("Registering blocks...");

        // Static keg block (fluid-capable)
        registerKeg();

        // Register equipment as blocks
        for (EquipmentData equipment : EquipmentManager.getInstance().getAllEquipment()) {
            registerEquipmentBlock(equipment);
        }

        Brewing.LOGGER.info("Registered {} blocks", BLOCKS.size());
    }

    /**
     * Registers an equipment block.
     */
    private static void registerEquipmentBlock(EquipmentData data) {
        String id = data.id();

        AbstractBlock.Settings settings = AbstractBlock.Settings.create()
                .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Brewing.MOD_ID, id)))
                .mapColor(MapColor.IRON_GRAY).strength(3.5f).sounds(BlockSoundGroup.METAL)
                .requiresTool();

        Block block = new Block(settings);
        Registry.register(Registries.BLOCK, Identifier.of(Brewing.MOD_ID, id), block);
        BLOCKS.put(id, block);
    }

    private static void registerKeg() {
        String id = "keg";
        AbstractBlock.Settings settings = AbstractBlock.Settings.create()
                .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(Brewing.MOD_ID, id)))
                .mapColor(MapColor.IRON_GRAY).strength(3.0f).sounds(BlockSoundGroup.METAL)
                .nonOpaque();

        KEG_BLOCK = Registry.register(Registries.BLOCK, Identifier.of(Brewing.MOD_ID, id),
                new dk.mosberg.brewing.block.KegBlock(settings));
        BLOCKS.put(id, KEG_BLOCK);
    }

    /**
     * Gets a registered block by ID.
     */
    public static Block getBlock(String id) {
        return BLOCKS.get(id);
    }

    /**
     * Gets all registered blocks.
     */
    public static Map<String, Block> getAllBlocks() {
        return Map.copyOf(BLOCKS);
    }
}
