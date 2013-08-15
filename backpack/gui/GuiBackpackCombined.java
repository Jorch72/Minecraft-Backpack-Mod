package backpack.gui;

import net.minecraft.inventory.IInventory;
import backpack.inventory.container.ContainerBackpackCombined;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBackpackCombined extends GuiAdvanced<ContainerBackpackCombined> {
    public GuiBackpackCombined(IInventory inventoryPlayer, IInventory otherInventory, IInventory inventoryBackpack) {
        super(new ContainerBackpackCombined(inventoryPlayer, otherInventory, inventoryBackpack, null));

        container = (ContainerBackpackCombined) inventorySlots;

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
        if(container.parts.get(1).ySize > 0) {
            container.parts.get(1).drawForegroundLayer(fontRenderer, x, y);
        }
    }
}