package backpack.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import backpack.Backpack;
import backpack.util.Version;
import cpw.mods.fml.common.IPlayerTracker;

public class PlayerTrackerBackpack implements IPlayerTracker {

    @Override
    public void onPlayerLogin(EntityPlayer player) {
        Backpack.playerHandler.loadPlayer(player);

        String latestSeen = Backpack.playerHandler.getLatestSeenVersion(player);
        if(Version.newestVersion != "" && !latestSeen.equals(Version.newestVersion) && !Version.isOutdated()) {
            player.addChatMessage("text.backpack.up_to_date");
            Backpack.playerHandler.setLatestSeenVersion(player, Version.newestVersion);
        } else if(Version.isOutdated()) {
            ChatMessageComponent message = new ChatMessageComponent().addFormatted("text.backpack.update_available", Version.newestVersion);
            player.sendChatToPlayer(message);
        }
    }

    @Override
    public void onPlayerLogout(EntityPlayer player) {
        Backpack.playerHandler.unloadPlayer(player);
    }

    @Override
    public void onPlayerChangedDimension(EntityPlayer player) {
        PacketHandlerBackpack.sendWornBackpackDataToClient(player);
    }

    @Override
    public void onPlayerRespawn(EntityPlayer player) {
        PacketHandlerBackpack.sendWornBackpackDataToClient(player);
    }
}