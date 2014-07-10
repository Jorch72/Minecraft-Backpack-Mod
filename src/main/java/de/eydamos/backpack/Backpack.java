package de.eydamos.backpack;

import de.eydamos.backpack.helper.BackpackHelper;
import de.eydamos.backpack.misc.Upgrader;
import org.apache.logging.log4j.Level;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLFingerprintViolationEvent;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import de.eydamos.backpack.handler.SaveFileHandler;
import de.eydamos.backpack.item.ItemsBackpack;
import de.eydamos.backpack.misc.ConfigurationBackpack;
import de.eydamos.backpack.misc.Constants;
import de.eydamos.backpack.network.PacketHandlerBackpack;
import de.eydamos.backpack.proxy.CommonProxy;
import de.eydamos.backpack.recipes.RecipeHelper;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, certificateFingerprint = Constants.FINGERPRINT, guiFactory = Constants.CLASS_GUI_FACTORY)
public class Backpack {
    @Instance(Constants.MOD_ID)
    public static Backpack instance;

    @SidedProxy(clientSide = Constants.CLASS_PROXY_CLIENT, serverSide = Constants.CLASS_PROXY_SERVER)
    public static CommonProxy proxy;

    public static PacketHandlerBackpack packetHandler = new PacketHandlerBackpack();
    public static SaveFileHandler saveFileHandler = new SaveFileHandler();
    public static BackpackHelper backpackHelper = new BackpackHelper();

    public static boolean valid = true;

    @EventHandler
    public void invalidFingerprint(FMLFingerprintViolationEvent event) {
        if(!Constants.FINGERPRINT.equals("@FINGERPRINT@")) {
            valid = false;

            FMLLog.log(Constants.MOD_ID, Level.ERROR, "Invalid fingerprint someone has changed the mod. Please redownload from the forum.");
        }
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        if(valid) {
            // get the configuration and let forge guess the filename
            ConfigurationBackpack.init(event.getSuggestedConfigurationFile());

            // create an instance of the items
            ItemsBackpack.initItems();

            // key bindings
            proxy.registerKeybindings();

            FMLInterModComms.sendRuntimeMessage(Constants.MOD_ID, "VersionChecker", "addVersionCheck", Constants.UPDATE_FILE);
        }
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        if(valid) {
            // register recipes
            RecipeHelper.registerRecipes();

            // register all Handlers
            proxy.registerHandlers();
        }
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.addNeiSupport();
    }
}
