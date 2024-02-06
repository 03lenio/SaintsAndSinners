package de.nulldrei.saintsandsinners.item.stuff;

import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.SaintsAndSinners;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BoxOfStuff extends Item {

    protected final ResourceLocation lootTableLocation;
    public BoxOfStuff(Properties p_41383_, ResourceLocation lootTableLocation) {
        super(p_41383_);
        this.lootTableLocation = lootTableLocation;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext p_41427_) {
        LootTable lootTable;
        if (!p_41427_.getLevel().isClientSide()) {
            lootTable = p_41427_.getLevel().getServer().getLootData().getLootTable(getLootTableLocation());
            LootParams lootparams = (new LootParams.Builder((ServerLevel)p_41427_.getLevel())).withParameter(LootContextParams.ORIGIN, p_41427_.getPlayer().position()).withParameter(LootContextParams.THIS_ENTITY, p_41427_.getPlayer()).create(LootContextParamSets.GIFT);
            List<ItemStack> list = lootTable.getRandomItems(lootparams);
            for(ItemStack itemStack : list) {
                if(!SASUtil.isInventoryFull(Objects.requireNonNull(p_41427_.getPlayer()))) {
                    p_41427_.getPlayer().getInventory().add(itemStack);
                } else {
                    BlockState toDropOn = p_41427_.getPlayer().getFeetBlockState();
                    Block.popResource(p_41427_.getLevel(), p_41427_.getPlayer().blockPosition(), itemStack);
                }
            }
            p_41427_.getLevel().playSound(null, p_41427_.getClickedPos(), SoundEvents.ITEM_BREAK, SoundSource.BLOCKS);
            p_41427_.getItemInHand().shrink(1);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    public ResourceLocation getLootTableLocation() {
        return lootTableLocation;
    }

}
