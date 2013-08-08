package backpack.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

public class SlotBrewingStandPotion extends Slot {
    public SlotBrewingStandPotion(IInventory inventory, int slot, int x, int y) {
        super(inventory, slot, x, y);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for
     * the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack stack) {
        return canHoldPotion(stack);
    }

    /**
     * Returns the maximum stack size for a given slot (usually the same as
     * getInventoryStackLimit(), but 1 in the case of armor slots)
     */
    @Override
    public int getSlotStackLimit() {
        return 1;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack stack) {
        if(stack.getItem() instanceof ItemPotion && stack.getItemDamage() > 0) {
            player.addStat(AchievementList.potion, 1);
        }

        super.onPickupFromSlot(player, stack);
    }

    /**
     * Returns true if this itemstack can be filled with a potion
     */
    public static boolean canHoldPotion(ItemStack stack) {
        return stack != null && (stack.getItem() instanceof ItemPotion || stack.itemID == Item.glassBottle.itemID);
    }
}
