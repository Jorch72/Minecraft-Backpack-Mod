package backpack.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import backpack.gui.parts.GuiPart;
import backpack.gui.parts.GuiPartScrolling;
import backpack.handler.PacketHandlerBackpack;
import backpack.inventory.container.ContainerAdvanced;
import backpack.misc.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiAdvanced<T extends ContainerAdvanced> extends GuiContainer {
    protected static final int TOPSPACING = 16;
    protected static final int BOTTOMSPACING = 7;
    protected T container;
    protected boolean close = false;

    public GuiAdvanced(Container container) {
        super(container);
    }

    @Override
    public void initGui() {
        super.initGui();
        
        for(GuiPart guiPart : container.parts) {
            guiPart.initGui(guiLeft, guiTop);
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

        for(GuiPart guiPart : container.parts) {
            guiPart.drawBackgroundLayer(f, x, y);
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x, y, button);
        for(GuiPart guiPart : container.parts) {
            if(guiPart instanceof GuiPartScrolling) {
                ((GuiPartScrolling) guiPart).mouseClicked(x, y);
            }
        }
    }

    @Override
    protected void mouseClickMove(int x, int y, int button, long timeSinceClicked) {
        super.mouseClickMove(x, y, button, timeSinceClicked);
        for(GuiPart guiPart : container.parts) {
            if(guiPart instanceof GuiPartScrolling) {
                ((GuiPartScrolling) guiPart).mouseClickMove(x, y);
            }
        }
    }

    @Override
    protected void mouseMovedOrUp(int x, int y, int button) {
        super.mouseMovedOrUp(x, y, button);
        for(GuiPart guiPart : container.parts) {
            if(guiPart instanceof GuiPartScrolling) {
                ((GuiPartScrolling) guiPart).mouseReleased(x, y);
            }
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();

        int mouseX = Mouse.getEventX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
        int mouseWheelDirection = Mouse.getEventDWheel();

        if(mouseWheelDirection > 0) {
            mouseWheelDirection = 1;
        }
        if(mouseWheelDirection < 0) {
            mouseWheelDirection = -1;
        }

        for(GuiPart guiPart : container.parts) {
            if(guiPart instanceof GuiPartScrolling) {
                if(guiPart.isInRactangle(mouseX, mouseY)) {
                    ((GuiPartScrolling) guiPart).mouseScrollWheel(mouseWheelDirection);
                }
            }
        }
    }
    
    @Override
    protected void keyTyped(char charTyped, int idTyped) {
        super.keyTyped(charTyped, idTyped);
        
        if(charTyped == 'b') {
            PacketHandlerBackpack.sendGuiOpenCloseToServer(Constants.PACKET_ID_CLOSE_GUI);
            close = true;
        }
    }

    protected void drawTopBorder() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.func_110434_K().func_110577_a(Constants.guiCombined);

        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, TOPSPACING);
    }

    protected void drawBottomBorder() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.func_110434_K().func_110577_a(Constants.guiCombined);

        drawTexturedModalRect(guiLeft, guiTop + ySize - BOTTOMSPACING, 0, 160, xSize, BOTTOMSPACING);
    }
    
    public void closeGui() {
        if(close) {
            mc.thePlayer.closeScreen();
            close = false;
        }
    }
}