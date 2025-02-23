package net.oshino.penguinmod.entity.client;


import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.PenguinMod;
import net.oshino.penguinmod.entity.custom.PenguinEntity;

/**
 * Renders the PenguinEntity using the PenguinModel and applies scaling based on the entity's age.
 */
public class PenguinRenderer extends MobEntityRenderer<PenguinEntity,PenguinModel<PenguinEntity>> {

    /**
     * Constructor for the PenguinRenderer.
     *
     * @param context The rendering context provided by Minecraft.
     */
    public PenguinRenderer(EntityRendererFactory.Context context) {
        super(context, new PenguinModel<>(context.getPart(PenguinModel.PENGUIN)), 0.2f);
    }
    /**
     * Returns the texture used for rendering the PenguinEntity.
     *
     * @param entity The penguin entity instance.
     * @return The texture identifier for the penguin.
     */
    @Override
    public Identifier getTexture(PenguinEntity entity) {
        return Identifier.of(PenguinMod.MOD_ID,"textures/entity/penguin.png");
    }
    /**
     * Applies custom rendering transformations, such as scaling for baby penguins.
     *
     * @param livingEntity The penguin entity being rendered.
     * @param f Animation tick delta.
     * @param g Rotation angle.
     * @param matrixStack The transformation matrix stack.
     * @param vertexConsumerProvider The vertex consumer for rendering.
     * @param i Lighting value.
     */
    @Override
    public void render(PenguinEntity livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }
        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }


}
