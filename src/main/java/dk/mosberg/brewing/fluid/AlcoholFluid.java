package dk.mosberg.brewing.fluid;

import dk.mosberg.brewing.registry.ModFluids;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

/** Simple alcohol fluid with still/flowing variants. */
public abstract class AlcoholFluid extends FlowableFluid {
    public FlowableFluid getStill() {
        return ModFluids.ALCOHOL_STILL;
    }

    public FlowableFluid getFlowing() {
        return ModFluids.ALCOHOL_FLOWING;
    }

    public Item getBucketItem() {
        return Items.BUCKET; // Placeholder bucket; custom bucket registered separately
    }

    public boolean isInfinite(ServerWorld world) {
        return false;
    }

    protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {}

    protected int getFlowSpeed(WorldView world) {
        return 4;
    }

    protected int getLevelDecreasePerBlock(WorldView world) {
        return 1;
    }

    public int getTickRate(WorldView world) {
        return 5;
    }

    protected float getBlastResistance() {
        return 100f;
    }

    protected BlockState toBlockState(FluidState state) {
        return ModFluids.ALCOHOL_BLOCK.getDefaultState().with(FluidBlock.LEVEL,
                getBlockStateLevel(state));
    }

    protected boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos,
            Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !state.isStill();
    }

    protected int getSpreadDelay(WorldView world, BlockPos pos, FluidState state,
            FluidState newState) {
        return getTickRate(world);
    }

    protected int getMaxFlowDistance(WorldView world) {
        return 4;
    }

    public static class Flowing extends AlcoholFluid {
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }

        public boolean isStill(FluidState state) {
            return false;
        }
    }

    public static class Still extends AlcoholFluid {
        public int getLevel(FluidState state) {
            return 8;
        }

        public boolean isStill(FluidState state) {
            return true;
        }
    }
}
