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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import net.oshino.penguinmod.entity.ModEntities;
import net.oshino.penguinmod.util.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;



public class PenguinEntity extends TameableEntity implements Angerable {
    // Penguin Digging Ice or Snow Counter
    int diggingCounter;
    // Penguin Hunger Level
    private int hungerLevel = 100;
    // Penguin Swimming State
    private boolean isSwimming = false;
    // Animation State for the penguin
    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState swimIdleAnimationState = new AnimationState();

    private int idleAnimationTimeOut = 0;
    private int swimIdleAnimationTimeOut = 0;
    //Penguin Digging Ice or Snow (Boolean)
    private static final TrackedData<Boolean> DIGGING_ICE_OR_SNOW = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    //TravelPos is random position for the penguin to travel to in water
    private static final TrackedData<BlockPos> TRAVEL_POS = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
    //Penguin is travelling in water or not (Boolean)
    private static  final TrackedData<Boolean> TRAVELLING = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    // Penguin is in water mode or Land mode (Boolean)
    private static final TrackedData<Boolean> Land_Bound = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
//    //Penguin has Egg or not (Boolean)
//    private static final TrackedData<Boolean> HAS_EGG = DataTracker.registerData(PenguinEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
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
//    public boolean hasEgg() {
//        return this.dataTracker.get(HAS_EGG);
//    }
//    void setHasEgg(boolean hasEgg) {
//        this.dataTracker.set(HAS_EGG, hasEgg);
//    }
    boolean isTravelling() {
        return this.dataTracker.get(TRAVELLING);
    }
    void setTravelling(boolean travelling) {this.dataTracker.set(TRAVELLING, travelling);}
    boolean isLandBound() {
        return this.dataTracker.get(Land_Bound);
    }
    void setLandBound(boolean landBound) {
        this.dataTracker.set(Land_Bound, landBound);
    }
    public boolean isDiggingIceOrSnow() {
        return this.dataTracker.get(DIGGING_ICE_OR_SNOW);
    }

    void setDiggingIceOrSnow(boolean diggingIceOrSnow) {
        this.diggingCounter = diggingIceOrSnow ? 1 : 0;
        this.dataTracker.set(DIGGING_ICE_OR_SNOW, diggingIceOrSnow);
    }

    public boolean isSwimming() {
        return isSwimming;
    }

    public void setSwimming(boolean swimming) {
        this.isSwimming = swimming;
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
        builder.add(DIGGING_ICE_OR_SNOW, false);
//        builder.add(HAS_EGG, false);
        this.moveControl = new PenguinEntity.PenguinMoveControl(this);

    }
    //Penguin Data Tracker data stored for Save state
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
//        nbt.putBoolean("HasEgg", this.hasEgg());
        nbt.putInt("TravelPosX", this.getTravelPos().getX());
        nbt.putInt("TravelPosY", this.getTravelPos().getY());
        nbt.putInt("TravelPosZ", this.getTravelPos().getZ());
    }
    //Penguin Data Tracker data loaded for Load state
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
//        this.setHasEgg(nbt.getBoolean("HasEgg"));
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
        this.goalSelector.add(1,new AnimalMateGoal(this, 1.0));
