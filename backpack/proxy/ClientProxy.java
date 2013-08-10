package backpack.proxy;

import java.lang.reflect.Method;

import net.minecraft.entity.player.EntityPlayer;
import backpack.gui.GuiWorkbenchBackpack;
import backpack.handler.KeyHandlerBackpack;
import backpack.handler.PlayerHandlerBackpack;
import backpack.handler.RenderPlayerHandler;
import backpack.nei.OverlayHandlerBackpack;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.registry.GameRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void registerHandler() {
        super.registerHandler();
        KeyBindingRegistry.registerKeyBinding(new KeyHandlerBackpack());
        IPlayerTracker playerHandler = new PlayerHandlerBackpack();
        GameRegistry.registerPlayerTracker(playerHandler);
        RenderingRegistry.registerEntityRenderingHandler(EntityPlayer.class, new RenderPlayerHandler());
    }

    @Override
    public void addNeiSupport() {
        try {
            Class API = Class.forName("codechicken.nei.api.API");
            Class IOverlayHandler = Class.forName("codechicken.nei.api.IOverlayHandler");
            Method registerGuiOverlay = API.getDeclaredMethod("registerGuiOverlay", new Class[] { Class.class, String.class });
            Method registerGuiOverlayHandler = API.getDeclaredMethod("registerGuiOverlayHandler", new Class[] { Class.class, IOverlayHandler, String.class });

            registerGuiOverlay.invoke(API, new Object[] { GuiWorkbenchBackpack.class, "crafting" });
            registerGuiOverlayHandler.invoke(API, new Object[] { GuiWorkbenchBackpack.class, new OverlayHandlerBackpack(), "crafting" });

            FMLLog.info("[Backpacks] NEI Support enabled");
        }
        catch (Exception e) {
            FMLLog.info("[Backpacks] NEI Support couldn't be enabled");
        }
    }
}