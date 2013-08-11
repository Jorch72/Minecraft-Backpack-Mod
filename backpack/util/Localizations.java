package backpack.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Localizations {
    protected static final String path = "/assets/backpack/lang/";

    public static void addLocalizations() {
        InputStream is = Localizations.class.getResourceAsStream(path + "languages.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        try {
            while(in.ready()) {
                String lang = in.readLine();
                LanguageRegistry.instance().loadLocalization(path + lang + ".properties", lang, false);
            }
        }
        catch (IOException e) {
            FMLLog.info("[Backpacks] Failed to load localizations.");
        }
    }
}
