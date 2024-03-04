package de.nulldrei.saintsandsinners.behaviors;

import de.nulldrei.saintsandsinners.entity.projectile.NailBomb;
import de.nulldrei.saintsandsinners.entity.projectile.StickyProximityBomb;
import de.nulldrei.saintsandsinners.entity.projectile.ThrownTimedNoiseMakerBomb;
import de.nulldrei.saintsandsinners.entity.projectile.arrow.ExplosiveArrow;
import de.nulldrei.saintsandsinners.entity.projectile.arrow.LureArrow;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.Util;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

import javax.annotation.Nonnull;

public class SASDispenseBehavior {

        public static void registerDispenseBehaviors() {
            DispenserBlock.registerBehavior(SASItems.EXPLOSIVE_ARROW.get(), new AbstractProjectileDispenseBehavior() {
                @Nonnull
                protected Projectile getProjectile(@Nonnull Level level, @Nonnull Position position, @Nonnull ItemStack stack) {
                    AbstractArrow arrow = new ExplosiveArrow(level, position.x(), position.y(), position.z());
                    arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
                    return arrow;
                }
            });
            DispenserBlock.registerBehavior(SASItems.LURE_ARROW.get(), new AbstractProjectileDispenseBehavior() {
                @Nonnull
                protected Projectile getProjectile(@Nonnull Level level, @Nonnull Position position, @Nonnull ItemStack stack) {
                    AbstractArrow arrow = new LureArrow(level, position.x(), position.y(), position.z());
                    arrow.pickup = AbstractArrow.Pickup.ALLOWED;
                    return arrow;
                }
            });
            DispenserBlock.registerBehavior(SASItems.NAIL_BOMB.get(), new AbstractProjectileDispenseBehavior() {
                protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                    return Util.make(new NailBomb(level, position.x(), position.y(), position.z()), (p_123474_) -> {
                        p_123474_.setItem(stack);
                    });
                }
            });
            DispenserBlock.registerBehavior(SASItems.STICKY_PROXIMITY_BOMB.get(), new AbstractProjectileDispenseBehavior() {
                protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                    return Util.make(new StickyProximityBomb(level, position.x(), position.y(), position.z()), (p_123474_) -> {
                        p_123474_.setItem(stack);
                    });
                }
            });
            DispenserBlock.registerBehavior(SASItems.TIMED_NOISE_MAKER_BOMB.get(), new AbstractProjectileDispenseBehavior() {
                protected Projectile getProjectile(Level level, Position position, ItemStack stack) {
                    return Util.make(new ThrownTimedNoiseMakerBomb(level, position.x(), position.y(), position.z()), (p_123474_) -> {
                        p_123474_.setItem(stack);
                    });
                }
            });
        }
}

