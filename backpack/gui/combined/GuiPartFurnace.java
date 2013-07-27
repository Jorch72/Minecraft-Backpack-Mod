package backpack.gui.combined;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;
import backpack.inventory.ContainerBackpackCombined;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiPartFurnace extends GuiPart {
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;
    private TileEntityFurnace furnace;

    public GuiPartFurnace(IInventory inventory, int inventoryRows) {
        super(inventory, inventoryRows);
        ySize = 70;
        background = new ResourceLocation("textures/gui/container/furnace.png");
        furnace = (TileEntityFurnace) inventory;
    }

    @Override
    public void addSlots(ContainerBackpackCombined container) {
        container.addSlot(new Slot(inventory, 0, 56, 17));
        container.addSlot(new Slot(inventory, 1, 56, 53));
        container.addSlot(new SlotFurnace(Minecraft.getMinecraft().thePlayer, inventory, 2, 116, 35));
    }

    @Override
    public void drawForegroundLayer(FontRenderer fontRenderer, int x, int y) {
        String s = inventory.isInvNameLocalized() ? inventory.getInvName() : I18n.func_135053_a(inventory.getInvName());
        fontRenderer.drawString(s, xSize / 2 - fontRenderer.getStringWidth(s) / 2, 6, 0x404040);
    }

    @Override
    public void drawBackgroundLayer(float f, int x, int y) {
        super.drawBackgroundLayer(f, x, y);
        int i1;

        if(furnace.isBurning()) {
            i1 = furnace.getBurnTimeRemainingScaled(12);
            drawTexturedModalRect(guiLeft + 56, guiTop + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        }

        i1 = furnace.getCookProgressScaled(24);
        drawTexturedModalRect(guiLeft + 79, guiTop + 34, 176, 14, i1 + 1, 16);
    }

    @Override
    public void addCraftingToCrafters(Container container, ICrafting par1iCrafting) {
        par1iCrafting.sendProgressBarUpdate(container, 0, furnace.furnaceCookTime);
        par1iCrafting.sendProgressBarUpdate(container, 1, furnace.furnaceBurnTime);
        par1iCrafting.sendProgressBarUpdate(container, 2, furnace.currentItemBurnTime);
    }

    @Override
    public void detectAndSendChanges(ContainerBackpackCombined container) {
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