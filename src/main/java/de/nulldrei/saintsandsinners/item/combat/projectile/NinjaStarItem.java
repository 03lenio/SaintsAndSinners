package de.nulldrei.saintsandsinners.item.combat.projectile;

import de.nulldrei.saintsandsinners.entity.projectile.NailBomb;
import de.nulldrei.saintsandsinners.entity.projectile.NinjaStar;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NinjaStarItem extends Item {

    public NinjaStarItem(Item.Properties p_43235_) {
        super(p_43235_);
    }

    public InteractionResultHolder<ItemStack> use(Level p_43142_, Player p_43143_, InteractionHand p_43144_) {
        ItemStack itemstack = p_43143_.getItemInHand(p_43144_);
        if (!p_43142_.isClientSide()) {
            NinjaStar ninjaStar = new NinjaStar(p_43142_, p_43143_);
            ninjaStar.setItem(itemstack);
            ninjaStar.shootFromRotation(p_43143_, p_43143_.getXRot(), p_43143_.getYRot(), 0.0F, 1.5F, 1.0F);
            p_43142_.addFreshEntity(ninjaStar);
            CriteriaTriggers.USING_ITEM.trigger((ServerPlayer) p_43143_, itemstack);
        }

        p_43143_.awardStat(Stats.ITEM_USED.get(this));
        if (!p_43143_.getAbilities().instabuild) {
            itemstack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemstack, p_43142_.isClientSide());
    }
}
