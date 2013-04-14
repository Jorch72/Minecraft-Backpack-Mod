package backpack;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.Item;
import net.minecraftforge.common.EnumHelper;
import backpack.item.ItemBackpack;
import backpack.item.ItemLeather;
import backpack.misc.ConfigurationBackpack;
import backpack.misc.Constants;
import backpack.proxy.CommonProxy;
import backpack.recipes.RecipeHelper;
import backpack.util.PacketHandlerBackpack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = "Backpack", name = "Backpack", version = "1.7.9")
@NetworkMod(
        clientSideRequired = true,
        serverSideRequired = true,
        channels = { Constants.CHANNEL_RENAME, Constants.CHANNEL_OPEN },
        packetHandler = PacketHandlerBackpack.class
        )
public class Backpack {
    // an instance of the actual item
    public static Item backpack;
    public static Item boundLeather;
    public static Item tannedLeather;

    public static EnumArmorMaterial backpackMaterial = EnumHelper.addArmorMaterial("backpackMaterial", -1, new int[] { 0, 0, 0, 0 }, 0);

    @Instance("Backpack")
    public static Backpack instance;

    @SidedProxy(clientSide = "backpack.proxy.ClientProxy", serverSide = "backpack.proxy.CommonProxy")
    public static CommonProxy proxy;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        // get the configuration file and let forge guess it's name
        ConfigurationBackpack config = new ConfigurationBackpack(event.getSuggestedConfigurationFile());
        config.init();
    }

    @Init
    public void load(FMLInitializationEvent event) {
        // create an instance of the backpack item with the id loaded from the
        // configuration file
        backpack = new ItemBackpack(ConfigurationBackpack.BACKPACK_ID);
        boundLeather = new ItemLeather(ConfigurationBackpack.BOUND_LEATHER_ID);
        tannedLeather = new ItemLeather(ConfigurationBackpack.TANNED_LEATHER_ID);

        // register recipes
        RecipeHelper.registerRecipes();

        // register GuiHandler
        NetworkRegistry.instance().registerGuiHandler(this, proxy);

        // register key bindings
        proxy.registerKeyBinding();
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        // Stub Method
    }
}
