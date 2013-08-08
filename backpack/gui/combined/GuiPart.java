package backpack.gui.combined;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;

import org.lwjgl.opengl.GL11;

import backpack.inventory.ContainerAdvanced;
import backpack.misc.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiPart extends Gui {
    protected int LEFTSPACING = 8;
    protected int SLOT = 18;

    protected ContainerAdvanced container;
    protected IInventory inventory;
    protected boolean big;
    protected int guiLeft;
    protected int guiTop;
    protected int offsetY;
    protected int textOffset;
    protected TEXTPOSITION textPosition = TEXTPOSITION.LEFT;
    protected int inventoryRows;
    protected int topSpacing;
    protected int bottomSpacing;
    protected int firstSlot;
    protected int lastSlot;
    public int ySize;
    public int xSize = 176;

    public GuiPart(ContainerAdvanced container, IInventory inventory, int inventoryRows) {
        this(container, inventory, inventoryRows, false);
    }

    public GuiPart(ContainerAdvanced container, IInventory inventory, int inventoryRows, boolean big) {
        this.container = container;
        this.inventory = inventory;
        this.big = big;
        if(big) {
            this.inventoryRows = inventoryRows > 6 ? 6 : inventoryRows;
        } else {
            this.inventoryRows = inventoryRows > 3 ? 3 : inventoryRows;
        }
        ySize = this.inventoryRows * SLOT;
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

    public void drawBackgroundLayer(float f, int x, int y) {
        GL11.glPushMatrix();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().func_110434_K().func_110577_a(Constants.guiCombined);
        drawTexturedModalRect(guiLeft, guiTop + offsetY, 0, 4, xSize, ySize);

        GL11.glPopMatrix();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
    }

    public void addCraftingToCrafters(ICrafting par1iCrafting) {
    }

    public void detectAndSendChanges() {
    }

    public abstract void addSlots();

    public void drawForegroundLayer(FontRenderer fontRenderer, int x, int y) {
        String text = inventory.isInvNameLocalized() ? inventory.getInvName() : I18n.func_135053_a(inventory.getInvName());
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

    public enum TEXTPOSITION {
        LEFT, MIDDLE, RIGHT
    }
}