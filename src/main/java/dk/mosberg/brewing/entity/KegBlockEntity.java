package dk.mosberg.brewing.entity;

import dk.mosberg.brewing.registry.ModBlockEntities;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;

/** Block entity for keg blocks. */
public class KegBlockEntity extends BlockEntity implements Iterable<StorageView<FluidVariant>> {
    private static final long CAN_VOLUME = FluidConstants.BUCKET / 4; // 1 can
    private static final long CAPACITY = CAN_VOLUME * 16; // 16 cans

    private final SingleVariantStorage<FluidVariant> tank = new SingleVariantStorage<>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return CAPACITY;
        }

        @Override
        protected void onFinalCommit() {
            markDirty();
            if (world != null)
                world.updateListeners(getPos(), getCachedState(), getCachedState(), 3);
        }
    };

    public KegBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KEG_BLOCK_ENTITY, pos, state);
    }

    public SingleVariantStorage<FluidVariant> getTank() {
        return tank;
    }

    @Override
    public Iterator<StorageView<FluidVariant>> iterator() {
        return tank.iterator();
    }

    /** Registers sided fluid storage for this block entity. */
    public static void registerStorage() {
        FluidStorage.SIDED.registerForBlockEntity((be, direction) -> be.getTank(),
                ModBlockEntities.KEG_BLOCK_ENTITY);
    }
}
