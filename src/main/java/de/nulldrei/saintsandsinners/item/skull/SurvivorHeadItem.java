package de.nulldrei.saintsandsinners.item.skull;

import de.nulldrei.saintsandsinners.block.skull.SASSkullItem;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PlayerHeadBlock;

public class SurvivorHeadItem extends SASSkullItem {
    public SurvivorHeadItem(Block skull, Block wallSkull, Properties properties, Direction direction) {
        super(skull, wallSkull, properties, direction);
    }

    @Override
    public SoundEvent getSound() {
        return SoundEvents.AMBIENT_NETHER_WASTES_LOOP.get();
    }
}