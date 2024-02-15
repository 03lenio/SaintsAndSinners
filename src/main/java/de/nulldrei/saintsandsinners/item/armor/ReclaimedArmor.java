package de.nulldrei.saintsandsinners.item.armor;

import de.nulldrei.saintsandsinners.SaintsAndSinners;
import de.nulldrei.saintsandsinners.armor.model.ReclaimedMaskModel;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.DistExecutor;

import java.util.EnumMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReclaimedArmor extends ArmorItem {



    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    private final LazyLoadedValue<HumanoidModel<?>> model;

    public ReclaimedArmor(ArmorMaterial material, ArmorItem.Type type, Properties properties) {
        super(material, type, properties.fireResistant());
        this.model = DistExecutor.unsafeRunForDist(() -> () -> new LazyLoadedValue<>(() -> this.provideArmorModelForSlot(type)),
                () -> () -> null);
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return SaintsAndSinners.MODID + ":textures/models/armor/reclaimed_mask.png";
    }

    @OnlyIn(Dist.CLIENT)
    public HumanoidModel<?> provideArmorModelForSlot(ArmorItem.Type type) {
        return new ReclaimedMaskModel(Minecraft.getInstance().getEntityModels().bakeLayer(new ModelLayerLocation(new ResourceLocation(SaintsAndSinners.MODID, "main"), "reclaimed_mask")), type);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {

            @Override
            public HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, HumanoidModel<?> _default) {
                return model.get();
            }
        });
    }


}