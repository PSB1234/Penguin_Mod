package net.oshino.penguinmod.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.AmphibiousSwimNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.oshino.penguinmod.entity.ModEntities;
import net.oshino.penguinmod.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class PenguinEntity extends TameableEntity implements Angerable {
    // Penguin Hunger Level
    private int hungerLevel = 100;
    // Animation State for the penguin
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeOut = 0;
    //Penguins Oxygen Capacity
    public static final int MAX_AIR = 3600;
    //TravelPos is random position for the penguin to travel to in water
    private static final TrackedData<BlockPos> TRAVEL_POS = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    //Penguin is travelling in water or not (Boolean)
    private static  final TrackedData<Boolean> TRAVELLING = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    // Penguin is in water mode or Land mode (Boolean)
    private static final TrackedData<Boolean> Land_Bound = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    // Items that the penguin will follow
    // private static final Ingredient TEMPTATION_ITEMS = Ingredient.ofItems(Items.COD, Items.SALMON,Items.INK_SAC,Items.GLOW_INK_SAC);

    // Predicate for the penguin to follow the Mobs
    public static final Predicate<LivingEntity> Penguin_Food = entity -> {
        EntityType<?> entityType = entity.getType();
        return entityType == EntityType.COD || entityType == EntityType.SALMON || entityType == EntityType.SQUID||
                entityType == EntityType.GLOW_SQUID || entityType == EntityType.TROPICAL_FISH;
    };


    // Constructor for the penguin entity
    public PenguinEntity(EntityType<? extends PenguinEntity> entityType, World world) {
        super(entityType, world);
        //Water Pathfinding is Enabled
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    //getters and setters for the penguin's data tracker
    BlockPos getTravelPos() {
        return this.dataTracker.get(TRAVEL_POS);
    }
    void setTravelPos(BlockPos pos) {
        this.dataTracker.set(TRAVEL_POS, pos);
    }

    boolean isTravelling() {
        return this.dataTracker.get(TRAVELLING);
    }
    void setTravelling(boolean travelling) {
        this.dataTracker.set(TRAVELLING, travelling);
    }
    boolean isLandBound() {
        return this.dataTracker.get(Land_Bound);
    }
    void setLandBound(boolean landBound) {
        this.dataTracker.set(Land_Bound, landBound);
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
    public void reduceHunger() {
        if (this.hungerLevel > 0) {
            this.hungerLevel--;
        }
    }
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
        this.moveControl = new PenguinEntity.PenguinMoveControl(this);

    }
    //Penguin Data Tracker data stored for Save state
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("TravelPosX", this.getTravelPos().getX());
        nbt.putInt("TravelPosY", this.getTravelPos().getY());
        nbt.putInt("TravelPosZ", this.getTravelPos().getZ());
    }
    //Penguin Data Tracker data loaded for Load state
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
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

    // init Goals for the penguin
    @Override
    protected void initGoals() {

        this.goalSelector.add(1,new PenguinEntity.PenguinHuntGoal(this, 1.2));
        this.goalSelector.add(2,new PenguinEntity.PenguinEscapeDangerGoal(this,1.2F));
        this.goalSelector.add(3,new TemptGoal(this,1.23D,stack ->stack.isIn(ModTags.Items.PENGUIN_FOOD),false));
        this.goalSelector.add(4,new WanderAroundGoal(this,1D));


    }

    // Register the data for the penguin
    public static DefaultAttributeContainer.Builder createAttributes(){
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH,10)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED,0.35)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }


    // Animation States Manipulation
    private void setupAnimationStates() {
        if (this.idleAnimationTimeOut <= 0) {
            this.idleAnimationTimeOut = 40;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeOut;
        }

    }
    //Baby penguin Scale is reduced
    @Override
    public float getScaleFactor() {
        return this.isBaby() ? 0.3F : 1.0F;
    }

    //Amphibious Navigation for the penguin
    @Override
    protected EntityNavigation createNavigation(World world) {
        return new PenguinEntity.PenguinSwimNavigation(this, world);
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
    // if the block is solid land
    private static boolean isSolidLand(BlockState blockState, World world, BlockPos checkPos) {
        return blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isOf(Blocks.DIRT) ||
                blockState.isOf(Blocks.SAND) || blockState.isSolidBlock(world, checkPos) ;
    }
    // Method to make the penguin jump out of water
    private void jumpOutOfWater() {
        this.setVelocity(this.getVelocity().add(0.0D, 0.05D, 0.0D));
    }
    //Penguin travel
    @Override
    public void travel(Vec3d movementInput) {
        if (this.isLogicalSideForUpdatingMovement() && this.isTouchingWater()) {
            this.updateVelocity(0.1F, movementInput);
            this.move(MovementType.SELF, this.getVelocity());
            this.setVelocity(this.getVelocity().multiply(0.9D));

            // Check oxygen level and jump out if low
            // Threshold for low oxygen
            if (this.getAir() < 20) {
                BlockPos pos = findNearestLand(this, 10);
                if(pos != null){
                    this.setLandBound(true);
                    this.setTravelling(false);
                    this.setTravelPos(pos);
                    this.jumpOutOfWater();
                }else{
                    BlockPos randomTravelPos = this.getBlockPos().add(
                            this.random.nextInt(16) - 8,
                            this.random.nextInt(8) - 4,
                            this.random.nextInt(16) - 8
                    );
                    this.setTravelPos(randomTravelPos);
                }
                this.jumpOutOfWater();
            } else if (this.getTarget() == null && !this.isLandBound()) {
                this.setVelocity(this.getVelocity().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(movementInput);
        }
    }


    //Penguin Navigation in water
    static class PenguinSwimNavigation extends AmphibiousSwimNavigation {
        PenguinSwimNavigation(PenguinEntity penguin, World world) {
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

    //Penguin Move Control
    static class PenguinMoveControl extends MoveControl {
        private final PenguinEntity penguin;

        PenguinMoveControl(PenguinEntity penguin) {
            super(penguin);
            this.penguin = penguin;
        }

        private void updateVelocity() {
            if (this.penguin.isTouchingWater()) {
                // Penguins swim efficiently with increased speed and vertical buoyancy
                this.penguin.setVelocity(this.penguin.getVelocity().add(0.0, 0.1, 0.0));
                this.penguin.setMovementSpeed(Math.max(this.penguin.getMovementSpeed(), 0.15F));
            } else if (this.penguin.isOnGround()) {

                    // Penguins waddle slowly on land
                    this.penguin.setMovementSpeed(Math.max(this.penguin.getMovementSpeed() / 1.5F, 0.08F));

            }
        }

        @Override
        public void tick() {
            this.updateVelocity();
            if (this.state == MoveControl.State.MOVE_TO && !this.penguin.getNavigation().isIdle()) {
                double d = this.targetX - this.penguin.getX();
                double e = this.targetY - this.penguin.getY();
                double f = this.targetZ - this.penguin.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                if (g < 1.0E-5F) {
                    this.entity.setMovementSpeed(0.0F);
                } else {
                    e /= g;
                    float h = (float)(MathHelper.atan2(f, d) * 180.0F / (float) Math.PI) - 90.0F;
                    this.penguin.setYaw(this.wrapDegrees(this.penguin.getYaw(), h, 90.0F));
                    this.penguin.bodyYaw = this.penguin.getYaw();
                    float i = (float) (this.speed * this.penguin.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
                    this.penguin.setMovementSpeed(MathHelper.lerp(0.125F, this.penguin.getMovementSpeed(), i));

                    // Adjust velocity for swimming or waddling
                    if (this.penguin.isTouchingWater()) {
                        this.penguin.setVelocity(this.penguin.getVelocity().add(0.0, (double) this.penguin.getMovementSpeed() * e * 0.2, 0.0));
                    } else {
                        this.penguin.setVelocity(this.penguin.getVelocity().multiply(1.0, 0.0, 1.0)); // Ground movement
                    }
                }
            } else {
                this.penguin.setMovementSpeed(0.0F);
            }
        }
    }

    //Penguin Tick(Time in Minecraft) Function
    @Override
    public void tick() {
        super.tick();
        // Reduce hunger every 5 seconds
        if (this.age % 100 == 0) {
            this.reduceHunger();
        }
    }

    //Custom Penguin goals

    //penguin escape danger goal

    static class PenguinEscapeDangerGoal extends EscapeDangerGoal{
        public PenguinEscapeDangerGoal(PenguinEntity penguin, double speed) {
            super(penguin, speed);
        }
        @Override
        public boolean canStart() {
            if (!this.isInDanger()) {
                return false;
            } else {
                BlockPos blockPos = this.locateClosestWater(this.mob.getWorld(), this.mob, 7);
                if (blockPos != null) {
                    this.targetX = blockPos.getX();
                    this.targetY = blockPos.getY();
                    this.targetZ = blockPos.getZ();
                    return true;
                } else {
                    return this.findTarget();
                }
            }
        }
    }
    public static class PenguinHuntGoal extends Goal {
        private final PenguinEntity penguin;
        private final double speed;
        private LivingEntity target;

        public PenguinHuntGoal(PenguinEntity penguin, double speed) {
            this.penguin = penguin;
            this.speed = speed;
        }

        @Override
        public boolean canStart() {
            // Start if the penguin is hungry and there is prey nearby
            return this.penguin.getHungerLevel() < 50 && this.findPrey();
        }

        @Override
        public boolean shouldContinue() {
            // Continue as long as the penguin is still hungry and the target exists
            return this.penguin.getHungerLevel() < 80 && this.target != null && this.target.isAlive();
        }

        @Override
        public void start() {
            // Begin moving toward the target
            this.penguin.getNavigation().startMovingTo(this.target, this.speed);
        }

        @Override
        public void stop() {
            // Clear the target when the goal ends
            this.target = null;
            this.penguin.getNavigation().stop();
        }

        @Override
        public void tick() {
            if (this.target != null) {
                double distance = this.penguin.squaredDistanceTo(this.target);
                if (distance < 2.0) {
                    // Attack the target if close enough
                    this.penguin.tryAttack(this.target);
                } else {
                    // Continue moving toward the target
                    this.penguin.getNavigation().startMovingTo(this.target, this.speed);
                }
            }
        }

        private boolean findPrey() {
            // Find nearby prey that matches the FOLLOW_TAMED_PREDICATE
            List<LivingEntity> nearbyEntities = this.penguin.getWorld().getEntitiesByClass(
                    LivingEntity.class,
                    this.penguin.getBoundingBox().expand(10.0),
                    Penguin_Food
            );
            if (!nearbyEntities.isEmpty()) {
                this.target = nearbyEntities.getFirst(); // Select the first valid target
                return true;
            }
            return false;
        }
    }



    @Override
    public int getAngerTime() {
        return 0;
    }

    @Override
    public void setAngerTime(int angerTime) {

    }

    @Override
    public @Nullable UUID getAngryAt() {
        return null;
    }

    @Override
    public void setAngryAt(@Nullable UUID angryAt) {

    }

    @Override
    public void chooseRandomAngerTime() {

    }


}
