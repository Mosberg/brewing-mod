package dk.mosberg.brewing.registry;

import dk.mosberg.brewing.Brewing;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Registers creative tabs (item groups) for the brewing mod.
 */
public class ModItemGroups {
    public static final RegistryKey<ItemGroup> BREWING_GROUP =
            RegistryKey.of(RegistryKeys.ITEM_GROUP, Identifier.of(Brewing.MOD_ID, "brewing"));

    /**
     * Registers all item groups.
     */
    public static void register() {
        Brewing.LOGGER.info("Registering item groups...");

        Registry.register(Registries.ITEM_GROUP, BREWING_GROUP,
                FabricItemGroup.builder()
                        .displayName(Text.translatable("itemGroup." + Brewing.MOD_ID + ".brewing"))
                        .icon(() -> new ItemStack(ModItems.getItem("glass_bottle")))
                        .entries((displayContext, entries) -> {
                            // Add all containers
                            ModItems.getAllItems().forEach((id, item) -> {
                                entries.add(item);
                            });

                            // Add all blocks as items
                            ModBlockItems.getAllBlockItems().forEach((id, item) -> {
                                entries.add(item);
                            });
                        }).build());

        Brewing.LOGGER.info("Registered item groups");
    }
}
