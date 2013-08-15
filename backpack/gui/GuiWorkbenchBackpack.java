package backpack.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import backpack.inventory.container.ContainerWorkbenchBackpack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiWorkbenchBackpack extends GuiAdvanced<ContainerWorkbenchBackpack> {
    public GuiWorkbenchBackpack(InventoryPlayer inventoryPlayer, IInventory inventoryBackpack) {
        super(new ContainerWorkbenchBackpack(inventoryPlayer, inventoryBackpack, null));

        container = (ContainerWorkbenchBackpack) inventorySlots;

        ySize = TOPSPACING + container.calculatePartHeight() + BOTTOMSPACING;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        container.parts.get(0).setTextOffset(6);
        container.parts.get(2).setTextOffset(19 + container.parts.get(0).ySize + container.parts.get(1).ySize);

        container.parts.get(0).drawForegroundLayer(fontRenderer, x, y);
        container.parts.get(2).drawForegroundLayer(fontRenderer, x, y);
    }
}