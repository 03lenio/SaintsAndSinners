package de.nulldrei.saintsandsinners.data;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.jetbrains.annotations.Nullable;

public class SASDamageTypes
{
    public static final ResourceKey<DamageType> NINJA_STAR = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SaintsAndSinners.MODID, "ninja_star"));

    public static class Sources
    {
        private static Holder.Reference<DamageType> getHolder(RegistryAccess access, ResourceKey<DamageType> damageTypeKey)
        {
            return access.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeKey);
        }

        private static DamageSource source(RegistryAccess access, ResourceKey<DamageType> damageTypeKey, @Nullable Entity directEntity, @Nullable Entity causingEntity)
        {
            return new DamageSource(getHolder(access, damageTypeKey), directEntity, causingEntity);
        }

        public static DamageSource projectile(RegistryAccess access, Projectile projectile, LivingEntity entity)
        {
            return source(access, NINJA_STAR, projectile, entity);
        }

        public static DamageSource projectileNoPlayer(RegistryAccess access, Projectile projectile)
        {
            return source(access, NINJA_STAR, projectile, null);
        }
    }
}