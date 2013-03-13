package backpack.proxy;

import net.minecraftforge.client.MinecraftForgeClient;
import backpack.util.KeyHandlerBackpack;
import cpw.mods.fml.client.registry.KeyBindingRegistry;

public class ClientProxy extends CommonProxy {	
	@Override
	public void registerKeyBinding() {
		KeyBindingRegistry.registerKeyBinding(new KeyHandlerBackpack());
	}
}