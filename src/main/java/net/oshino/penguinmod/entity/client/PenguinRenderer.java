package net.oshino.penguinmod.entity.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.oshino.penguinmod.PenguinMod;
import net.oshino.penguinmod.entity.custom.PenguinEntities;

public class PenguinRenderer extends MobEntityRenderer<PenguinEntities,PenguinModel<PenguinEntities>> {
    public PenguinRenderer(EntityRendererFactory.Context context) {
        super(context, new PenguinModel<>(context.getPart(PenguinModel.Penguin)), 0.75f);
    }

    @Override
    public Identifier getTexture(PenguinEntities entity) {
        return Identifier.of(PenguinMod.MOD_ID,"textures/entity/penguin/penguin.png");
    }

    @Override
    public void render(PenguinEntities livingEntity, float f, float g, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

}
