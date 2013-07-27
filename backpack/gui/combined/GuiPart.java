package backpack.gui.combined;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import backpack.inventory.ContainerBackpackCombined;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class GuiPart extends Gui {
    public int TOPSPACING = 18;
    public int LEFTSPACING = 8;
    public int BOTTOMSPACING = 7;
    public int SLOT = 18;
    public int INVENTORYSPACING = 14;
    public int HOTBARSPACING = 4;

    protected IInventory inventory;
    protected ResourceLocation background;
    protected int guiLeft;
    protected int guiTop;
    public int rows;
    public int ySize;
    public int xSize = 176;

    public GuiPart(IInventory inventory, int inventoryRows) {
        this.inventory = inventory;
        rows = inventoryRows > 3 ? 3 : inventoryRows;
    }

    public void initGui(int guiTop, int guiLeft) {
        this.guiTop = guiTop;
        this.guiLeft = guiLeft;
    }

    public void drawBackgroundLayer(float f, int x, int y) {
        GL11.glPushMatrix();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft.getMinecraft().func_110434_K().func_110577_a(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        GL11.glPopMatrix();
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2) {
    }

    public void addCraftingToCrafters(Container container, ICrafting par1iCrafting) {
    }

    public void detectAndSendChanges(ContainerBackpackCombined container) {
    }

    public abstract void addSlots(ContainerBackpackCombined container);

    public abstract void drawForegroundLayer(FontRenderer fontRenderer, int x, int y);
}
