package de.nulldrei.saintsandsinners.item.combat;

import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DebugStickItem;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

import java.util.Objects;

public class Bottle extends Item {

    private HitResult hitResult;

    public Bottle(Properties p_41383_) {
        super(p_41383_);
    }



    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        Entity camera = Minecraft.getInstance().getCameraEntity();
        assert camera != null;
        hitResult = camera.pick(20.0D, 0.0F, false);
        if (this.hitResult.getType() == HitResult.Type.BLOCK) {
            entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(SASItems.BROKEN_BOTTLE.get()));
            entity.level().playSound(null, entity.blockPosition(), SoundEvents.GLASS_BREAK, SoundSource.BLOCKS);
        } else {
            return false;
        }


        return true;
    }
}
