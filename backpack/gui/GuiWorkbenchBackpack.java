package backpack.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import backpack.inventory.ContainerWorkbenchBackpack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiWorkbenchBackpack extends GuiContainer {
    private ResourceLocation background;

    public GuiWorkbenchBackpack(InventoryPlayer inventoryPlayer, IInventory inventoryBackpack) {
        super(new ContainerWorkbenchBackpack(inventoryPlayer, inventoryBackpack, null));
        xSize = 175;
        if(inventoryBackpack.getSizeInventory() != 0) {
            ySize = 207;
            background = new ResourceLocation("backpack", "textures/gui/guiWorkbenchBackpack.png");
        } else {
            ySize = 166;
            background = new ResourceLocation("textures/gui/container/crafting_table.png");
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString(StatCollector.translateToLocal("container.crafting"), 28, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 94, 0x404040);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(ySize == 207) {
            mc.func_110434_K().func_110577_a(background);
        } else {
            mc.func_110434_K().func_110577_a(background);
        }
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}