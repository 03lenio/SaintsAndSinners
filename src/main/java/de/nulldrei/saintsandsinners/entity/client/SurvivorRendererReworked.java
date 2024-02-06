package de.nulldrei.saintsandsinners.entity.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.animations.pose.SurvivorArmPose;
import de.nulldrei.saintsandsinners.entity.peaceful.BeggarSurvivor;
import de.nulldrei.saintsandsinners.entity.peaceful.variant.Variant;
import net.minecraft.Util;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

import javax.annotation.Nullable;
import java.util.Map;

public class SurvivorRendererReworked extends HumanoidMobRenderer<Mob, SurvivorModelReworked<Mob>> {
    private static final Map<EntityType<?>, ResourceLocation> TEXTURES = ImmutableMap.of(EntityType.PIGLIN, new ResourceLocation("textures/entity/piglin/piglin.png"), EntityType.ZOMBIFIED_PIGLIN, new ResourceLocation("textures/entity/piglin/zombified_piglin.png"), EntityType.PIGLIN_BRUTE, new ResourceLocation("textures/entity/piglin/piglin_brute.png"));

    private static final Map<Variant, ResourceLocation> TEXTURE_BY_VARIANT = Util.make(Maps.newEnumMap(Variant.class), (p_114874_) -> {
        p_114874_.put(Variant.TOM, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/tom.png"));
        p_114874_.put(Variant.PATRICK, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/patrick.png"));
        p_114874_.put(Variant.OSAMA, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/osama.png"));
    });

    private final ItemRenderer itemRenderer;
    static SurvivorModelReworked<Mob> survivorModel;
    private final ItemInHandRenderer itemInHandRenderer;

    public SurvivorRendererReworked(EntityRendererProvider.Context p_174344_, ModelLayerLocation p_174345_, ModelLayerLocation p_174346_, ModelLayerLocation p_174347_, boolean p_174348_) {
        super(p_174344_, createModel(p_174344_.getModelSet(), p_174345_, p_174348_), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidArmorModel(p_174344_.bakeLayer(p_174346_)), new HumanoidArmorModel(p_174344_.bakeLayer(p_174347_)), p_174344_.getModelManager()));
        this.itemRenderer = p_174344_.getItemRenderer();

        this.itemInHandRenderer = p_174344_.getItemInHandRenderer();
    }

    private static SurvivorModelReworked<Mob> createModel(EntityModelSet p_174350_, ModelLayerLocation p_174351_, boolean p_174352_) {
        survivorModel = new SurvivorModelReworked<>(p_174350_.bakeLayer(p_174351_));

        return survivorModel;
    }

    @Override
    public void render(Mob p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) {
        BeggarSurvivor beggarSurvivor = (BeggarSurvivor) p_115455_;
        if(beggarSurvivor.getArmPose() == SurvivorArmPose.BEGGING_FOR_ITEM) {
           // itemInHandRenderer.renderItem(beggarSurvivor, beggarSurvivor.getNeededItem(),  beggarSurvivor.isLeftHanded() ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, true, p_115458_, p_115459_, 100);
            /*
            p_115458_.pushPose();

            survivorModel.translateToHand(HumanoidArm.LEFT, p_115458_);

            p_115458_.mulPose(Axis.XP.rotationDegrees(0.0F));
            p_115458_.mulPose(Axis.YP.rotationDegrees(180.0F));
            p_115458_.rotateAround(Axis.YP.rotationDegrees(0F), 180, 180, 0);
            p_115458_.translate((float)(-1) / 16.0F, -1.55F, -0.625F);
            this.itemInHandRenderer.renderItem(p_115455_, beggarSurvivor.getNeededItem(), ItemDisplayContext.HEAD, false, p_115458_, p_115459_, p_115460_ - 0);
            p_115458_.popPose();

             */
            //this.itemRenderer.renderStatic(p_115455_, beggarSurvivor.getNeededItem(),  beggarSurvivor.isLeftHanded() ? ItemDisplayContext.FIRST_PERSON_RIGHT_HAND : ItemDisplayContext.FIRST_PERSON_LEFT_HAND, p_270203_, p_270974_, p_270686_, p_270072_.level(), p_115460_, OverlayTexture.NO_OVERLAY, p_115455_.getId() + p.ordinal());
            //itemRenderer.renderStatic(p_115455_, beggarSurvivor.getNeededItem(), ItemDisplayContext.HEAD, beggarSurvivor.isLeftHanded(), p_115458_, p_115459_, p_115455_.level(), 200, 350,200);
            if(beggarSurvivor.isAlive()) {
                p_115458_.pushPose();
                p_115458_.translate(0, 2.5, 0);
                p_115458_.scale(0.5f, 0.5f, 0.5f);
                this.itemRenderer.renderStatic(beggarSurvivor.getNeededItem(), ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, p_115458_, p_115459_, p_115455_.level(), p_115455_.getId());

                p_115458_.popPose();
            }
        }
        super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
    }




    @Override
    public ResourceLocation getTextureLocation(Mob p_114482_) {
        BeggarSurvivor beggarSurvivor = (BeggarSurvivor) p_114482_;
        return TEXTURE_BY_VARIANT.get(beggarSurvivor.getVariant());
    }
}
