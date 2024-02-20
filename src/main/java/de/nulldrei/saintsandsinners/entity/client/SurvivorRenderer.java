package de.nulldrei.saintsandsinners.entity.client;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.entity.animations.pose.SurvivorArmPose;
import de.nulldrei.saintsandsinners.entity.hostile.ReclaimedFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.hostile.TowerFactionSurvivor;
import de.nulldrei.saintsandsinners.entity.hostile.variant.ReclaimedVariant;
import de.nulldrei.saintsandsinners.entity.hostile.variant.TowerVariant;
import de.nulldrei.saintsandsinners.entity.neutral.RobberSurvivor;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemDisplayContext;

import java.util.Map;

public class SurvivorRenderer extends HumanoidMobRenderer<Mob, SurvivorModel<Mob>> {

    private static final Map<Variant, ResourceLocation> TEXTURE_BY_VARIANT_BEGGAR = Util.make(Maps.newEnumMap(Variant.class), (p_114874_) -> {
        p_114874_.put(Variant.TOM, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/tom.png"));
        p_114874_.put(Variant.PATRICK, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/patrick.png"));
        p_114874_.put(Variant.OSAMA, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/osama.png"));
    });

    private static final Map<de.nulldrei.saintsandsinners.entity.neutral.variant.Variant, ResourceLocation> TEXTURE_BY_VARIANT_ROBBER = Util.make(Maps.newEnumMap(de.nulldrei.saintsandsinners.entity.neutral.variant.Variant.class), (p_114874_) -> {
        p_114874_.put(de.nulldrei.saintsandsinners.entity.neutral.variant.Variant.BEN, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/ben.png"));
        p_114874_.put(de.nulldrei.saintsandsinners.entity.neutral.variant.Variant.RANDY, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/randy.png"));
        p_114874_.put(de.nulldrei.saintsandsinners.entity.neutral.variant.Variant.RICK, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/rick.png"));
    });

    private static final Map<TowerVariant, ResourceLocation> TEXTURE_BY_VARIANT_TOWER_GUARD = Util.make(Maps.newEnumMap(TowerVariant.class), (p_114874_) -> {
        p_114874_.put(TowerVariant.MISSY, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/missy.png"));
        p_114874_.put(TowerVariant.ABRAHAM, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/abraham.png"));
        p_114874_.put(TowerVariant.JOE, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/joe.png"));
    });

    private static final Map<ReclaimedVariant, ResourceLocation> TEXTURE_BY_VARIANT_RECLAIMED_CULTIST = Util.make(Maps.newEnumMap(ReclaimedVariant.class), (p_114874_) -> {
        p_114874_.put(ReclaimedVariant.GEORGIA  , new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/georgia.png"));
        p_114874_.put(ReclaimedVariant.WALTER, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/walter.png"));
        p_114874_.put(ReclaimedVariant.JESSE, new ResourceLocation(SaintsAndSinners.MODID, "textures/entity/survivor/jesse.png"));
    });

    private final ItemRenderer itemRenderer;
    static SurvivorModel<Mob> survivorModel;

    public SurvivorRenderer(EntityRendererProvider.Context p_174344_, ModelLayerLocation p_174345_, ModelLayerLocation p_174346_, ModelLayerLocation p_174347_, boolean p_174348_) {
        super(p_174344_, createModel(p_174344_.getModelSet(), p_174345_, p_174348_), 0.5F, 1.0019531F, 1.0F, 1.0019531F);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidArmorModel(p_174344_.bakeLayer(p_174346_)), new HumanoidArmorModel(p_174344_.bakeLayer(p_174347_)), p_174344_.getModelManager()));
        this.itemRenderer = p_174344_.getItemRenderer();
    }

    private static SurvivorModel<Mob> createModel(EntityModelSet p_174350_, ModelLayerLocation p_174351_, boolean p_174352_) {
        survivorModel = new SurvivorModel<>(p_174350_.bakeLayer(p_174351_));

        return survivorModel;
    }

    @Override
    public void render(Mob p_115455_, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) {
        if(p_115455_ instanceof BeggarSurvivor beggarSurvivor) {
            if (beggarSurvivor.getArmPose() == SurvivorArmPose.BEGGING_FOR_ITEM) {
                if (beggarSurvivor.isAlive()) {
                    p_115458_.pushPose();
                    p_115458_.translate(0, 2.5, 0);
                    p_115458_.scale(0.5f, 0.5f, 0.5f);
                    this.itemRenderer.renderStatic(beggarSurvivor.getNeededItem(), ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, p_115458_, p_115459_, p_115455_.level(), p_115455_.getId());
                    p_115458_.popPose();
                }
            }
        } else if(p_115455_ instanceof RobberSurvivor robberSurvivor) {
            if (robberSurvivor.getArmPose() == SurvivorArmPose.DEMANDING_ITEM) {
                if (robberSurvivor.isAlive()) {
                    p_115458_.pushPose();
                    p_115458_.translate(0, 2.5, 0);
                    p_115458_.scale(0.5f, 0.5f, 0.5f);
                    this.itemRenderer.renderStatic(robberSurvivor.getNeededItem(), ItemDisplayContext.FIXED, 15728880, OverlayTexture.NO_OVERLAY, p_115458_, p_115459_, p_115455_.level(), p_115455_.getId());
                    p_115458_.popPose();
                }
            }
        }
        super.render(p_115455_, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
    }


    @Override
    public ResourceLocation getTextureLocation(Mob p_114482_) {
        if (p_114482_ instanceof BeggarSurvivor beggarSurvivor) {
            return TEXTURE_BY_VARIANT_BEGGAR.get(beggarSurvivor.getVariant());
        } else if(p_114482_ instanceof RobberSurvivor robberSurvivor) {
            return TEXTURE_BY_VARIANT_ROBBER.get(robberSurvivor.getVariant());
        }
        else if(p_114482_ instanceof TowerFactionSurvivor towerFactionSurvivor) {
            return TEXTURE_BY_VARIANT_TOWER_GUARD.get(towerFactionSurvivor.getVariant());
        }
        else if(p_114482_ instanceof ReclaimedFactionSurvivor reclaimedFactionSurvivor) {
            return TEXTURE_BY_VARIANT_RECLAIMED_CULTIST.get(reclaimedFactionSurvivor.getVariant());
        }
        return null;
    }
}
