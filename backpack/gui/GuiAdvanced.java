package backpack.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import org.lwjgl.opengl.GL11;

import backpack.misc.Constants;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class GuiAdvanced extends GuiContainer {
    protected static final int TOPSPACING = 16;
    protected static final int BOTTOMSPACING = 7;

    public GuiAdvanced(Container container) {
        super(container);
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
}