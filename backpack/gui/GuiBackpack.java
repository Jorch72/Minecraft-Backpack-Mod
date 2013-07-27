package backpack.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import backpack.inventory.ContainerBackpack;
import backpack.misc.Constants;
import backpack.util.NBTUtil;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiBackpack extends GuiContainer {
    private IInventory upperInventory;
    private IInventory lowerInventory;
    private int inventoryRows;
    private ResourceLocation background;

    public GuiBackpack(IInventory inventoryPlayer, IInventory inventoryBackpack) {
        super(new ContainerBackpack(inventoryPlayer, inventoryBackpack, null));
        upperInventory = inventoryBackpack;
        lowerInventory = inventoryPlayer;
        inventoryRows = inventoryBackpack.getSizeInventory() / 9;
        ySize = 115 + inventoryRows * 18;
        background = new ResourceLocation("textures/gui/container/generic_54.png");
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRenderer.drawString(StatCollector.translateToLocal(upperInventory.getInvName()), 8, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal(lowerInventory.getInvName()), 8, ySize - 95, 0x404040);
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.func_110434_K().func_110577_a(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, inventoryRows * 18 + 17);
        drawTexturedModalRect(guiLeft, guiTop + inventoryRows * 18 + 17, 0, 125, xSize, 97);
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