package de.nulldrei.saintsandsinners.item.combat.melee;

import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.extensions.IForgePlayer;

import java.util.UUID;

public class EsteemedMortal extends SwordItem {

    private HitResult hitResult;

    public EsteemedMortal(Tier p_43269_, int p_43270_, float p_43271_, Item.Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    @Override
    public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_) {
        if(!p_41428_.isClientSide && p_41429_ instanceof Player player) {
            System.out.println("From ohio");
            player.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_REACH.get()).setBaseValue(4);
        }
        super.onUseTick(p_41428_, p_41429_, p_41430_, p_41431_);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        AttributeModifier rangeModifier = new AttributeModifier(UUID.fromString("556E1665-5bf6-4d07-9c0f-22b3512c1494"), "SAS Esteemed Mortal extended reach", 0.5D, AttributeModifier.Operation.fromValue(AttributeModifier.Operation.MULTIPLY_BASE.ordinal()));
        if(!player.level().isClientSide()) {
            if(player.getMainHandItem().getItem() == SASItems.ESTEEMED_MORTAL.get()) {
                if(!player.getAttribute(ForgeMod.ENTITY_REACH.get()).hasModifier(rangeModifier)) {
                    player.getAttribute(ForgeMod.ENTITY_REACH.get()).addTransientModifier(rangeModifier);
                }
            } else {
                player.getAttribute(ForgeMod.ENTITY_REACH.get()).removeModifier(UUID.fromString("556E1665-5bf6-4d07-9c0f-22b3512c1494"));
            }
        }
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }
}
