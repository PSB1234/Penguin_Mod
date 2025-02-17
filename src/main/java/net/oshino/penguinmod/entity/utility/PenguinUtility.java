package net.oshino.penguinmod.entity.utility;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;


public class PenguinUtility {
    // if the block is solid land
    private static boolean isSolidLand(BlockState blockState, World world, BlockPos checkPos) {
        return blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isOf(Blocks.DIRT) ||
                blockState.isOf(Blocks.SAND) || blockState.isSolidBlock(world, checkPos) ;
    }

//break the ice using his forehead
public static void breakIceAbove(TameableEntity PenguinEntity) {
    BlockPos posAbove = PenguinEntity.getBlockPos().up();
    BlockState stateAbove = PenguinEntity.getWorld().getBlockState(posAbove);

    if (stateAbove.getBlock() == Blocks.ICE || stateAbove.getBlock() == Blocks.FROSTED_ICE) {
        PenguinEntity.getWorld().breakBlock(posAbove,true); // True = Drops ice shards
        PenguinEntity.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F);}
}


    //Find the nearest Land
    public static BlockPos findNearestLand(Entity entity, int radius) {
        World world = entity.getWorld();
        BlockPos.Mutable checkPos = new BlockPos.Mutable();

        BlockPos entityPos = entity.getBlockPos();
        int waterLevel = entityPos.getY(); // Start checking at the entity's current water level

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                // Scan from the current water level upwards to find land
                for (int y = waterLevel; y <= waterLevel + 20; y++) {
                    checkPos.set(entityPos.getX() + x, y, entityPos.getZ() + z);
                    BlockState blockState = world.getBlockState(checkPos);

                    // Check if it's solid land and there's air above it
                    if (isSolidLand(blockState,world,checkPos) && world.isAir(checkPos.up())) {
                        return checkPos.toImmutable();
                    }
                }
            }
        }

        return null; // No land found
    }

}
