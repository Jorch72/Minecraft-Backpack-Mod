package backpack.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import backpack.inventory.ContainerBackpackCombined;

public class GuiBackpackCombined extends GuiContainer {
    protected IInventory upperInventory;
    protected IInventory lowerInventory;
    protected int lowerGuiRows;
    protected ResourceLocation background;
    protected ContainerBackpackCombined container;

    public GuiBackpackCombined(IInventory inventoryPlayer, IInventory otherInventory, IInventory inventoryBackpack) {
        super(new ContainerBackpackCombined(inventoryPlayer, otherInventory, inventoryBackpack, null));
        upperInventory = otherInventory;
        lowerInventory = inventoryBackpack;

        container = (ContainerBackpackCombined) inventorySlots;

        lowerGuiRows = container.lowerInventoryRows > 3 ? 3 : container.lowerInventoryRows;
        ySize = container.top.ySize + container.INVENTORYSPACING + (lowerGuiRows + 1) * container.SLOT + container.HOTBARSPACING + container.BOTTOMSPACING;
        background = new ResourceLocation("textures/gui/container/generic_54.png");
    }

    @Override
    public void initGui() {
        super.initGui();
        container.top.initGui(guiTop, guiLeft);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        int lowerHeight = container.BOTTOMSPACING + container.SLOT + container.HOTBARSPACING + lowerGuiRows * container.SLOT + container.INVENTORYSPACING;
        // fontRenderer.drawString(StatCollector.translateToLocal(upperInventory.getInvName()),
        // 8, 6, 0x404040);
        container.top.drawForegroundLayer(fontRenderer, x, y);
        fontRenderer.drawString(StatCollector.translateToLocal(lowerInventory.getInvName()), 8, ySize - lowerHeight + 3, 0x404040);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int bottomHeight = container.INVENTORYSPACING + lowerGuiRows * container.SLOT;

        // top container (chest/furnance/hopper/dropper/dispenser)
        container.top.drawBackgroundLayer(f, x, y);

        mc.func_110434_K().func_110577_a(background);

        // bottom container (backpack)
        drawTexturedModalRect(guiLeft, guiTop + container.top.ySize, 0, 125, xSize, bottomHeight);
        // bottom of gui
        drawTexturedModalRect(guiLeft, guiTop + container.top.ySize + bottomHeight, 0, 193, xSize, 29);
    }
}
