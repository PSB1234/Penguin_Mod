package net.oshino.penguinmod.entity.custom;

import net.minecraft.entity.AnimationState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.oshino.penguinmod.entity.ModEntities;
import org.jetbrains.annotations.Nullable;

public class PenguinEntities extends AnimalEntity {
    public final AnimationState idleState = new AnimationState();
    private int idleTimeOut = 0;


    public PenguinEntities(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
            this.goalSelector.add(0,new SwimGoal(this));
            this.goalSelector.add(0, new PowderSnowJumpGoal(this, this.getWorld()));
            this.goalSelector.add(1,new AnimalMateGoal(this,1.00D));
            this.goalSelector.add(2,new TemptGoal(this,1.23D,
                    Ingredient.ofItems(Items.COD)
                    ,false));
            this.goalSelector.add(3,new FollowParentGoal(this,1.1D));
            this.goalSelector.add(4,new WanderAroundGoal(this,1.8D));
            this.goalSelector.add(5,new LookAtEntityGoal(this, PlayerEntity.class,4.3F));
            this.goalSelector.add(6,new LookAroundGoal(this));
    }


    public static DefaultAttributeContainer.Builder createAttributes(){
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,10)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.25)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE,10);

    }


    private void setupAnimationStates() {
        if (this.idleTimeOut <= 0) {
            this.idleTimeOut = 40;
            this.idleState.start(this.age);
        } else {
            --this.idleTimeOut;
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(Items.COD);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.PENGUIN.create(world);
    }
}
