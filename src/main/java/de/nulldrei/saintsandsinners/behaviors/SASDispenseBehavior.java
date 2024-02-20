package de.nulldrei.saintsandsinners.behaviors;

import de.nulldrei.saintsandsinners.entity.projectile.ExplosiveArrow;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
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
        }
}

