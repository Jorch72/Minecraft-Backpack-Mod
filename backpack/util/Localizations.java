package backpack.util;

import java.io.File;
import java.net.URISyntaxException;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Localizations {
    protected static final String path = "/assets/backpack/lang/";
    
    public static void addLocalizations() {
        File folder;
        try {
            folder = new File(Localizations.class.getResource(path).toURI());
        
            if(folder.exists()) {
                for(File langFile : folder.listFiles()) {
                    String fileName  = langFile.getName();
                    if(fileName.endsWith(".properties")) {
                        String lang = fileName.split("\\.")[0];
                        LanguageRegistry.instance().loadLocalization(path + fileName, lang , false);
                    }
                }
            }
        }
        catch (URISyntaxException e) {
            FMLLog.info("[Backpacks] Failed to load localizations.");
        }
    }
}
