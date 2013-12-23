package backpack.gui.parts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;

import org.lwjgl.opengl.GL11;

import backpack.gui.GuiAdvanced;
import backpack.inventory.container.ContainerAdvanced;
import backpack.misc.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GuiPart<C extends ContainerAdvanced> {
    protected int LEFTSPACING = 8;
    protected int SLOT = 18;

    protected C container;
    protected IInventory inventory;
    protected GuiAdvanced gui;
    protected int guiLeft;
    protected int guiTop;
    protected int offsetY;
    protected int textOffset;
    protected TEXTPOSITION textPosition = TEXTPOSITION.LEFT;
    protected int topSpacing;
    protected int bottomSpacing;
    public int inventoryRows;
    public int inventoryCols;
    public int firstSlot;
    public int lastSlot;
    public int ySize;
    public int xSize = 176;

    public GuiPart(ContainerAdvanced container, IInventory inventory, int inventoryRows) {
        this(container, inventory, inventoryRows, 9, false);
    }

    public GuiPart(ContainerAdvanced container, IInventory inventory, int inventoryRows, int inventoryCols, boolean big) {
        this.container = (C) container;
        this.inventory = inventory;
        if(big) {
            this.inventoryRows = inventoryRows > 6 ? 6 : inventoryRows;
        } else {
            this.inventoryRows = inventoryRows > 3 ? 3 : inventoryRows;
        }
        this.inventoryCols = inventoryCols;
        ySize = this.inventoryRows * SLOT;
    }

    public void setGui(GuiAdvanced gui) {
        this.gui = gui;
    }

    public void initGui(int guiLeft, int guiTop) {
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void setTextOffset(int offset) {
        textOffset = offset;
    }

    public void setTextPosition(TEXTPOSITION position) {
        textPosition = position;
    }

    public void setSpacings(int topSpacing, int bottomSpacing) {
        this.topSpacing = topSpacing;
        this.bottomSpacing = bottomSpacing;
        if(ySize != 0) {
            ySize += topSpacing + bottomSpacing;
        }
    }

    public void drawForegroundLayer(FontRenderer fontRenderer, int x, int y) {
        String text = inventory.isInvNameLocalized() ? inventory.getInvName() : I18n.getString(inventory.getInvName());
        int xOffset;
        switch(textPosition) {
            case LEFT:
                xOffset = 8;
                break;
            case MIDDLE:
                xOffset = xSize / 2 - fontRenderer.getStringWidth(text) / 2;
                break;
            case RIGHT:
                xOffset = xSize - fontRenderer.getStringWidth(text) - 6;
                break;
            default:
                xOffset = 0;
        }
        fontRenderer.drawString(text, xOffset, textOffset, 0x404040);
    }

    public void drawBackgroundLayer(float f, int x, int y) {
        GL11.glPushMatrix();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().getTextureManager().bindTexture(Constants.guiCombined);
        drawTexturedModalRect(guiLeft, guiTop + offsetY, 0, 4, xSize, ySize);

        GL11.glPopMatrix();
    }

    public void actionPerformed(GuiButton guiButton) {
    }

    public void addCraftingToCrafters(ICrafting player) {
    }

    public void detectAndSendChanges() {
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int value) {
    }

    public boolean isInRactangle(int mouseX, int mouseY) {
        int x = guiLeft;
        int y = guiTop + offsetY + topSpacing;
        int width = xSize;
        int height = ySize - topSpacing - bottomSpacing;

        return x <= mouseX && mouseX <= x + width && y <= mouseY && mouseY <= y + height;
    }

    public abstract void addSlots();

    protected void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par6, 0, (par3 + 0) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, 0, (par3 + par5) * f, (par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + 0, 0, (par3 + par5) * f, (par4 + 0) * f1);
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0, (par3 + 0) * f, (par4 + 0) * f1);
        tessellator.draw();
    }

    public enum TEXTPOSITION {
        LEFT, MIDDLE, RIGHT
    }
}