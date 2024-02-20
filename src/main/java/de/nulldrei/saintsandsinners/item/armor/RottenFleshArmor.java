package de.nulldrei.saintsandsinners.item.armor;

import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.DistExecutor;

public class RottenFleshArmor extends ArmorItem {


    public RottenFleshArmor(ArmorMaterial material, ArmorItem.Type type, Properties properties) {
        super(material, type, properties.fireResistant());
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        if(!level.isClientSide() && !player.isSpectator() && !player.isCreative()) {
            if(SASUtil.doesPlayerWearRottenFleshArmor(player)) {
                int oddsDamageArmor = level.random.nextInt(15);
                if(player.isSprinting() || level.isRaining()) {
                    for(ItemStack armorPiece : player.getArmorSlots()) {
                        if(oddsDamageArmor == 0) {
                            armorPiece.hurtAndBreak(1, player, (p_41300_) -> {
                                p_41300_.broadcastBreakEvent(player.getUsedItemHand());
                            });
                            if(armorPiece.getDamageValue() == 0) {
                                level.playSound(null, player.blockPosition(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS);
                            }
                        }

                    }
                }

            }
        }
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
    }


}
