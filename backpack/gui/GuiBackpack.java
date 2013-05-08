package backpack.gui;

import invtweaks.api.ContainerGUI;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import backpack.inventory.ContainerBackpack;
import backpack.misc.Constants;
import backpack.util.NBTUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@ContainerGUI
@SideOnly(Side.CLIENT)
public class GuiBackpack extends GuiContainer {
    private IInventory upperInventory;
    private IInventory lowerInventory;
    private int inventoryRows;

    public GuiBackpack(IInventory inventoryPlayer, IInventory inventoryBackpack) {
        super(new ContainerBackpack(inventoryPlayer, inventoryBackpack, null));
        upperInventory = inventoryBackpack;
        lowerInventory = inventoryPlayer;
        inventoryRows = inventoryBackpack.getSizeInventory() / 9;
        ySize = 114 + inventoryRows * 18;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString(StatCollector.translateToLocal(upperInventory.getInvName()), 8, 6, 4210752);
        fontRenderer.drawString(StatCollector.translateToLocal(lowerInventory.getInvName()), 8, ySize - 96 + 2, 4210752);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture("/gui/container.png");
        int var5 = (width - xSize) / 2;
        int var6 = (height - ySize) / 2;
        drawTexturedModalRect(var5, var6, 0, 0, xSize, inventoryRows * 18 + 17);
        drawTexturedModalRect(var5, var6 + inventoryRows * 18 + 17, 0, 126, xSize, 96);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if(mc.thePlayer != null) {
            EntityPlayer player = mc.thePlayer;

            ItemStack itemStack = player.getCurrentArmor(2);
            if(itemStack != null) {
                if(NBTUtil.hasTag(itemStack, Constants.WEARED_BACKPACK_OPEN)) {
                    NBTUtil.removeTag(itemStack, Constants.WEARED_BACKPACK_OPEN);
                }
            }
        }
    }
}