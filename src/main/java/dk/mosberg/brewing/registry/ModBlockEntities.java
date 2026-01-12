package dk.mosberg.brewing.registry;

import dk.mosberg.brewing.Brewing;
import dk.mosberg.brewing.entity.KegBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

/** Registers block entities for the brewing mod. */
public class ModBlockEntities {
    public static BlockEntityType<KegBlockEntity> KEG_BLOCK_ENTITY;

    public static void register() {
        Brewing.LOGGER.info("Registering block entities...");

        KEG_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
                Identifier.of(Brewing.MOD_ID, "keg"), FabricBlockEntityTypeBuilder
                        .create(KegBlockEntity::new, ModBlocks.KEG_BLOCK).build());

        // Register fluid storage hookup
        KegBlockEntity.registerStorage();

        Brewing.LOGGER.info("Registered block entities");
    }

    public static KegBlockEntity createKegEntity(net.minecraft.util.math.BlockPos pos,
            net.minecraft.block.BlockState state) {
        return new KegBlockEntity(pos, state);
    }
}
