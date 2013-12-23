package backpack.gui.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class GuiIconButton extends GuiButton {
    protected ResourceLocation icon = null;
    protected int iconPositionX = 0;
    protected int iconPositionY = 0;
    protected int iconOffsetX = 0;
    protected int iconOffsetY = 0;
    protected int iconWidth;
    protected int iconHeight;
    protected boolean offsetable = true;

    public GuiIconButton(int id, int xPos, int yPos, String buttonText) {
        this(id, xPos, yPos, 200, 20, buttonText);
    }

    public GuiIconButton(int id, int xPos, int yPos, int width, int height, String buttonText) {
        super(id, xPos, yPos, width, height, buttonText);
        if(height > 20) {
            height = 20;
        }
        if(width > 200) {
            width = 200;
        }
        iconWidth = width;
        iconHeight = height;
    }

    /**
     * Sets an icon for the button with no offset.
     * 
     * @param icon
     *            The ResourceLocation of the icon which should be printed on
     *            the button.
     */
    public void setIcon(ResourceLocation icon) {
        setIcon(icon, 0, 0);
    }

    /**
     * Sets an icon for the button. The offsets are relative to the upper left
     * corner of the button.
     * 
     * @param icon
     *            The ResourceLocation of the icon which should be printed on
     *            the button.
     * @param offsetX
     *            The x offset relative to the button.
     * @param offsetY
     *            The y offset relative to the button.
     */
    public void setIcon(ResourceLocation icon, int offsetX, int offsetY) {
        this.icon = icon;
        iconOffsetX = offsetX;
        iconOffsetY = offsetY;
    }

    /**
     * Sets if the icon has an offset. 
     * If set to true the offsets are defined as followed:
     * Disabled button icon has an y offset of 0.
     * Normal button icon has an y offset of 20.
     * Hovered button icon has an y offset of 40.
     * 
     * @param value
     *            If the icon has an offset for different button states.
     */
    public void setIconOffsettable(boolean value) {
        offsetable = value;
    }

    /**
     * Sets the width and the height of the icon, default values are the size of
     * the button.
     * 
     * @param width
     *            The width of the icon.
     * @param height
     *            The hight of the icon.
     */
    public void setIconSize(int width, int height) {
        iconWidth = width;
        iconHeight = height;
    }

    @Override
    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        if(drawButton) {
            FontRenderer fontrenderer = minecraft.fontRenderer;

            minecraft.getTextureManager().bindTexture(buttonTextures);

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            field_82253_i = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
            int backgroundOffset = getHoverState(field_82253_i);

            int widthLeft = (int) Math.floor(width / 2);
            int widthRight = width - widthLeft;
            drawTexturedModalRect(xPosition, yPosition, 0, 46 + backgroundOffset * 20, widthLeft, height);
            drawTexturedModalRect(xPosition + widthLeft, yPosition, 200 - widthRight, 46 + backgroundOffset * 20, widthRight, height);
            // bottom 4 pixels
            drawTexturedModalRect(xPosition, yPosition + height - 4, 0, 46 + backgroundOffset * 20 + 16, widthLeft, 4);
            drawTexturedModalRect(xPosition + widthLeft, yPosition + height - 4, 200 - widthRight, 46 + backgroundOffset * 20 + 16, widthRight, 4);

            // icon
            if(icon != null) {
                if(!offsetable) {
                    backgroundOffset = 0;
                }
                drawTexturedModalRect(xPosition + iconPositionX, yPosition + iconPositionY, iconOffsetX, iconOffsetY + backgroundOffset * 20, iconWidth, iconHeight);
            }

            mouseDragged(minecraft, mouseX, mouseY);
            int textColor = 14737632;

            if(!enabled) {
                textColor = -6250336;
            } else if(field_82253_i) {
                textColor = 16777120;
            }

            drawCenteredString(fontrenderer, displayString, xPosition + (int) Math.ceil(width / 2) + 1, yPosition + (height - 8) / 2, textColor);
        }
    }
}