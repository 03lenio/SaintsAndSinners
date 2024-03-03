package de.nulldrei.saintsandsinners.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import de.nulldrei.saintsandsinners.entity.projectile.TimedNoiseMakerBomb;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TimedNoiseMakerBombRenderer extends EntityRenderer<TimedNoiseMakerBomb> {
   private static final float MIN_CAMERA_DISTANCE_SQUARED = 12.25F;
   private final ItemRenderer itemRenderer;
   private final RandomSource random = RandomSource.create();

   public TimedNoiseMakerBombRenderer(EntityRendererProvider.Context p_174198_) {
      super(p_174198_);
      this.itemRenderer = p_174198_.getItemRenderer();
      this.shadowRadius = 0.15F;
      this.shadowStrength = 0.75F;
   }

   @Override
   public ResourceLocation getTextureLocation(TimedNoiseMakerBomb p_114482_) {
      return TextureAtlas.LOCATION_BLOCKS;
   }

   public void render(TimedNoiseMakerBomb p_115036_, float p_115037_, float p_115038_, PoseStack p_115039_, MultiBufferSource p_115040_, int p_115041_) {
      if (p_115036_.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(p_115036_) < 12.25D)) {
         p_115039_.pushPose();
         p_115039_.scale(1, 1, 1);
         p_115039_.translate(0,0.25,0);
         this.itemRenderer.renderStatic(new ItemStack(SASItems.TIMED_NOISE_MAKER_BOMB.get()), ItemDisplayContext.GROUND, 15728880, OverlayTexture.NO_OVERLAY, p_115039_, p_115040_, p_115036_.level(), p_115036_.getId());
         p_115039_.popPose();
         super.render(p_115036_, p_115037_, p_115038_, p_115039_, p_115040_, p_115041_);
      }
   }
}