package de.nulldrei.saintsandsinners.item.skull.render;

import com.mojang.blaze3d.vertex.PoseStack;
import de.nulldrei.saintsandsinners.block.skull.SASSkullBlock;
import de.nulldrei.saintsandsinners.block.skull.render.SASSkullBlockRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class SASSkullItemRenderer extends BlockEntityWithoutLevelRenderer {
    private final Map<SkullBlock.Type, SkullModelBase> skullModels;

    private static final SASSkullItemRenderer instance =
            new SASSkullItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(),
            Minecraft.getInstance().getEntityModels());

    public static SASSkullItemRenderer getInstance() {
        return instance;
    }

    public SASSkullItemRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher, EntityModelSet entityModelSet) {
        super(blockEntityRenderDispatcher, entityModelSet);
        this.skullModels = SASSkullBlockRenderer.createSkullRenderers(entityModelSet);
    }

    @Override
    public void renderByItem(ItemStack itemStack, ItemDisplayContext itemDisplayContext,
                             PoseStack poseStack, MultiBufferSource multiBufferSource,
                             int light, int overlay) {
        Item item = itemStack.getItem();
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof SASSkullBlock effectSkullBlock) {
                SkullBlock.Type type = effectSkullBlock.getType();
                SkullModelBase skullmodelbase = this.skullModels.get(type);
                RenderType rendertype = SkullBlockRenderer.getRenderType(type, null);
                SASSkullBlockRenderer.renderSkull(null, 180.0F, 0.0F, poseStack, multiBufferSource, light, skullmodelbase, rendertype);

            }
        }
    }
}