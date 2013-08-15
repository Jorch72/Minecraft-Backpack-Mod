package backpack.gui;

import net.minecraft.inventory.IInventory;
import backpack.inventory.container.ContainerBackpack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBackpack extends GuiAdvanced<ContainerBackpack> {
    public GuiBackpack(IInventory inventoryPlayer, IInventory inventoryBackpack) {
        super(new ContainerBackpack(inventoryPlayer, inventoryBackpack, null));

        container = (ContainerBackpack) inventorySlots;

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