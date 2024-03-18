package de.nulldrei.saintsandsinners.block.utility;

import de.nulldrei.saintsandsinners.SASUtil;
import de.nulldrei.saintsandsinners.item.SASItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.commands.GiveCommand;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RecyclingBinBlock extends HorizontalDirectionalBlock {

    public static final VoxelShape vs = Block.box(-1, 0, 2, 17, 16, 14);


    @Override
    public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(!p_60504_.isClientSide()) {
            if(!(p_60506_.getItemInHand(p_60507_).getItem() instanceof AirItem)) {
                if(isScrapItem(p_60506_.getItemInHand(p_60507_))) return InteractionResult.FAIL;
                ItemStack itemInHand = p_60506_.getItemInHand(p_60507_);
                List<CraftingRecipe> recipes = p_60504_.getRecipeManager().getAllRecipesFor(RecipeType.CRAFTING);
                boolean returnedItem = false;
                ArrayList<ItemStack> alreadySeen = new ArrayList<ItemStack>();
                int ingredientCounter = 0;
                for (CraftingRecipe recipe : recipes) {
                    if (recipe.getResultItem(p_60504_.registryAccess()).getItem() == itemInHand.getItem()) {
                        for (Ingredient ingredient : recipe.getIngredients()) {
                            for (ItemStack itemStack : ingredient.getItems()) {
                                if(ingredientCounter > recipe.getIngredients().size()) break;
                                if(itemStack.getItem().toString().contains("iron")) {
                                    addItem(p_60506_, new ItemStack(Items.IRON_NUGGET), p_60505_);
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().contains("gold")) {
                                    addItem(p_60506_, new ItemStack(Items.GOLD_NUGGET), p_60505_);
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().contains("diamond")) {
                                    addItem(p_60506_, new ItemStack(SASItems.DIAMOND_SHARD.get()), p_60505_);
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().contains("netherite")) {
                                    addItem(p_60506_, new ItemStack(SASItems.NETHERITE_FRAGMENT.get()), p_60505_);
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().contains("copper")) {
                                    addItem(p_60506_, new ItemStack(SASItems.COPPER_NUGGET.get()), p_60505_);
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().contains("wool")) {
                                    addItem(p_60506_, new ItemStack(Items.STRING), p_60505_);
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().equals("gunpowder")) {
                                    addItem(p_60506_, new ItemStack(Items.GUNPOWDER), p_60505_);
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().contains("wood") || itemStack.getItem().toString().contains("plank")) {
                                    addItem(p_60506_, new ItemStack(Items.STICK), p_60505_);
                                    //System.out.println(itemStack.getItem().toString());
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().contains("bone")) {
                                    addItem(p_60506_, new ItemStack(Items.BONE), p_60505_);
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().contains("glass")) {
                                    addItem(p_60506_, new ItemStack(SASItems.GLASS_SHARD.get()), p_60505_);
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().contains("leather")) {
                                    addItem(p_60506_, new ItemStack(Items.LEATHER), p_60505_);
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().contains("redstone")) {
                                    addItem(p_60506_, new ItemStack(Items.REDSTONE), p_60505_);
                                    returnedItem = true;
                                }
                                else if(itemStack.getItem().toString().contains("sugar")) {
                                    addItem(p_60506_, new ItemStack(Items.SUGAR), p_60505_);
                                    returnedItem = true;
                                }
                                if(returnedItem) {
                                    ingredientCounter++;
                                }

                            }
                        }
                        if(returnedItem) {
                            p_60504_.playSound(null, p_60505_, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS);
                            itemInHand.shrink(1);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }

            }

        }
        p_60504_.playSound(null, p_60505_, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS);
        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        return this.defaultBlockState().setValue(FACING, p_49820_.getHorizontalDirection().getOpposite());
    }

    public void addItem(Player player, ItemStack item, BlockPos binBlockPos) {
        if(!SASUtil.isInventoryFull(player)) {
            player.getInventory().add(item);
        } else {
            popResource(player.level(), binBlockPos.above(), item);
        }
    }

    public boolean isScrapItem(ItemStack item) {
        return (item.getItem() == Items.IRON_NUGGET || item.getItem() == Items.GOLD_NUGGET || item.getItem() == SASItems.NETHERITE_FRAGMENT.get() || item.getItem() == SASItems.COPPER_NUGGET.get() || item.getItem() == SASItems.DIAMOND_SHARD.get() || item.getItem() == Items.REDSTONE || item.getItem() == Items.GUNPOWDER || item.getItem() == Items.STICK || item.getItem() == Items.STRING || item.getItem() == Items.GLASS || item.getItem() == Items.LEATHER || item.getItem() == Items.SUGAR || item.getItem() == Items.BONE);
    }

    public BlockState rotate(BlockState p_60215_, Rotation p_60216_) {
        return p_60215_.setValue(FACING, p_60216_.rotate(p_60215_.getValue(FACING)));
    }



    @Override
    public BlockState mirror(BlockState p_54122_, Mirror p_54123_) {
        return p_54122_.rotate(p_54123_.getRotation(p_54122_.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        Direction direction = p_60555_.getValue(FACING);
        switch (direction) {
            case EAST:
            default:
                return rotateShape(Direction.SOUTH, Direction.EAST, vs);
            case SOUTH:
                return rotateShape(Direction.SOUTH, Direction.NORTH, vs);
            case WEST:
                return rotateShape(Direction.EAST, Direction.NORTH, vs);
            case NORTH:
                return vs;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.MODEL;
    }

    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
        VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

        int times = (to.ordinal() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++) {
            buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            buffer[0] = buffer[1];
            buffer[1] = Shapes.empty();
        }

        return buffer[0];
    }
    public RecyclingBinBlock(Properties p_49795_) {
        super(p_49795_);
    }
}
