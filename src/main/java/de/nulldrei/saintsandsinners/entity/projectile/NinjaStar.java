package de.nulldrei.saintsandsinners.entity.projectile;

import de.nulldrei.saintsandsinners.data.SASDamageTypes;
import de.nulldrei.saintsandsinners.entity.SASEntities;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class NinjaStar extends ThrowableItemProjectile {

    public Player shooter;

    public NinjaStar(EntityType<? extends NinjaStar> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
    }

    public NinjaStar(Level p_37399_, LivingEntity p_37400_,  Player shooter) {
        super(SASEntities.NAIL_BOMB.get(), p_37400_, p_37399_);
        this.shooter = shooter;
    }

    public NinjaStar(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(SASEntities.NAIL_BOMB.get(), p_37395_, p_37396_, p_37397_, p_37394_);
        this.shooter = null;
    }

    protected Item getDefaultItem() {
        return SASItems.NAIL_BOMB.get();
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        if(!level().isClientSide()) {
            double d0 = (double)EntityType.ITEM.getHeight() / 2.0D;
            double d1 = (double)p_37258_.getBlockPos().getX() + 0.5D + Mth.nextDouble(level().random, -0.25D, 0.25D);
            double d2 = (double)p_37258_.getBlockPos().getY() + 0.5D + Mth.nextDouble(level().random, -0.25D, 0.25D) - d0;
            double d3 = (double)p_37258_.getBlockPos().getZ() + 0.5D + Mth.nextDouble(level().random, -0.25D, 0.25D);
            ItemEntity itementity = new ItemEntity(level(), d1, d2, d3, new ItemStack(SASItems.NINJA_STAR.get()));
            itementity.setDefaultPickUpDelay();
            level().addFreshEntity(itementity);
            discard();
        }
        super.onHitBlock(p_37258_);
    }

    @Override
    protected void onHitEntity(EntityHitResult p_37259_) {
        if(!level().isClientSide()) {
            DamageSource source;
            if(shooter != null) {
                source = SASDamageTypes.Sources.projectile(level().registryAccess(), this, shooter);
            } else {
                source = SASDamageTypes.Sources.projectileNoPlayer(level().registryAccess(), this);
            }
            p_37259_.getEntity().hurt(source, 3F);
            discard();
        }
        super.onHitEntity(p_37259_);
    }
}
