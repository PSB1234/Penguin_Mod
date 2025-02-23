package net.oshino.penguinmod.entity.utility;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.pathing.AmphibiousSwimNavigation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.oshino.penguinmod.entity.custom.PenguinEntity;
/**
 * Custom navigation system for the {@link PenguinEntity}, extending {@link AmphibiousSwimNavigation}.
 * This navigation allows penguins to move efficiently both in water and on land.
 */
public class PenguinSwimNavigation extends AmphibiousSwimNavigation {
    /**
     * Constructs the navigation system for the penguin.
     *
     * @param penguin The penguin entity using this navigation.
     * @param world   The world where the penguin exists.
     */
     public PenguinSwimNavigation(PenguinEntity penguin, World world) {
        super(penguin, world);
    }
    /**
     * Determines whether a position is a valid navigation target for the penguin.
     * - If the penguin is in "travelling" mode, it can only move through water.
     * - Otherwise, it avoids air blocks below it to ensure stable movement.
     *
     * @param pos The position to check.
     * @return {@code true} if the position is valid for movement, {@code false} otherwise.
     */
    @Override
    public boolean isValidPosition(BlockPos pos) {
        if(this.entity instanceof PenguinEntity penguin && penguin.isTravelling() ){
            // When travelling, penguins can only navigate through water blocks.
            return this.world.getBlockState(pos).isOf(Blocks.WATER);
        }
        // On land, the penguin ensures there's a solid block beneath it to prevent floating.
        return !this.world.getBlockState(pos.down()).isAir();
    }
}
