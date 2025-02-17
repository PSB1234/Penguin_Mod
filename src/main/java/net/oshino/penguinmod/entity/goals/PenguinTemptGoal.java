package net.oshino.penguinmod.entity.goals;

import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.item.ItemStack;
import net.oshino.penguinmod.entity.custom.PenguinEntity;

import java.util.function.Predicate;

public class PenguinTemptGoal extends TemptGoal {
    private final PenguinEntity penguin ;
     public PenguinTemptGoal(PenguinEntity penguin, double speed, Predicate<ItemStack> foodPredicate, boolean canBeScared) {
        super(penguin, speed, foodPredicate, canBeScared);
        this.penguin = penguin;

    }
    @Override
    public boolean canStart() {
        return super.canStart() && this.penguin.isSwimming();
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue()&& this.penguin.isSubmergedInWater();
    }


}
