package net.oshino.penguinmod.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;
import net.oshino.penguinmod.entity.ModEntities;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
/**
 * Custom block representing a penguin egg.
 */
public class PenguinEggBlock extends Block {
    public static MapCodec<PenguinEggBlock> CODEC = createCodec(PenguinEggBlock::new);
    public static final IntProperty HATCH = IntProperty.of("hatch", 0, 2); // 0, 2 - stages of hatching

    /**
     * Constructor for PenguinEggBlock.
     *
     * @param settings The block settings.
     */
    public PenguinEggBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(HATCH, 0));

    }
    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC; // Register properties here
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HATCH);
    }
    /**
     * Gets the current hatch stage of the egg.
     *
     * @param state The block state.
     * @return The hatch stage.
     */
    public int getHatchStage(BlockState state) {
        return state.get(HATCH);
    }
    /**
     * Checks if the egg is ready to hatch.
     *
     * @param state The block state.
     * @return True if the egg is ready to hatch, false otherwise.
     */
    private boolean isReadyToHatch(BlockState state) {
        return this.getHatchStage(state) == 2;
    }
    /**
     * Spawns a baby penguin at the given position.
     *
     * @param world The server world.
     * @param pos   The block position.
     */
    private void spawnPenguin(ServerWorld world, BlockPos pos) {
            PenguinEntity penguinEntity = new PenguinEntity(ModEntities.PENGUIN, world);
            Vec3d vec3d = pos.toCenterPos().add(world.random.nextDouble() * 0.3 - 0.15, 0, world.random.nextDouble() * 0.3 - 0.15);
            penguinEntity.setBaby(true);
            penguinEntity.refreshPositionAndAngles(vec3d.getX(), vec3d.getY(), vec3d.getZ(), MathHelper.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
            world.spawnEntity(penguinEntity);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!this.isReadyToHatch(state)) {
                world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                world.setBlockState(pos, state.with(HATCH, this.getHatchStage(state) + 1), Block.NOTIFY_LISTENERS);
            world.scheduleBlockTick(pos, this, 100);
        } else {
            world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
            world.breakBlock(pos, false);
            this.spawnPenguin(world, pos);
        }

    }
    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient()) {
            world.syncWorldEvent(WorldEvents.TURTLE_EGG_PLACED, pos, 0);
        }

        int i = 24000;
        int j = i / 3;
        world.emitGameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Emitter.of(state));
        world.scheduleBlockTick(pos, this, j + world.random.nextInt(300));
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient && entity instanceof LivingEntity && !(entity instanceof PenguinEntity )&& world.random.nextInt(10) == 0) {
            world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.2F);
            world.breakBlock(pos, false);
        }
        super.onSteppedOn(world, pos, state, entity);
    }
    private static final VoxelShape SMALL_CUBOID_SHAPE = Block.createCuboidShape(
            4.0D, 0.0D, 4.0D, 12.0D, 9.0D, 12.0D); // Adjust size here

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SMALL_CUBOID_SHAPE;
    }
}


