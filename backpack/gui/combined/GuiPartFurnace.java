package backpack.gui.combined;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.tileentity.TileEntityFurnace;
import backpack.inventory.ContainerAdvanced;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPartFurnace extends GuiPart {
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private TileEntityFurnace furnace;

    public GuiPartFurnace(ContainerAdvanced container, IInventory inventory, int inventoryRows) {
        super(container, inventory, inventoryRows);
        ySize = 54;
        furnace = (TileEntityFurnace) inventory;
    }

    @Override
    public void addSlots() {
        int offset = offsetY + topSpacing + 1;

        firstSlot = container.inventorySlots.size();

        container.addSlot(new Slot(inventory, 0, 56, offset));
        container.addSlot(new Slot(inventory, 1, 56, offset + 36));
        container.addSlot(new SlotFurnace(Minecraft.getMinecraft().thePlayer, inventory, 2, 116, offset + 18));

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
    public void addCraftingToCrafters(ICrafting par1iCrafting) {
        par1iCrafting.sendProgressBarUpdate(container, 0, furnace.furnaceCookTime);
        par1iCrafting.sendProgressBarUpdate(container, 1, furnace.furnaceBurnTime);
        par1iCrafting.sendProgressBarUpdate(container, 2, furnace.currentItemBurnTime);
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
    public void updateProgressBar(int par1, int par2) {
        if(par1 == 0) {
            furnace.furnaceCookTime = par2;
        }

        if(par1 == 1) {
            furnace.furnaceBurnTime = par2;
        }

        if(par1 == 2) {
            furnace.currentItemBurnTime = par2;
        }
    }
}