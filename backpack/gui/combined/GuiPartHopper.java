package backpack.gui.combined;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import backpack.inventory.ContainerBackpackCombined;

public class GuiPartHopper extends GuiPart {

    public GuiPartHopper(IInventory inventory, int inventoryRows) {
        super(inventory, inventoryRows);
        ySize = 37;
        background = new ResourceLocation("textures/gui/container/hopper.png");
        LEFTSPACING = 44;
        TOPSPACING = 20;
    }

    @Override
    public void addSlots(ContainerBackpackCombined container) {
        int x = LEFTSPACING;
        int y = TOPSPACING;
        for(int i = 0; i < inventory.getSizeInventory(); ++i) {
            container.addSlot(new Slot(inventory, i, x, y));
            x += SLOT;
        }
    }

    @Override
    public void drawForegroundLayer(FontRenderer fontRenderer, int x, int y) {
        fontRenderer.drawString(inventory.isInvNameLocalized() ? inventory.getInvName() : I18n.func_135053_a(inventory.getInvName()), 8, 6, 0x404040);
    }
}