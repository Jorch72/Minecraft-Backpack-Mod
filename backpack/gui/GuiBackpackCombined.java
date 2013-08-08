package backpack.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.inventory.IInventory;
import backpack.inventory.ContainerBackpackCombined;

@SideOnly(Side.CLIENT)
public class GuiBackpackCombined extends GuiAdvanced {
    protected ContainerBackpackCombined container;

    public GuiBackpackCombined(IInventory inventoryPlayer, IInventory otherInventory, IInventory inventoryBackpack) {
        super(new ContainerBackpackCombined(inventoryPlayer, otherInventory, inventoryBackpack, null));

        container = (ContainerBackpackCombined) inventorySlots;

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
        if(container.bottom.ySize > 0) {
            container.bottom.drawForegroundLayer(fontRenderer, x, y);
        }
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        drawTopBorder();
        drawBottomBorder();

        // top container (chest/furnance/hopper/dropper/dispenser)
        container.top.drawBackgroundLayer(f, x, y);

        // bottom container (backpack)
        container.bottom.drawBackgroundLayer(f, x, y);

        // hotbar
        container.hotbar.drawBackgroundLayer(f, x, y);
    }
}