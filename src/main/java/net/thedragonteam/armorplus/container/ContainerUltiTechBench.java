package net.thedragonteam.armorplus.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.thedragonteam.armorplus.api.crafting.base.BaseCraftingManager;
import net.thedragonteam.armorplus.api.crafting.base.BaseSlotCrafting;
import net.thedragonteam.armorplus.container.base.ContainerBenchBase;
import net.thedragonteam.armorplus.container.base.InventoryCraftingImproved;
import net.thedragonteam.armorplus.tileentity.TileEntityUltiTechBench;

import java.util.stream.IntStream;

/**
 * @author Sokratis Fotkatzikis - TheDragonTeam
 */
public class ContainerUltiTechBench extends ContainerBenchBase {

    private static final int ITEM_BOX = 18;
    private static final int RECIPE_SLOTS = 50;
    private static final int RECIPE_SIZE = 7;
    private static final int RECIPE_SIZE_TOTAL = 49;
    private static final int ROW_SLOTS = 9;
    private static final int FULL_INVENTORY_SLOTS = RECIPE_SLOTS + 36;
    private static final int MAIN_INVENTORY_SLOTS = RECIPE_SLOTS + 27;
    private final World world;
    public InventoryCraftingImproved craftMatrix = new InventoryCraftingImproved(this, 7, 7);
    public IInventory craftResult = new InventoryCraftResult();

    public ContainerUltiTechBench(InventoryPlayer playerInventory, TileEntityUltiTechBench tile) {
        super(tile, RECIPE_SLOTS, MAIN_INVENTORY_SLOTS, FULL_INVENTORY_SLOTS);
        this.world = tile.getWorld();
        this.addSlotToContainer(new BaseSlotCrafting(BaseCraftingManager.getUTBInstance(), playerInventory.player, this.craftMatrix, this.craftResult, 0, 168, 71));

        for (int yIndex = 0; yIndex < RECIPE_SIZE; ++yIndex)
            for (int xIndex = 0; xIndex < RECIPE_SIZE; ++xIndex)
                this.addSlotToContainer(new Slot(this.craftMatrix, xIndex + yIndex * RECIPE_SIZE, 12 + xIndex * ITEM_BOX, 17 + yIndex * ITEM_BOX));

        for (int height = 0; height < 3; ++height)
            for (int width = 0; width < ROW_SLOTS; ++width)
                this.addSlotToContainer(new Slot(playerInventory, width + height * 9 + 9, 8 + width * ITEM_BOX, 156 + height * ITEM_BOX));

        IntStream.range(0, ROW_SLOTS).mapToObj(index -> new Slot(playerInventory, index, 8 + index * ITEM_BOX, 214)).forEachOrdered(this::addSlotToContainer);

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        this.craftResult.setInventorySlotContents(0, BaseCraftingManager.getUTBInstance().findMatchingRecipe(this.craftMatrix, this.world));
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        onContainerClosed(playerIn, this.world.isRemote, RECIPE_SIZE_TOTAL, this.craftMatrix);
    }

    /**
     * Called to determine if the current slot is valid for the stack merging (double-click) code. The stack passed in
     * is null for the initial slot that was double-clicked.
     */
    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
}