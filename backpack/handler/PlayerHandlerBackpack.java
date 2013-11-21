package backpack.handler;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import backpack.util.PlayerSave;
import cpw.mods.fml.common.IPlayerTracker;

public class PlayerHandlerBackpack implements IPlayerTracker {
    protected ConcurrentHashMap<String, PlayerSave> playerSaves = new ConcurrentHashMap<String, PlayerSave>();
    protected File worldSaveDir = null;

    @Override
    public void onPlayerLogin(EntityPlayer player) {
        PlayerSave playerSave = getPlayerSave(player.username);

        // backwards compatibility
        NBTTagCompound playerData = player.getEntityData();
        if(playerData.hasKey("backpack")) {
            ItemStack backpack = ItemStack.loadItemStackFromNBT(playerData.getCompoundTag("backpack"));
            playerSave.setWornBackpack(backpack);
            playerData.removeTag("backpack");
        }

        PacketHandlerBackpack.sendWornBackpackDataToClient(player);
    }

    @Override
    public void onPlayerLogout(EntityPlayer player) {
        getPlayerSave(player.username).save();
        playerSaves.remove(player.username);
    }

    @Override
    public void onPlayerChangedDimension(EntityPlayer player) {
        PacketHandlerBackpack.sendWornBackpackDataToClient(player);
    }

    @Override
    public void onPlayerRespawn(EntityPlayer player) {
        PacketHandlerBackpack.sendWornBackpackDataToClient(player);
    }

    public ItemStack getBackpack(EntityPlayer player) {
        return getPlayerSave(player.username).getWornBackpack();
    }
    
    public void setBackpack(EntityPlayer player, ItemStack wornBackpack) {
        getPlayerSave(player.username).setWornBackpack(wornBackpack);
    }
    
    protected PlayerSave getPlayerSave(String username) {
        PlayerSave playerSave = playerSaves.get(username);
        if(playerSave == null) {
            playerSave = new PlayerSave(username, new File(getWorldSaveDir(), "backpacks/players"));
            playerSaves.put(username, playerSave);
        }
        return playerSave;
    }
    
    protected File getWorldSaveDir() {
        if(worldSaveDir == null) {
            worldSaveDir = DimensionManager.getCurrentSaveRootDirectory();
        }
        return worldSaveDir;
    }

    public void savePlayerData() {
        for(String username : playerSaves.keySet()) {
            playerSaves.remove(username).save();
        }
    }
}