package backpack;

import backpack.handler.PacketHandlerBackpack;
import backpack.item.Items;
import backpack.misc.ConfigurationBackpack;
import backpack.misc.Constants;
import backpack.misc.Version;
import backpack.proxy.CommonProxy;
import backpack.recipes.RecipeHelper;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION)
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = false,
        channels = { Constants.CHANNEL },
        packetHandler = PacketHandlerBackpack.class
        )
public class Backpack {
    @Instance(Constants.MOD_ID)
    public static Backpack instance;

    @SidedProxy(clientSide = "backpack.proxy.ClientProxy", serverSide = "backpack.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // get the configuration file and let forge guess it's name
        ConfigurationBackpack.init(event.getSuggestedConfigurationFile());

        // create an instance of the backpack item with the id loaded from the
        // configuration file
        Items.initItems();
    }

    @Mod.EventHandler
    public void load(FMLInitializationEvent event) {
        Version.checkForUpdate();
        
        Items.addNames();

        // register recipes
        RecipeHelper.registerRecipes();

        // register all Handlers
        proxy.registerHandler();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.addNeiSupport();
    }
}
