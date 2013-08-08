package backpack.gui;

import net.minecraft.inventory.IInventory;
import backpack.inventory.ContainerBackpack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBackpack extends GuiAdvanced {
    protected ContainerBackpack container;

    public GuiBackpack(IInventory inventoryPlayer, IInventory inventoryBackpack) {
        super(new ContainerBackpack(inventoryPlayer, inventoryBackpack, null));

        container = (ContainerBackpack) inventorySlots;

        ySize = TOPSPACING + container.top.ySize + container.bottom.ySize + container.hotbar.ySize + BOTTOMSPACING;
    }

    @Override
    public void initGui() {
        super.initGui();

        container.top.initGui(guiLeft, guiTop);
        container.bottom.initGui(guiLeft, guiTop);
        container.hotbar.initGui(guiLeft, guiTop);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        container.top.setTextOffset(6);
        container.bottom.setTextOffset(13 + container.top.ySize);

        container.top.drawForegroundLayer(fontRenderer, x, y);
        container.bottom.drawForegroundLayer(fontRenderer, x, y);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        drawTopBorder();
        drawBottomBorder();

        // backpack
        container.top.drawBackgroundLayer(f, x, y);
        // inventory
        container.bottom.drawBackgroundLayer(f, x, y);
        // hotbar
        container.hotbar.drawBackgroundLayer(f, x, y);
    }
}