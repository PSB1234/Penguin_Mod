package net.oshino.penguinmod.entity.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.oshino.penguinmod.entity.ModEntities;
import net.oshino.penguinmod.entity.goals.*;
import net.oshino.penguinmod.entity.utility.PenguinMoveControl;
import net.oshino.penguinmod.entity.utility.PenguinSwimNavigation;
import net.oshino.penguinmod.entity.utility.PenguinUtility;
import net.oshino.penguinmod.sound.ModSounds;
import net.oshino.penguinmod.util.ModTags;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import java.util.function.Predicate;
import static net.oshino.penguinmod.entity.utility.PenguinUtility.*;


public class PenguinEntity extends AnimalEntity {
    // ========= Tracked Data =========
    // Penguin Sliding State
    private static final TrackedData<Boolean> SLIDING = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    //walking speed of the penguin
    private static final float WALKING_SPEED = 0.3F;
    //Sliding Ice Cooldown
    private static final TrackedData<Integer> SLIDING_COOLDOWN = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.INTEGER);
    //Penguin is hit by player
    private static final TrackedData<Vector3f> HIT_BY_PLAYER_DIRECTION = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.VECTOR3F);
    private static final TrackedData<Boolean> HIT_BY_PLAYER = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    // Penguin Hunger Level
    private int hungerLevel = 100;
    // Penguin Swimming State
    private boolean isSwimming = false;
    // Animation State for the penguin
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState swimIdleAnimationState = new AnimationState();
    //aimation timeouts the penguin
    private int idleAnimationTimeOut = 0;
    private int swimIdleAnimationTimeOut = 0;
    //TravelPos is random position for the penguin to travel to in water
    private static final TrackedData<BlockPos> TRAVEL_POS = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    //Penguin is travelling in water or not (Boolean)
    private static  final TrackedData<Boolean> TRAVELLING = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    // Penguin is in water mode or Land mode (Boolean)
    private static final TrackedData<Boolean> Land_Bound = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    //Penguin has Egg or not (Boolean)
    private static final TrackedData<Boolean> HAS_EGG = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    /** Predicate defining which entities are considered food by the penguin */
    private static final Predicate<LivingEntity> Penguin_Food = entity -> {
        EntityType<?> entityType = entity.getType();
        return entityType == EntityType.COD || entityType == EntityType.SALMON || entityType == EntityType.SQUID||
                entityType == EntityType.GLOW_SQUID || entityType == EntityType.TROPICAL_FISH;
    };
    /**
     * Checks if there is ice above the penguin.
     * @return true if there is ice, false otherwise.
     */
    private boolean isIceAbove() {
        BlockPos posAbove = this.getBlockPos().up();
        Block blockAbove = this.getWorld().getBlockState(posAbove).getBlock();
        return blockAbove == Blocks.ICE || blockAbove == Blocks.FROSTED_ICE || blockAbove == Blocks.PACKED_ICE;
    }


    // ========= Constructors =========

    /**
     * Constructs a new Penguin entity.
     * @param entityType The entity type of the penguin.
     * @param world The world the penguin exists in.
     */
    public PenguinEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        //Water Pathfinding is Enabled
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    // ========= Getters and Setters =========
    public BlockPos getTravelPos() {
        return this.dataTracker.get(TRAVEL_POS);
    }
    public void setTravelPos(BlockPos pos) {
        this.dataTracker.set(TRAVEL_POS, pos);
    }
    public boolean hasEgg() {
        return this.dataTracker.get(HAS_EGG);
    }
    public void setHasEgg(boolean hasEgg) {
        this.dataTracker.set(HAS_EGG, hasEgg);
    }
    public Vector3f getHitByPlayerDirection() {
        return this.dataTracker.get(HIT_BY_PLAYER_DIRECTION);
    }
    public void setHitByPlayerDirection(Vector3f direction) {
        this.dataTracker.set(HIT_BY_PLAYER_DIRECTION, direction);
    }
    public boolean isTravelling() {
        return this.dataTracker.get(TRAVELLING);
    }
    public void setTravelling(boolean travelling) {this.dataTracker.set(TRAVELLING, travelling);}
    public boolean isLandBound() {
        return this.dataTracker.get(Land_Bound);
    }
    public void setLandBound(boolean landBound) {
        this.dataTracker.set(Land_Bound, landBound);
    }

    public boolean isSwimming() {
        return isSwimming;
    }
    public boolean gotHitByPlayer() {
        return this.dataTracker.get(HIT_BY_PLAYER);
    }
    public void setHitByPlayer(boolean hitByPlayer) {
        this.dataTracker.set(HIT_BY_PLAYER, hitByPlayer);
    }
    public void setSwimming(boolean swimming) {
        this.isSwimming = swimming;
    }
    public static Predicate<LivingEntity> getTargetPredicate() {
        return Penguin_Food;
    }
    public boolean isOnIce() {
        BlockPos pos = this.getBlockPos().down();
        BlockState blockState = this.getWorld().getBlockState(pos);
        return blockState.isOf(Blocks.ICE) || blockState.isOf(Blocks.PACKED_ICE) || blockState.isOf(Blocks.BLUE_ICE) ||
                blockState.isOf(Blocks.FROSTED_ICE);
    }
    //Penguin Sliding Cooldown
    public int getSlidingCooldown() {
        return this.dataTracker.get(SLIDING_COOLDOWN);
    }
    //Set Penguin Sliding Cooldown
    public void setSlidingCooldown(int cooldown) {
        this.dataTracker.set(SLIDING_COOLDOWN, cooldown);
    }
    //Penguin is sliding on ice
    public boolean isSliding() {
        return this.dataTracker.get(SLIDING);
    }
    //set Penguin is sliding on ice
    public void setSliding(boolean sliding) {
        this.dataTracker.set(SLIDING, sliding);
    }

    //Penguin Water Collision is Disabled
    @Override
    public boolean isPushedByFluids() {
        return false;
    }
    public int getHungerLevel() {
        return this.hungerLevel;
    }
    // Decrease the hunger level of the penguin
    public void reduceHunger() {if (this.hungerLevel > 0) this.hungerLevel--;}
    // Increase the hunger level of the penguin
    public void increaseHunger(int amount) {
        this.hungerLevel = Math.min(this.hungerLevel + amount, 100);
    }
    // Initialize the data tracker for the penguin
    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(TRAVEL_POS, BlockPos.ORIGIN);
        builder.add(TRAVELLING, false);
        builder.add(Land_Bound, false);
        builder.add(SLIDING, false);
        builder.add(HIT_BY_PLAYER_DIRECTION, new Vector3f(0, 0, 0));
        builder.add(SLIDING_COOLDOWN, 0);
        builder.add(HIT_BY_PLAYER, false);
        builder.add(HAS_EGG, false);
        this.moveControl = new PenguinMoveControl(this,WALKING_SPEED);
    }
    //Penguin Data Tracker data stored for Save state
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("HasEgg", this.hasEgg());
        nbt.putInt("TravelPosX", this.getTravelPos().getX());
        nbt.putInt("TravelPosY", this.getTravelPos().getY());
        nbt.putInt("TravelPosZ", this.getTravelPos().getZ());
    }
    //Penguin Data Tracker data loaded for Load state
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setHasEgg(nbt.getBoolean("HasEgg"));
        int x = nbt.getInt("TravelPosX");
        int y = nbt.getInt("TravelPosY");
        int z = nbt.getInt("TravelPosZ");
        this.setTravelPos(new BlockPos(x, y, z));
    }
    //Penguin Data Initialization
    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData) {
        this.setTravelPos(BlockPos.ORIGIN);
        return super.initialize(world, difficulty, spawnReason, entityData);
    }
    // ========= AI and Behavior =========

    /**
     * Initializes AI goals for the penguin.
     */
    @Override
    protected void initGoals() {
        this.goalSelector.add(0,new PenguinSlideOnIceGoal(this));
        this.goalSelector.add(1,new PenguinMateGoal(this, 1.0));
        this.goalSelector.add(1,new PenguinLayEggGoal(this, 1.0));
        this.goalSelector.add(2,new PenguinHuntGoal(this, 1.0F));
        this.goalSelector.add(3,new PenguinEscapeGoal(this,1.0F));
        this.goalSelector.add(4,new TemptGoal(this,1.0F, (stack)-> stack.isIn(ModTags.Items.PENGUIN_FOOD),false));
        this.goalSelector.add(5,new WanderAroundGoal(this,1.0F));
    }

    // Register the data for the penguin
    public static DefaultAttributeContainer.Builder createAttributes(){
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,10)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,WALKING_SPEED)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }


    // ========= Animation =========

    /**
     * Updates animation states for the penguin.
     */
    private void setupAnimationStates() {
        if (this.idleAnimationTimeOut <= 0) {
            this.idleAnimationTimeOut = 40;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeOut;
        }
        if (this.swimIdleAnimationTimeOut <= 0) {
            this.swimIdleAnimationTimeOut = 40;
            this.swimIdleAnimationState.start(this.age);
        } else {
            --this.swimIdleAnimationTimeOut;
        }
    }
    //Baby penguin Scale is reduced
    @Override
    public float getScaleFactor() {
        return this.isBaby() ? 0.5F : 1.0F;
    }
    //Amphibious Navigation for the penguin
    @Override
    protected EntityNavigation createNavigation(World world) {
        return new PenguinSwimNavigation(this, world);
    }
    //Breeding Items for the penguin
    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isIn(ModTags.Items.PENGUIN_FOOD);
    }
    //Child Penguin Creation
    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.PENGUIN.create(world);
    }
    //favourability of land
    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        // Check if the position is on land (not in water)
        if (this.isLandBound() && !world.getFluidState(pos).isIn(FluidTags.WATER)) {
            return 10.0F;  // Favor land positions
        }else {
        return 1.0F;  // Less favorable for water or non-land positions
    }
}
    /**
     * Handles penguin behavior per game tick.
     */
    @Override
    public void tick() {
        super.tick();
        // Reduce hunger every 5 seconds
        if (this.age % 100 == 0) {
            this.reduceHunger();
        }
        //Sliding countdown reduction
        if (this.getSlidingCooldown() > 0) {
            this.setSlidingCooldown(this.getSlidingCooldown() - 1);
        }
        // Update the animation states
        if (this.getWorld().isClient) {
            this.setupAnimationStates();
        }
        //changing hitbox depending on the penguin's state
        if(this.isSliding() && this.isOnIce() && this.getVelocity().lengthSquared()>0.02 ||(this.isTouchingWater())){
            double height = 0.45f;
            double width = 0.7f;
            double offsetX = (width / 2);
            double offsetZ = (width / 2);

            // Set the bounding box dynamically
            this.setBoundingBox(new Box(
                    this.getX() - offsetX, this.getY() , this.getZ() - offsetZ,
                    this.getX() + offsetX, this.getY() + height, this.getZ() + offsetZ ));
        }

    }



    // ========= Movement =========

    // Method to make the penguin jump out of water with a boost
    private void jumpOutOfWater() {
        this.setVelocity(this.getVelocity().add(0.0D, 0.3D, 0.0D));
    }
    /**
     * Handles penguin movement in water.
     */
    @Override
    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement() && this.isTouchingWater()) {
            this.updateVelocity(0.1F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9D));

            // Check oxygen level and escape if it's low
            if (this.getAir() < 50) {
                BlockPos nearestOpening = findNearestLand(this, 20);
                if (nearestOpening != null) {
                    this.setLandBound(false);
                    this.setTravelling(true);
                    this.setTravelPos(nearestOpening);
                    // Check if there's ice above
                    if (this.isIceAbove()) {
                        this.jumpOutOfWater();
                        PenguinUtility.breakIceAbove(this);

                    } else {
                        this.jumpOutOfWater();
                    }
                }
            }

            // Natural sinking behavior if not escaping
            else if (this.getTarget() == null && !this.isLandBound()) {
                this.setVelocity(this.getVelocity().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(movementInput);
        }
    }

    // ========= Sounds =========
    @Nullable
    @Override
    public SoundEvent getAmbientSound() {
        return ModSounds.PENGUIN_AMBIENT;
    }
    @Nullable
    @Override
    public SoundEvent getHurtSound(DamageSource source) {
        return  ModSounds.PENGUIN_HURT;
    }
    @Nullable
    @Override
    public SoundEvent getDeathSound() {
        return ModSounds.PENGUIN_DEATH;
    }
}
