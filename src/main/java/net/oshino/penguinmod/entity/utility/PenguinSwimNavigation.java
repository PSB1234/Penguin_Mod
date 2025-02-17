package net.oshino.penguinmod.entity.utility;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.pathing.AmphibiousSwimNavigation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.oshino.penguinmod.entity.custom.PenguinEntity;

public class PenguinSwimNavigation extends AmphibiousSwimNavigation {
     public PenguinSwimNavigation(PenguinEntity penguin, World world) {
        super(penguin, world);
    }

    @Override
    public boolean isValidPosition(BlockPos pos) {
        if(this.entity instanceof PenguinEntity penguin && penguin.isTravelling() ){
            return this.world.getBlockState(pos).isOf(Blocks.WATER);
        }
        return !this.world.getBlockState(pos.down()).isAir();
    }
}
