package backpack.gui;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import backpack.gui.parts.GuiPart;
import backpack.gui.parts.GuiPartScrolling;
import backpack.handler.KeyHandlerBackpack;
import backpack.handler.PacketHandlerBackpack;
import backpack.inventory.container.ContainerAdvanced;
import backpack.misc.ConfigurationBackpack;
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
    protected void actionPerformed(GuiButton guiButton) {
        for(GuiPart guiPart : container.parts) {
            guiPart.actionPerformed(guiButton);
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
    protected void func_85041_a(int x, int y, int button, long timeSinceClicked) {
        super.func_85041_a(x, y, button, timeSinceClicked);
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
                if(ConfigurationBackpack.NEISupport) {
                    if(((GuiPartScrolling) guiPart).getScrollbar().isInRectangle(mouseX, mouseY)) {
                        ((GuiPartScrolling) guiPart).mouseScrollWheel(mouseWheelDirection);
                    }
                } else {
                    if(guiPart.isInRactangle(mouseX, mouseY)) {
                        ((GuiPartScrolling) guiPart).mouseScrollWheel(mouseWheelDirection);
                    }
                }
            }
        }
    }

    @Override
    protected void keyTyped(char charTyped, int keyCode) {
        super.keyTyped(charTyped, keyCode);

        if(keyCode == KeyHandlerBackpack.openBackpack.keyCode) {
            PacketHandlerBackpack.sendGuiOpenCloseToServer(Constants.PACKET_ID_CLOSE_GUI);
            close = true;
        }
    }

    @Override
    protected void drawHoveringText(List lines, int mouseX, int mouseY, FontRenderer fontRenderer) {
        if(!lines.isEmpty()) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int longestTooltipContent = 0;

            for(Object line : lines) {
                int lineWidth = fontRenderer.getStringWidth(line.toString());

                if(lineWidth > longestTooltipContent) {
                    longestTooltipContent = lineWidth;
                }
            }

            int tooltipX = mouseX + 12;
            int tooltipY = mouseY - 12;
            int tooltipHeight = 8;

            if(lines.size() > 1) {
                tooltipHeight += 2 + (lines.size() - 1) * 10;
            }

            if(tooltipX + longestTooltipContent > width) {
                tooltipX -= 28 + longestTooltipContent;
            }

            if(tooltipY + tooltipHeight + 6 > height) {
                tooltipY = height - tooltipHeight - 6;
            }

            zLevel = 300.0F;
            itemRenderer.zLevel = 300.0F;
            int l1 = -267386864;
            drawGradientRect(tooltipX - 3, tooltipY - 4, tooltipX + longestTooltipContent + 3, tooltipY - 3, l1, l1);
            drawGradientRect(tooltipX - 3, tooltipY + tooltipHeight + 3, tooltipX + longestTooltipContent + 3, tooltipY + tooltipHeight + 4, l1, l1);
            drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + longestTooltipContent + 3, tooltipY + tooltipHeight + 3, l1, l1);
            drawGradientRect(tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + tooltipHeight + 3, l1, l1);
            drawGradientRect(tooltipX + longestTooltipContent + 3, tooltipY - 3, tooltipX + longestTooltipContent + 4, tooltipY + tooltipHeight + 3, l1, l1);
            int i2 = 1347420415;
            int j2 = (i2 & 0xFEFEFE) >> 1 | i2 & 0xFF000000;
            drawGradientRect(tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + tooltipHeight + 3 - 1, i2, j2);
            drawGradientRect(tooltipX + longestTooltipContent + 2, tooltipY - 3 + 1, tooltipX + longestTooltipContent + 3, tooltipY + tooltipHeight + 3 - 1, i2, j2);
            drawGradientRect(tooltipX - 3, tooltipY - 3, tooltipX + longestTooltipContent + 3, tooltipY - 3 + 1, i2, i2);
            drawGradientRect(tooltipX - 3, tooltipY + tooltipHeight + 2, tooltipX + longestTooltipContent + 3, tooltipY + tooltipHeight + 3, j2, j2);

            for(int i = 0; i < lines.size(); ++i) {
                String line = (String) lines.get(i);
                fontRenderer.drawStringWithShadow(line, tooltipX, tooltipY, -1);

                if(i == 0) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            zLevel = 0.0F;
            itemRenderer.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }

    protected void drawTopBorder() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.renderEngine.bindTexture(Constants.guiCombined);

        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, TOPSPACING);
    }

    protected void drawBottomBorder() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        mc.renderEngine.bindTexture(Constants.guiCombined);

        drawTexturedModalRect(guiLeft, guiTop + ySize - BOTTOMSPACING, 0, 160, xSize, BOTTOMSPACING);
    }

    public List<GuiButton> getButtonList() {
        return buttonList;
    }

    public void closeGui() {
        if(close) {
            mc.thePlayer.closeScreen();
            close = false;
        }
    }
}