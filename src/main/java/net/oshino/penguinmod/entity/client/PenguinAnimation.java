package net.oshino.penguinmod.entity.client;

import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;

public class PenguinAnimation {

    public static final Animation WALKING = Animation.Builder.create(1.375f).looping()
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, -2f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.125f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3433333f, AnimationHelper.createTranslationalVector(0f, 0f, 2.66f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 1f, 4f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.6766666f, AnimationHelper.createTranslationalVector(0f, 1f, 4f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.9167666f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.1676667f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.375f, AnimationHelper.createTranslationalVector(0f, 0f, -2f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.125f, AnimationHelper.createRotationalVector(-1.25f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.3433333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(22.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.6766666f, AnimationHelper.createRotationalVector(22.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.9167666f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.1676667f, AnimationHelper.createRotationalVector(-35f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.375f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 1f, 4f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 1f, -3f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.75f, AnimationHelper.createTranslationalVector(0f, 0f, -2f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.0834333f, AnimationHelper.createTranslationalVector(0f, 0f, 2.66f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.375f, AnimationHelper.createTranslationalVector(0f, 1f, 4f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(22.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(-35f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.75f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.875f, AnimationHelper.createRotationalVector(-1.25f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.0834333f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.375f, AnimationHelper.createRotationalVector(22.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("upper_body",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, 5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.8343334f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.1676667f, AnimationHelper.createRotationalVector(0f, 0f, -5f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.375f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("left_flipper",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -1f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("left_flipper",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(348.14f, -19.87f, -46.41f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(328.14f, -19.87f, -46.41f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.1676667f, AnimationHelper.createRotationalVector(348.14f, -19.87f, -46.41f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.375f, AnimationHelper.createRotationalVector(348.14f, -19.87f, -46.41f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("right_flipper",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -1f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.1676667f, AnimationHelper.createTranslationalVector(0f, -1f, 0f),
                                    Transformation.Interpolations.CUBIC)))
            .addBoneAnimation("right_flipper",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-12.86f, -26.34f, 67.09f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(-18.24f, -9.6f, 56.27f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(1.1676667f, AnimationHelper.createRotationalVector(-12.86f, -26.34f, 67.09f),
                                    Transformation.Interpolations.CUBIC))).build();
    public static final Animation HAPPY = Animation.Builder.create(0.5f)
            .addBoneAnimation("left_flipper",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_flipper",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 0f, -102.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_flipper",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_flipper",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(0f, 0f, 102.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR))).build();
    public static final Animation SWIMMING = Animation.Builder.create(1f).looping()
            .addBoneAnimation("base",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 2f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createTranslationalVector(0f, 2f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("base",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(90f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(90f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("feet",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -1f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createTranslationalVector(0f, -1f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, -1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.75f, AnimationHelper.createTranslationalVector(0f, -1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(45f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(87.5f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(45f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.75f, AnimationHelper.createRotationalVector(87.5f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(45f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, -1f, 1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, -1f, 1f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.75f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createTranslationalVector(0f, -1f, 1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_foot",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(87.5f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.25f, AnimationHelper.createRotationalVector(45f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(87.5f, 0f, 0f),
                                    Transformation.Interpolations.CUBIC),
                            new Keyframe(0.75f, AnimationHelper.createRotationalVector(45f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(87.5f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 3f, -1f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createTranslationalVector(0f, 3f, -1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(-92.64f, 6.93f, 0.02f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(-92.63f, -5.56f, 0.6f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(-92.64f, 6.93f, 0.02f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_flipper",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, -55f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, -125f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(0f, 0f, -55f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_flipper",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 122.5f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createRotationalVector(0f, 0f, 60f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(0f, 0f, 122.5f),
                                    Transformation.Interpolations.LINEAR))).build();
    public static final Animation DROPING = Animation.Builder.create(1f)
            .addBoneAnimation("base",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createTranslationalVector(0f, 2f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("base",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(90f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("feet",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(62.5f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("tail",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(1f, AnimationHelper.createRotationalVector(0f, -7.5f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.TRANSLATE,
                            new Keyframe(0f, AnimationHelper.createTranslationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.5f, AnimationHelper.createTranslationalVector(0f, 0.79f, -1.21f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(0.75f, AnimationHelper.createTranslationalVector(0f, 1.52f, -2.02f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createTranslationalVector(0f, 3f, -1f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("head",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(-90f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("left_flipper",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(0f, 0f, -50f),
                                    Transformation.Interpolations.LINEAR)))
            .addBoneAnimation("right_flipper",
                    new Transformation(Transformation.Targets.ROTATE,
                            new Keyframe(0f, AnimationHelper.createRotationalVector(0f, 0f, 0f),
                                    Transformation.Interpolations.LINEAR),
                            new Keyframe(1f, AnimationHelper.createRotationalVector(0f, 0f, 70f),
                                    Transformation.Interpolations.LINEAR))).build();
}
