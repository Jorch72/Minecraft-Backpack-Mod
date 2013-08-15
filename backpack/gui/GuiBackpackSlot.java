package backpack.gui;

import net.minecraft.inventory.IInventory;
import backpack.inventory.container.ContainerBackpackSlot;

public class GuiBackpackSlot extends GuiAdvanced<ContainerBackpackSlot> {
    public GuiBackpackSlot(IInventory lowerInventory, IInventory upperInventory) {
        super(new ContainerBackpackSlot(lowerInventory, upperInventory));

        container = (ContainerBackpackSlot) inventorySlots;

        ySize = TOPSPACING + container.calculatePartHeight() + BOTTOMSPACING;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        container.parts.get(0).setTextOffset(6);
        container.parts.get(1).setTextOffset(13 + container.parts.get(0).ySize);

        container.parts.get(0).drawForegroundLayer(fontRenderer, x, y);
        container.parts.get(1).drawForegroundLayer(fontRenderer, x, y);
    }
}