//        this.goalSelector.add(1,new PenguinEntity.MateGoal(this, 1.0)); will add in future
        this.goalSelector.add(2,new PenguinEntity.PenguinHuntGoal(this, 1.2));
        this.goalSelector.add(3,new PenguinEntity.PenguinEscapeDangerGoal(this,1.2F));
        this.goalSelector.add(4,new TemptGoal(this,1.23D,stack ->stack.isIn(ModTags.Items.PENGUIN_FOOD),false));
        this.goalSelector.add(5,new WanderAroundGoal(this,1D));
        this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
        this.targetSelector.add(2, new AttackWithOwnerGoal(this));
        this.targetSelector.add(3, new RevengeGoal(this).setGroupRevenge());
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, AbstractSkeletonEntity.class, false));
        this.targetSelector.add(6, new UniversalAngerGoal<>(this, true));
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
    //Penguin Tick(Time in Minecraft) Function
    @Override
    public void tick() {
        super.tick();
        // Reduce hunger every 5 seconds
        if (this.age % 100 == 0) {
            this.reduceHunger();
        }
        // Update the animation states
        if (this.getWorld().isClient) {
            this.setupAnimationStates();
        }
        //changing hitbox depending on the penguin's state
        if (this.isTouchingWater() && this.getVelocity().lengthSquared()>0) {
            // Dimensions of the penguin
            double height = 0.45f;
            double width = 0.7f;
            double offsetX = (width / 2);
            double offsetZ = (width / 2);

            // Set the bounding box dynamically
            this.setBoundingBox(new Box(
                    this.getX() - offsetX, this.getY() + height, this.getZ() - offsetZ,
                    this.getX() + offsetX, this.getY() + height*1.8, this.getZ() + offsetZ ));
        }

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
                BlockPos nearestLand = findNearestLand(this, 10);
                 if(nearestLand != null){
                    this.setLandBound(true);
                    this.setTravelling(false);
                    this.setTravelPos(nearestLand);
                    this.jumpOutOfWater();
                }else{
                    BlockPos randomTravelPos = this.getBlockPos().add(
                            this.random.nextInt(16) - 8,
                            this.random.nextInt(8) - 4,
                            this.random.nextInt(16) - 8
                    );
                    this.setTravelPos(randomTravelPos);
                    this.jumpOutOfWater();

                }

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


    //Custom Penguin goals
    //Penguin mate Goal for breeding
//    static class MateGoal extends AnimalMateGoal {
//        private final PenguinEntity penguin;
//
//        MateGoal(PenguinEntity penguin, double speed) {
//            super(penguin, speed);
//            this.penguin = penguin;
//        }
//
//        @Override
//        public boolean canStart() {
//            return super.canStart() ;
//        }
//
//        @Override
//        protected void breed() {
//            ServerPlayerEntity serverPlayerEntity = this.animal.getLovingPlayer();
//            if (serverPlayerEntity == null && this.mate.getLovingPlayer() != null) serverPlayerEntity = this.mate.getLovingPlayer();
//
//            if (serverPlayerEntity != null) {
//                serverPlayerEntity.incrementStat(Stats.ANIMALS_BRED);
//                Criteria.BRED_ANIMALS.trigger(serverPlayerEntity, this.animal, this.mate, null);
//            }
//
//            this.animal.setBreedingAge(6000);
//            this.mate.setBreedingAge(6000);
//            this.animal.resetLoveTicks();
//            this.mate.resetLoveTicks();
//            Random random = this.animal.getRandom();
//            if (this.world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT)) {
//                this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1));
//            }
//            PenguinEntity babyPenguin = ModEntities.PENGUIN.create(this.world);
//            if (babyPenguin != null) {
//                babyPenguin.refreshPositionAndAngles(this.animal.getX(), this.animal.getY(), this.animal.getZ(), 0.0F, 0.0F);
//                babyPenguin.setBaby(true);
//                this.world.spawnEntity(babyPenguin);
//            }
//        }
//    }
    //Lay Egg Goal for the penguin
    //NEED TO MAKE PENGUIN EGG BLOCK
//    static class LayEggGoal extends MoveToTargetPosGoal {
//        private final PenguinEntity penguin;
//
//        LayEggGoal(PenguinEntity penguin, double speed) {
//            super(penguin, speed, 16);
//            this.penguin = penguin;
//        }
//
//        @Override
//        public boolean canStart() {
//            // Start if the penguin has an egg
//            return this.penguin.hasEgg() ? super.canStart() : false;
//        }
//
//        @Override
//        public boolean shouldContinue() {
//            // Continue if the penguin still has an egg
//            return super.shouldContinue() && this.penguin.hasEgg();
//        }
//
//        @Override
//        public void tick() {
//            super.tick();
//            BlockPos blockPos = this.penguin.getBlockPos();
//            if (!this.penguin.isTouchingWater() && this.hasReached()) {
//                if (this.penguin.diggingCounter < 1) {
//                    this.penguin.setDiggingIceOrSnow(true);
//                } else if (this.penguin.diggingCounter> this.getTickCount(200)) {
//                    World world = this.penguin.getWorld();
//                    world.playSound(null, blockPos, SoundEvents.ENTITY_TURTLE_LAY_EGG, SoundCategory.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F);
//                    BlockPos blockPos2 = this.targetPos.up();
//                    BlockState blockState = Blocks.PENGUIN_EGG.getDefaultState().with(PenguinEggBlock.EGGS, Integer.valueOf(this.penguin.random.nextInt(2) + 1));
//                    world.setBlockState(blockPos2, blockState, Block.NOTIFY_ALL);
//                    world.emitGameEvent(GameEvent.BLOCK_PLACE, blockPos2, GameEvent.Emitter.of(this.penguin, blockState));
//                    this.penguin.setHasEgg(false);
//                    this.penguin.setDiggingIceOrSnow(false);
//                    this.penguin.setLoveTicks(600);
//                }
//
//                if (this.penguin.isDiggingIceOrSnow()) {
//                    this.penguin.diggingCounter++;
//                }
//            }
//        }
//
//        @Override
//        protected boolean isTargetPos(WorldView world, BlockPos pos) {
//            // Check for valid ice/snow block instead of sand
//            return !world.isAir(pos.up()) ? false : PenguinEggBlock.isIceOrSnow(world, pos);
//        }
//    }

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

        // Complete tryAttack method in PenguinHuntGoal
        @Override
        public void tick() {
            if (this.target != null) {
                double distance = this.penguin.squaredDistanceTo(this.target);
                if (distance < 2.0) {
                    // Attack the target
                    this.penguin.tryAttack(this.target);
                    // Feed penguin and increase hunger
                    this.penguin.increaseHunger(20);
                    this.stop(); // End hunt after attacking
                } else {
                    // Keep moving towards the target
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
