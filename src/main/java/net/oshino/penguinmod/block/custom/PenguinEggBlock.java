package net.oshino.penguinmod.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.world.World;
import net.oshino.penguinmod.entity.ModEntities;
import net.oshino.penguinmod.entity.custom.PenguinEntity;

public class PenguinEggBlock extends Block {
    public static final IntProperty HATCH = IntProperty.of("hatch", 0, 2); // 0, 1, 2 - stages of hatching
    public static final IntProperty EGGS = IntProperty.of("eggs", 1, 2); // Stack up to 2 eggs

    public PenguinEggBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(HATCH, 0)
                .with(EGGS, 1));
    }
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HATCH, EGGS); // Register properties here
    }
    public int getHatchStage(BlockState state) {
        return state.get(HATCH);
    }
    private boolean isReadyToHatch(BlockState state) {
        return this.getHatchStage(state) == 2;
    }
    private void spawnPenguins(ServerWorld world, BlockPos pos, int eggCount) {
        for (int i = 0; i < eggCount; i++) {
            PenguinEntity penguinEntity = new PenguinEntity(ModEntities.PENGUIN, world);
            Vec3d vec3d = pos.toCenterPos().add(world.random.nextDouble() * 0.3 - 0.15, 0, world.random.nextDouble() * 0.3 - 0.15);
            penguinEntity.setBaby(true);
            penguinEntity.refreshPositionAndAngles(vec3d.getX(), vec3d.getY(), vec3d.getZ(), MathHelper.wrapDegrees(world.random.nextFloat() * 360.0F), 0.0F);
            world.spawnEntity(penguinEntity);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!this.isReadyToHatch(state)) {
            if (random.nextInt(3) == 0) { // 33% chance to progress hatching
                world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_CRACK, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
                world.setBlockState(pos, state.with(HATCH, this.getHatchStage(state) + 1), Block.NOTIFY_LISTENERS);
            }
        } else {
            world.playSound(null, pos, SoundEvents.ENTITY_TURTLE_EGG_HATCH, SoundCategory.BLOCKS, 0.7F, 0.9F + random.nextFloat() * 0.2F);
            world.breakBlock(pos, false);
            this.spawnPenguins(world, pos, state.get(EGGS)); // Modified to handle multiple eggs
        }
    }

    @Override
    public boolean canPathfindThrough(BlockState state, NavigationType type) {
        return false;
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient && entity instanceof LivingEntity && world.random.nextInt(10) == 0) {
            int count = state.get(EGGS);
            world.playSound(null, pos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 0.8F, 0.8F + world.random.nextFloat() * 0.2F);

            if (count > 1) {
                world.setBlockState(pos, state.with(EGGS, count - 1), 2);
            } else {
                world.breakBlock(pos, false);
            }
        }
        super.onSteppedOn(world, pos, state, entity);
    }

}
