package net.oshino.penguinmod.entity.utility;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
/**
 * A utility class that provides helper methods for penguin behavior in the Penguin Mod.
 * This includes interactions with terrain, breaking ice, and finding nearby land.
 */
public class PenguinUtility {
    /**
     * Checks if a given block is solid land where a penguin can stand.
     * Land includes grass, dirt, sand, or any other solid block.
     *
     * @param blockState The block state to check.
     * @param world The world where the block exists.
     * @param checkPos The position of the block being checked.
     * @return {@code true} if the block is solid land, otherwise {@code false}.
     */
    private static boolean isSolidLand(BlockState blockState, World world, BlockPos checkPos) {
        return blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isOf(Blocks.DIRT) ||
                blockState.isOf(Blocks.SAND) || blockState.isSolidBlock(world, checkPos) ;
    }
    /**
     * Allows a penguin to break ice blocks above its head using its forehead.
     * If the block above the penguin is ice or frosted ice, it will break and drop ice shards.
     *
     * @param PenguinEntity The penguin entity performing the action.
     */
public static void breakIceAbove(PenguinEntity PenguinEntity) {
    BlockPos posAbove = PenguinEntity.getBlockPos().up();
    BlockState stateAbove = PenguinEntity.getWorld().getBlockState(posAbove);

    if (stateAbove.getBlock() == Blocks.ICE || stateAbove.getBlock() == Blocks.FROSTED_ICE) {
        PenguinEntity.getWorld().breakBlock(posAbove,true); // True = Drops ice shards
        PenguinEntity.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F);}
}

    /**
     * Finds the nearest land position within a given radius around an entity.
     * The search starts at the entity's water level and scans upward to locate solid ground with air above it.
     *
     * @param entity The entity searching for land.
     * @param radius The search radius around the entity.
     * @return The {@link BlockPos} of the nearest land found, or {@code null} if no land is found.
     */
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
