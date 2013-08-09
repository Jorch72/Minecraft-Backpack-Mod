package backpack.gui;

import net.minecraft.inventory.IInventory;

import org.lwjgl.input.Mouse;

import backpack.gui.combined.GuiPartScrolling;
import backpack.inventory.ContainerBackpackCombined;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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

    @Override
    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        if(container.top instanceof GuiPartScrolling) {
            ((GuiPartScrolling) container.top).mouseClicked(x, y);
        }
        if(container.bottom instanceof GuiPartScrolling) {
            ((GuiPartScrolling) container.bottom).mouseClicked(x, y);
        }
    }

    @Override
    protected void mouseClickMove(int x, int y, int button, long timeSinceClicked) {
        super.mouseClickMove(x, y, button, timeSinceClicked);
        if(container.top instanceof GuiPartScrolling) {
            ((GuiPartScrolling) container.top).mouseClickMove(x, y);
        }
        if(container.bottom instanceof GuiPartScrolling) {
            ((GuiPartScrolling) container.bottom).mouseClickMove(x, y);
        }
    }
    
    @Override
    protected void mouseMovedOrUp(int x, int y, int button) {
        super.mouseMovedOrUp(x, y, button);
        if(container.top instanceof GuiPartScrolling) {
            ((GuiPartScrolling) container.top).mouseReleased(x, y);
        }
        if(container.bottom instanceof GuiPartScrolling) {
            ((GuiPartScrolling) container.bottom).mouseReleased(x, y);
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int mouseWheelDirection = Mouse.getEventDWheel();

        if(mouseWheelDirection > 0) {
            mouseWheelDirection = 1;
        }
        if(mouseWheelDirection < 0) {
            mouseWheelDirection = -1;
        }

        if(container.top instanceof GuiPartScrolling) {
            if(container.top.isInRactangle(mouseX, mouseY)) {
                ((GuiPartScrolling) container.top).mouseScrollWheel(mouseWheelDirection);
            }
        }
        if(container.bottom instanceof GuiPartScrolling) {
            if(container.bottom.isInRactangle(mouseX, mouseY)) {
                ((GuiPartScrolling) container.bottom).mouseScrollWheel(mouseWheelDirection);
            }
        }
    }
}