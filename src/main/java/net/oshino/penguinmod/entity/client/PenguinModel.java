package net.oshino.penguinmod.entity.client;


import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.oshino.penguinmod.PenguinMod;
import net.oshino.penguinmod.entity.custom.PenguinEntity;


public class PenguinModel<T extends PenguinEntity> extends SinglePartEntityModel<T> {
    public static final EntityModelLayer PENGUIN = new EntityModelLayer(
            Identifier.of(PenguinMod.MOD_ID,"penguin"),
            "main");
    private final ModelPart base;
    private final ModelPart lower_body;
    private final ModelPart feet;
    private final ModelPart left_foot;
    private final ModelPart right_foot;
    private final ModelPart tail;
    private final ModelPart upper_body;
    private final ModelPart head;
    private final ModelPart beak;
    private final ModelPart middle_body;
    private final ModelPart torso;
    private final ModelPart flippers;
    private final ModelPart left_flipper;
    private final ModelPart right_flipper;
    public PenguinModel(ModelPart root) {
        this.base = root.getChild("base");
        this.lower_body = this.base.getChild("lower_body");
        this.feet = this.lower_body.getChild("feet");
        this.left_foot = this.feet.getChild("left_foot");
        this.right_foot = this.feet.getChild("right_foot");
        this.tail = this.lower_body.getChild("tail");
        this.upper_body = this.base.getChild("upper_body");
        this.head = this.upper_body.getChild("head");
        this.beak = this.head.getChild("beak");
        this.middle_body = this.upper_body.getChild("middle_body");
        this.torso = this.middle_body.getChild("torso");
        this.flippers = this.middle_body.getChild("flippers");
        this.left_flipper = this.flippers.getChild("left_flipper");
        this.right_flipper = this.flippers.getChild("right_flipper");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, 24.0F, -3.50F));

        ModelPartData lower_body = base.addChild("lower_body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData feet = lower_body.addChild("feet", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, 0.0F, 4.0F));

        ModelPartData left_foot = feet.addChild("left_foot", ModelPartBuilder.create().uv(22, 0).cuboid(-1.5F, -0.5F, -2.5F, 3.0F, 1.0F, 5.0F, new Dilation(-0.001F)), ModelTransform.pivot(3.5F, -0.5F, -1.5F));

        ModelPartData right_foot = feet.addChild("right_foot", ModelPartBuilder.create().uv(22, 6).cuboid(-1.5F, -0.5F, -2.5F, 3.0F, 1.0F, 5.0F, new Dilation(-0.001F)), ModelTransform.pivot(0.5F, -0.5F, -1.5F));

        ModelPartData tail = lower_body.addChild("tail", ModelPartBuilder.create().uv(2, 1).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, -2.0F, 6.5F));

        ModelPartData upper_body = base.addChild("upper_body", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, -4.0F, 3.0F));

        ModelPartData head = upper_body.addChild("head", ModelPartBuilder.create().uv(0, 12).cuboid(-3.0F, -3.0F, -2.0F, 6.0F, 3.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -4.0F, 0.0F));

        ModelPartData beak = head.addChild("beak", ModelPartBuilder.create().uv(22, 12).cuboid(-0.5F, 0.0F, -1.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -1.0F, -2.0F));

        ModelPartData middle_body = upper_body.addChild("middle_body", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData torso = middle_body.addChild("torso", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -3.5F, -2.5F, 6.0F, 7.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, 0.5F));

        ModelPartData flippers = middle_body.addChild("flippers", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, 4.0F, -3.0F));

        ModelPartData left_flipper = flippers.addChild("left_flipper", ModelPartBuilder.create().uv(0, 20).cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 7.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(4.5F, -7.5F, 3.5F));

        ModelPartData right_flipper = flippers.addChild("right_flipper", ModelPartBuilder.create().uv(0, 20).mirrored().cuboid(-0.5F, -0.5F, -2.5F, 1.0F, 7.0F, 5.0F, new Dilation(0.0F)).mirrored(false), ModelTransform.pivot(-2.5F, -7.5F, 3.5F));
        return TexturedModelData.of(modelData, 48, 48);
    }

    public void setAngles(PenguinEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(netHeadYaw, headPitch);

        // Check if the penguin is in water
        if (entity.isTouchingWater() || entity.isSliding()) {
            // Check if the penguin is moving
            if (entity.getVelocity().lengthSquared() > 0.02) {
                // Swimming animation
                if(entity.isAttacking()){
                    this.animateMovement(PenguinAnimation.PENGUIN_UNDERWATER_ATTACK, limbSwing, limbSwingAmount, 2f, 2f);
                }else{
                    this.animateMovement(PenguinAnimation.PENGUIN_SWIMMING, limbSwing, limbSwingAmount, 2f, 2f);
                }
                    this.base.pivotZ = this.base.pivotZ + 6f;

                // Set body pitch when swimming in water
                this.base.pitch = (float) Math.toRadians(90);
                this.head.pitch = (float) Math.toRadians(-90);

            } else {
                // Reset body pitch when stationary in water
                this.base.pitch = 0;
                this.head.pitch = 0;
            }
        } else {
            this.base.pitch = 0;
            this.head.pitch = 0;
            // Walking animation
            this.animateMovement(PenguinAnimation.PENGUIN_WALKING, limbSwing, limbSwingAmount, 2f, 2f);
        }
    }

    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
        headPitch = MathHelper.clamp(headPitch, -25.0F, 45.0F);

        this.head.yaw = headYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        base.render(matrices, vertexConsumer, light, overlay,color);
    }
    @Override
    public ModelPart getPart(){
        return base;
    }

}