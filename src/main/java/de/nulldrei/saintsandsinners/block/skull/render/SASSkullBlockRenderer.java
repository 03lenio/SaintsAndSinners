package de.nulldrei.saintsandsinners.block.skull.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.block.skull.SASSkullBlock;
import net.minecraft.client.model.PiglinHeadModel;
import net.minecraft.client.model.SkullModel;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.dragon.DragonHeadModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.ModLoader;

import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SASSkullBlockRenderer extends SkullBlockRenderer implements BlockEntityRenderer<SkullBlockEntity> {
    private final Map<SkullBlock.Type, SkullModelBase> modelByType;

    public SASSkullBlockRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
        this.modelByType = createSkullRenderers(context.getModelSet());
        SKIN_BY_TYPE.put(SASSkullBlock.Types.ABRAHAM, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/abraham.png"));
        SKIN_BY_TYPE.put(SASSkullBlock.Types.BEN, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/ben.png"));
        SKIN_BY_TYPE.put(SASSkullBlock.Types.GEORGIA, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/georgia.png"));
        SKIN_BY_TYPE.put(SASSkullBlock.Types.JESSE, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/jesse.png"));
        SKIN_BY_TYPE.put(SASSkullBlock.Types.JOE, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/joe.png"));
        SKIN_BY_TYPE.put(SASSkullBlock.Types.MISSY, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/missy.png"));
        SKIN_BY_TYPE.put(SASSkullBlock.Types.OSAMA, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/osama.png"));
        SKIN_BY_TYPE.put(SASSkullBlock.Types.PATRICK, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/patrick.png"));
        SKIN_BY_TYPE.put(SASSkullBlock.Types.RANDY, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/randy.png"));
        SKIN_BY_TYPE.put(SASSkullBlock.Types.RICK, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/rick.png"));
        SKIN_BY_TYPE.put(SASSkullBlock.Types.TOM, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/tom.png"));
        SKIN_BY_TYPE.put(SASSkullBlock.Types.WALTER, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/walter.png"));
    }

    @Override
    public void render(SkullBlockEntity skullBlockEntity, float partialTicks, PoseStack poseStack,
                       MultiBufferSource multiBufferSource, int p_112538_, int p_112539_) {
        float f = skullBlockEntity.getAnimation(partialTicks);
        BlockState blockstate = skullBlockEntity.getBlockState();
        boolean flag = blockstate.getBlock() instanceof WallSkullBlock;
        Direction direction = flag ? blockstate.getValue(WallSkullBlock.FACING) : null;
        int i = flag ? RotationSegment.convertToSegment(direction.getOpposite()) : blockstate.getValue(SkullBlock.ROTATION);
        float f1 = RotationSegment.convertToDegrees(i);
        SkullBlock.Type skullBlock$type = ((AbstractSkullBlock)blockstate.getBlock()).getType();
        SkullModelBase skullmodelbase = this.modelByType.get(skullBlock$type);
        RenderType rendertype = getRenderType(skullBlock$type, skullBlockEntity.getOwnerProfile());
        renderSkull(direction, f1, f, poseStack, multiBufferSource, p_112538_, skullmodelbase, rendertype);
    }

    public static void renderSkull(@Nullable Direction direction, float p_173665_, float animationProgress,
                                   PoseStack poseStack, MultiBufferSource multiBufferSource, int p_173669_,
                                   SkullModelBase skullModelBase, RenderType renderType) {
        poseStack.pushPose();
        if (direction == null) {
            poseStack.translate(0.5F, 0.0F, 0.5F);
        } else {
            float horizontalTranslation = 0.25F;
            float verticalTranslation = 0.25F;
            poseStack.translate(0.5F - (float)direction.getStepX() * horizontalTranslation, verticalTranslation,
                    0.5F - (float)direction.getStepZ() * horizontalTranslation);
        }

        poseStack.scale(-1.0F, -1.0F, 1.0F);
        VertexConsumer vertexconsumer = multiBufferSource.getBuffer(renderType);
        skullModelBase.setupAnim(animationProgress, p_173665_, 0.0F);
        skullModelBase.renderToBuffer(poseStack, vertexconsumer, p_173669_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    @SuppressWarnings("UnstableApiUsage")
    public static Map<SkullBlock.Type, SkullModelBase> createSkullRenderers(EntityModelSet entityModelSet) {
        ImmutableMap.Builder<SkullBlock.Type, SkullModelBase> builder = ImmutableMap.builder();
        builder.put(SkullBlock.Types.SKELETON, new SkullModel(entityModelSet.bakeLayer(ModelLayers.SKELETON_SKULL)));
        builder.put(SkullBlock.Types.WITHER_SKELETON, new SkullModel(entityModelSet.bakeLayer(ModelLayers.WITHER_SKELETON_SKULL)));
        builder.put(SkullBlock.Types.PLAYER, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SkullBlock.Types.ZOMBIE, new SkullModel(entityModelSet.bakeLayer(ModelLayers.ZOMBIE_HEAD)));
        builder.put(SkullBlock.Types.CREEPER, new SkullModel(entityModelSet.bakeLayer(ModelLayers.CREEPER_HEAD)));
        builder.put(SkullBlock.Types.DRAGON, new DragonHeadModel(entityModelSet.bakeLayer(ModelLayers.DRAGON_SKULL)));
        builder.put(SkullBlock.Types.PIGLIN, new PiglinHeadModel(entityModelSet.bakeLayer(ModelLayers.PIGLIN_HEAD)));
        builder.put(SASSkullBlock.Types.ABRAHAM, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SASSkullBlock.Types.BEN, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SASSkullBlock.Types.GEORGIA, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SASSkullBlock.Types.JESSE, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SASSkullBlock.Types.JOE, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SASSkullBlock.Types.MISSY, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SASSkullBlock.Types.OSAMA, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SASSkullBlock.Types.PATRICK, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SASSkullBlock.Types.RANDY, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SASSkullBlock.Types.RICK, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SASSkullBlock.Types.TOM, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));
        builder.put(SASSkullBlock.Types.WALTER, new SkullModel(entityModelSet.bakeLayer(ModelLayers.PLAYER_HEAD)));

        ModLoader.get().postEvent(new EntityRenderersEvent.CreateSkullModels(builder, entityModelSet));
        return builder.build();
    }
}