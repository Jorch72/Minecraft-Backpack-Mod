package de.eydamos.backpack;

import de.eydamos.backpack.init.Configurations;
import de.eydamos.backpack.misc.Constants;
import de.eydamos.backpack.network.PacketHandlerBackpack;
import de.eydamos.backpack.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = Constants.MOD_ID, name = Constants.MOD_NAME, version = Constants.MOD_VERSION, certificateFingerprint = Constants.FINGERPRINT, guiFactory = Constants.CLASS_GUI_FACTORY)
public class Backpack {
    @Mod.Instance(Constants.MOD_ID)
    public static Backpack instance;

    @SidedProxy(clientSide = Constants.CLASS_PROXY_CLIENT, serverSide = Constants.CLASS_PROXY_SERVER)
    public static CommonProxy proxy;

    public static final Logger logger = LogManager.getLogger(Constants.MOD_NAME);

    public static final PacketHandlerBackpack packetHandler = new PacketHandlerBackpack();

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();
        Configurations.configExists = configFile.exists();
        Configurations.config = new Configuration(configFile);
        Configurations.config.load();
        Configurations.refreshConfig();

        // key bindings
        proxy.registerKeybindings();

        FMLInterModComms.sendRuntimeMessage(
            Constants.MOD_ID,
            "VersionChecker",
            "addVersionCheck",
            Constants.UPDATE_FILE
        );
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // registerItems all Handlers
        proxy.registerHandlers();
    }
}
