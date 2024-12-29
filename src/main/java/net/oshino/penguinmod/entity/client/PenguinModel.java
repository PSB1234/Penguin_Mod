package net.oshino.penguinmod.entity.client;


import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.oshino.penguinmod.PenguinMod;
import net.oshino.penguinmod.entity.custom.PenguinEntities;

public class PenguinModel<T extends PenguinEntities> extends SinglePartEntityModel<T> {
    public static final EntityModelLayer PENGUIN = new EntityModelLayer(
            Identifier.of(PenguinMod.MOD_ID,"penguin"),
            "main");
    private final ModelPart base;
    private final ModelPart upper;
    private final ModelPart neck;
    private final ModelPart face;
    private final ModelPart upper_beak;
    private final ModelPart lower_beak;
    private final ModelPart eyebrow;
    private final ModelPart lower;
    private final ModelPart left_wing;
    private final ModelPart right_wing;
    private final ModelPart tail;
    private final ModelPart stomach;
    private final ModelPart feets;
    private final ModelPart left_feet;
    private final ModelPart right_feet;
    public PenguinModel(ModelPart root) {
        this.base = root.getChild("base");
        this.upper = this.base.getChild("upper");
        this.neck = this.upper.getChild("neck");
        this.face = this.upper.getChild("face");
        this.upper_beak = this.face.getChild("upper_beak");
        this.lower_beak = this.face.getChild("lower_beak");
        this.eyebrow = this.face.getChild("eyebrow");
        this.lower = this.base.getChild("lower");
        this.left_wing = this.lower.getChild("left_wing");
        this.right_wing = this.lower.getChild("right_wing");
        this.tail = this.lower.getChild("tail");
        this.stomach = this.lower.getChild("stomach");
        this.feets = this.base.getChild("feets");
        this.left_feet = this.feets.getChild("left_feet");
        this.right_feet = this.feets.getChild("right_feet");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData base = modelPartData.addChild("base", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData upper = base.addChild("upper", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -24.0F, 0.0F));

        ModelPartData neck = upper.addChild("neck", ModelPartBuilder.create().uv(0, 25).cuboid(-8.8707F, -6.076F, -2.1301F, 16.0F, 6.0F, 9.0F, new Dilation(0.0F)), ModelTransform.pivot(1.0F, 6.0F, -1.0F));

        ModelPartData face = upper.addChild("face", ModelPartBuilder.create().uv(50, 25).cuboid(-7.0F, -8.0F, -3.5F, 14.0F, 8.0F, 7.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 1.5F));

        ModelPartData upper_beak = face.addChild("upper_beak", ModelPartBuilder.create().uv(52, 79).cuboid(-3.0F, -20.0F, -5.0F, 6.0F, 2.0F, 3.0F, new Dilation(0.0F))
                .uv(70, 79).cuboid(-3.0F, -19.0F, -7.0F, 6.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, -1.5F));

        ModelPartData lower_beak = face.addChild("lower_beak", ModelPartBuilder.create().uv(76, 64).cuboid(-3.0F, -18.0F, -7.0F, 6.0F, 1.0F, 5.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, -1.5F));

        ModelPartData eyebrow = face.addChild("eyebrow", ModelPartBuilder.create().uv(80, 74).cuboid(3.0F, -22.0F, -3.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(80, 76).cuboid(-6.0F, -22.0F, -3.0F, 3.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 16.0F, -1.5F));

        ModelPartData lower = base.addChild("lower", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

        ModelPartData left_wing = lower.addChild("left_wing", ModelPartBuilder.create().uv(0, 70).cuboid(-0.5F, -0.5F, -4.0F, 12.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(56, 18).cuboid(0.5F, -1.5F, -3.0F, 9.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(56, 0).cuboid(-1.5F, 0.5F, -4.0F, 15.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(10.5F, -8.5F, 2.0F));

        ModelPartData right_wing = lower.addChild("right_wing", ModelPartBuilder.create().uv(40, 70).cuboid(-11.5F, -0.5F, -4.0F, 12.0F, 1.0F, 8.0F, new Dilation(0.0F))
                .uv(76, 57).cuboid(-9.5F, -1.5F, -3.0F, 9.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(56, 9).cuboid(-13.5F, 0.5F, -4.0F, 15.0F, 1.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(-10.5F, -8.5F, 2.0F));

        ModelPartData tail = lower.addChild("tail", ModelPartBuilder.create().uv(80, 70).cuboid(-1.0F, 3.0F, 6.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData stomach = lower.addChild("stomach", ModelPartBuilder.create().uv(26, 79).cuboid(-6.0F, -7.0F, 6.0F, 12.0F, 9.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 79).cuboid(-6.0F, -7.0F, -5.0F, 12.0F, 9.0F, 1.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-9.0F, -10.0F, -4.0F, 18.0F, 15.0F, 10.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData feets = base.addChild("feets", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData left_feet = feets.addChild("left_feet", ModelPartBuilder.create().uv(0, 40).cuboid(1.0F, 6.0F, -8.0F, 7.0F, 2.0F, 15.0F, new Dilation(0.0F))
                .uv(0, 57).cuboid(1.0F, 5.0F, -6.0F, 7.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -8.0F, 0.0F));

        ModelPartData right_feet = feets.addChild("right_feet", ModelPartBuilder.create().uv(44, 40).cuboid(-3.0F, 1.0F, -8.0F, 7.0F, 2.0F, 15.0F, new Dilation(0.0F))
                .uv(38, 57).cuboid(-3.0F, 0.0F, -6.0F, 7.0F, 1.0F, 12.0F, new Dilation(0.0F)), ModelTransform.pivot(-5.0F, -3.0F, 0.0F));
        return TexturedModelData.of(modelData, 112, 112);
    }
    @Override
    public void setAngles(PenguinEntities entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(netHeadYaw, headPitch);

        this.animateMovement(PenguinAnimation.WALKING, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.updateAnimation(entity.idleState, PenguinAnimation.CHECKING, ageInTicks, 1f);
    }

    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
        headPitch = MathHelper.clamp(headPitch, -25.0F, 45.0F);

        this.upper.yaw = headYaw * 0.017453292F;
        this.upper.pitch = headPitch * 0.017453292F;
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