package backpack.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import backpack.inventory.ContainerWorkbenchBackpack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiWorkbenchBackpack extends GuiAdvanced {
    private ContainerWorkbenchBackpack container;

    public GuiWorkbenchBackpack(InventoryPlayer inventoryPlayer, IInventory inventoryBackpack) {
        super(new ContainerWorkbenchBackpack(inventoryPlayer, inventoryBackpack, null));

        container = (ContainerWorkbenchBackpack) inventorySlots;

        ySize = TOPSPACING + container.workbench.ySize + container.backpack.ySize + container.player.ySize + container.hotbar.ySize + BOTTOMSPACING;
    }

    @Override
    public void initGui() {
        super.initGui();
        container.workbench.initGui(guiLeft, guiTop);
        container.backpack.initGui(guiLeft, guiTop);
        container.player.initGui(guiLeft, guiTop);
        container.hotbar.initGui(guiLeft, guiTop);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        container.workbench.setTextOffset(6);
        container.player.setTextOffset(19 + container.workbench.ySize + container.backpack.ySize);

        container.workbench.drawForegroundLayer(fontRenderer, x, y);
        container.player.drawForegroundLayer(fontRenderer, x, y);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        drawTopBorder();
        drawBottomBorder();

        container.workbench.drawBackgroundLayer(f, x, y);
        container.backpack.drawBackgroundLayer(f, x, y);
        container.player.drawBackgroundLayer(f, x, y);
        container.hotbar.drawBackgroundLayer(f, x, y);
    }
}