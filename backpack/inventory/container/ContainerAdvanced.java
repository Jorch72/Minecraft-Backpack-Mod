package backpack.inventory.container;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import backpack.Backpack;
import backpack.gui.parts.GuiPart;
import backpack.handler.PacketHandlerBackpack;
import backpack.inventory.IInventoryBackpack;
import backpack.inventory.slot.SlotScrolling;
import backpack.misc.Constants;
import backpack.util.BackpackUtil;
import backpack.util.NBTUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ContainerAdvanced extends Container {
    protected final ItemStack openedBackpack;
    protected final IInventory lowerInventory;
    protected final IInventory upperInventory;
    public final int upperInventoryRows;
    public final int lowerInventoryRows;
    public ArrayList<GuiPart> parts = new ArrayList<GuiPart>();

    public ContainerAdvanced(IInventory inventoryLower, IInventory inventoryUpper, ItemStack backpack) {
        if(inventoryLower != null) {
            lowerInventory = inventoryLower;
            lowerInventory.openChest();
            lowerInventoryRows = BackpackUtil.getInventoryRows(lowerInventory);
        } else {
            lowerInventory = new InventoryBasic("placebo", false, 128);
            lowerInventoryRows = 0;
        }
        if(inventoryUpper != null) {
            upperInventory = inventoryUpper;
            upperInventory.openChest();
            upperInventoryRows = BackpackUtil.getInventoryRows(upperInventory);
        } else {
            upperInventory = new InventoryBasic("placebo", false, 128);
            upperInventoryRows = 0;
        }

        if(lowerInventory instanceof IInventoryBackpack || lowerInventory instanceof InventoryEnderChest) {
            openedBackpack = backpack;
        } else if(upperInventory instanceof IInventoryBackpack || upperInventory instanceof InventoryEnderChest) {
            openedBackpack = backpack;
        } else {
            openedBackpack = null;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        ItemStack itemStack = null;
        if(openedBackpack != null && NBTUtil.getBoolean(openedBackpack, Constants.WORN_BACKPACK_OPEN)) {
            itemStack = Backpack.playerHandler.getBackpack(player);
        } else if(player.getCurrentEquippedItem() != null) {
            itemStack = player.getCurrentEquippedItem();
        }
        if(BackpackUtil.UUIDEquals(itemStack, openedBackpack) || BackpackUtil.isEnderBackpack(itemStack)) {
            return true;
        }
        return false;
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        lowerInventory.closeChest();
        upperInventory.closeChest();

        if(!player.worldObj.isRemote) {
            ItemStack itemStack = Backpack.playerHandler.getBackpack(player);
            if(itemStack != null) {
                if(NBTUtil.hasTag(itemStack, Constants.WORN_BACKPACK_OPEN)) {
                    NBTUtil.removeTag(itemStack, Constants.WORN_BACKPACK_OPEN);
                    Backpack.playerHandler.setBackpack(player, itemStack);
                }
            }
        }
    }

    @Override
    protected boolean mergeItemStack(ItemStack sourceStack, int firstSlot, int lastSlot, boolean backwards) {
        boolean result = false;
        int currentSlotIndex = firstSlot;

        if(backwards) {
            currentSlotIndex = lastSlot - 1;
        }

        Slot slot;
        ItemStack slotStack;

        if(sourceStack.isStackable()) {
            while(sourceStack.stackSize > 0 && (!backwards && currentSlotIndex < lastSlot || backwards && currentSlotIndex >= firstSlot)) {
                slot = (Slot) inventorySlots.get(currentSlotIndex);
                if(!(slot instanceof SlotScrolling && ((SlotScrolling) slot).isDisabled())) {
                    slotStack = slot.getStack();

                    if(slotStack != null && slotStack.itemID == sourceStack.itemID && (!sourceStack.getHasSubtypes() || sourceStack.getItemDamage() == slotStack.getItemDamage())
                            && ItemStack.areItemStackTagsEqual(sourceStack, slotStack)) {
                        int l = slotStack.stackSize + sourceStack.stackSize;

                        if(l <= sourceStack.getMaxStackSize()) {
                            sourceStack.stackSize = 0;
                            slotStack.stackSize = l;
                            slot.onSlotChanged();
                            result = true;
                        } else if(slotStack.stackSize < sourceStack.getMaxStackSize()) {
                            sourceStack.stackSize -= sourceStack.getMaxStackSize() - slotStack.stackSize;
                            slotStack.stackSize = sourceStack.getMaxStackSize();
                            slot.onSlotChanged();
                            result = true;
                        }
                    }
                }

                if(backwards) {
                    --currentSlotIndex;
                } else {
                    ++currentSlotIndex;
                }
            }
        }

        if(sourceStack.stackSize > 0) {
            if(backwards) {
                currentSlotIndex = lastSlot - 1;
            } else {
                currentSlotIndex = firstSlot;
            }

            while(!backwards && currentSlotIndex < lastSlot || backwards && currentSlotIndex >= firstSlot) {
                slot = (Slot) inventorySlots.get(currentSlotIndex);
                if(!(slot instanceof SlotScrolling && ((SlotScrolling) slot).isDisabled())) {
                    slotStack = slot.getStack();

                    if(slotStack == null) {
                        slot.putStack(sourceStack.copy());
                        slot.onSlotChanged();
                        sourceStack.stackSize = 0;
                        result = true;
                        break;
                    }
                }

                if(backwards) {
                    --currentSlotIndex;
                } else {
                    ++currentSlotIndex;
                }
            }
        }

        return result;
    }

    public void addSlot(Slot slot) {
        addSlotToContainer(slot);
    }

    public List<ICrafting> getCrafters() {
        return crafters;
    }

    @SideOnly(Side.CLIENT)
    public void sendScrollbarToServer(GuiPart guiPart, int offset) {
        for(int i = 0; i < parts.size(); i++) {
            if(parts.get(i) == guiPart) {
                PacketHandlerBackpack.sendScrollbarPositionToServer(i, offset);
                updateSlots(i, offset, false);
                break;
            }
        }
    }

    public void updateSlots(int guiPartIndex, int offset, boolean isServer) {
        int slotNumber = parts.get(guiPartIndex).firstSlot;
        int inventoryRows = parts.get(guiPartIndex).inventoryRows;
        int inventoryCols = parts.get(guiPartIndex).inventoryCols;

        for(int row = 0; row < inventoryRows; ++row) {
            for(int col = 0; col < inventoryCols; ++col) {
                int slotIndex = col + (row + offset) * inventoryCols;

                SlotScrolling slot = (SlotScrolling) inventorySlots.get(slotNumber);

                if(slotIndex < slot.inventory.getSizeInventory()) {
                    if(isServer) {
                        slot.setSlotIndex(slotIndex);
                    }
                    slot.setDisabled(false);
                } else {
                    slot.setDisabled(true);
                }

                slotNumber++;
            }
        }
        if(isServer) {
            detectAndSendChanges();
        }
    }

    public int calculatePartHeight() {
        int height = 0;
        for(GuiPart guiPart : parts) {
            height += guiPart.ySize;
        }
        return height;
    }
}