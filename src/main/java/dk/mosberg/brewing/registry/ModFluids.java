package dk.mosberg.brewing.registry;

import dk.mosberg.brewing.Brewing;
import dk.mosberg.brewing.fluid.AlcoholFluid;
import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.MapColor;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

/** Registers fluids for the brewing mod. */
public class ModFluids {
    public static FlowableFluid ALCOHOL_STILL;
    public static FlowableFluid ALCOHOL_FLOWING;
    public static Block ALCOHOL_BLOCK;

    public static void register() {
        Brewing.LOGGER.info("Registering fluids...");

        ALCOHOL_STILL = Registry.register(Registries.FLUID,
                Identifier.of(Brewing.MOD_ID, "alcohol"), new AlcoholFluid.Still());
        ALCOHOL_FLOWING = Registry.register(Registries.FLUID,
                Identifier.of(Brewing.MOD_ID, "flowing_alcohol"), new AlcoholFluid.Flowing());

        ALCOHOL_BLOCK =
                Registry.register(Registries.BLOCK, Identifier.of(Brewing.MOD_ID, "alcohol_block"),
                        new FluidBlock(ALCOHOL_STILL,
                                net.minecraft.block.AbstractBlock.Settings.create()
                                        .registryKey(RegistryKey.of(RegistryKeys.BLOCK,
                                                Identifier.of(Brewing.MOD_ID, "alcohol_block")))
                                        .mapColor(MapColor.WATER_BLUE).replaceable().noCollision()
                                        .strength(100.0F).dropsNothing()) {});

        Brewing.LOGGER.info("Registered fluids and fluid block");
    }
}
