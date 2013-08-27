package backpack.gui.parts;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.tileentity.TileEntityFurnace;
import backpack.inventory.container.ContainerAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiPartFurnace extends GuiPart {
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private TileEntityFurnace furnace;
    private EntityPlayer player;

    public GuiPartFurnace(ContainerAdvanced container, EntityPlayer player, IInventory inventory, int inventoryRows) {
        super(container, inventory, inventoryRows);
        ySize = 54;
        furnace = (TileEntityFurnace) inventory;
        this.player = player;
    }

    @Override
    public void addSlots() {
        int offset = offsetY + topSpacing + 1;

        firstSlot = container.inventorySlots.size();

        container.addSlot(new Slot(inventory, 0, 56, offset));
        container.addSlot(new Slot(inventory, 1, 56, offset + 36));
        container.addSlot(new SlotFurnace(player, inventory, 2, 116, offset + 18));

        lastSlot = container.inventorySlots.size();
    }

    @Override
    public void drawBackgroundLayer(float f, int x, int y) {
        super.drawBackgroundLayer(f, x, y);

        drawTexturedModalRect(guiLeft + 57, guiTop + 37, 0, 224, 13, 13);
        drawTexturedModalRect(guiLeft + 80, guiTop + 35, 0, 238, 22, 15);

        for(int i = firstSlot; i < lastSlot; i++) {
            Slot slot = (Slot) container.inventorySlots.get(i);
            if(slot instanceof SlotFurnace) {
                drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 5, guiTop + slot.yDisplayPosition - 5, 201, 18, 26, 26);
            } else {
                drawTexturedModalRect(guiLeft + slot.xDisplayPosition - 1, guiTop + slot.yDisplayPosition - 1, 201, 0, 18, 18);
            }
        }

        int barWidth;

        if(furnace.isBurning()) {
            barWidth = furnace.getBurnTimeRemainingScaled(12);
            drawTexturedModalRect(guiLeft + 57, guiTop + 48 - barWidth, 13, 236 - barWidth, 14, barWidth + 2);
        }

        barWidth = furnace.getCookProgressScaled(24);
        drawTexturedModalRect(guiLeft + 80, guiTop + 34, 22, 238, barWidth, 16);
    }

    @Override
    public void addCraftingToCrafters(ICrafting player) {
        player.sendProgressBarUpdate(container, 0, furnace.furnaceCookTime);
        player.sendProgressBarUpdate(container, 1, furnace.furnaceBurnTime);
        player.sendProgressBarUpdate(container, 2, furnace.currentItemBurnTime);
    }

    @Override
    public void detectAndSendChanges() {
        for(int i = 0; i < container.getCrafters().size(); ++i) {
            ICrafting icrafting = container.getCrafters().get(i);

            if(lastCookTime != furnace.furnaceCookTime) {
                icrafting.sendProgressBarUpdate(container, 0, furnace.furnaceCookTime);
            }

            if(lastBurnTime != furnace.furnaceBurnTime) {
                icrafting.sendProgressBarUpdate(container, 1, furnace.furnaceBurnTime);
            }

            if(lastItemBurnTime != furnace.currentItemBurnTime) {
                icrafting.sendProgressBarUpdate(container, 2, furnace.currentItemBurnTime);
            }
        }

        lastCookTime = furnace.furnaceCookTime;
        lastBurnTime = furnace.furnaceBurnTime;
        lastItemBurnTime = furnace.currentItemBurnTime;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
        if(id == 0) {
            furnace.furnaceCookTime = value;
        }

        if(id == 1) {
            furnace.furnaceBurnTime = value;
        }

        if(id == 2) {
            furnace.currentItemBurnTime = value;
        }
    }
}