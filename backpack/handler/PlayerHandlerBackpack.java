package backpack.handler;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import backpack.util.NBTUtil;
import backpack.util.PlayerSave;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlayerHandlerBackpack {
    protected ConcurrentHashMap<String, PlayerSave> playerSaves = new ConcurrentHashMap<String, PlayerSave>();
    protected File worldSaveDir = null;
    protected ItemStack clientBackpack;

    public void loadPlayer(EntityPlayer player) {
        PlayerSave playerSave = getPlayerSave(player.username);

        // backwards compatibility
        NBTTagCompound playerData = player.getEntityData();
        if(playerData.hasKey("backpack")) {
            ItemStack backpack = ItemStack.loadItemStackFromNBT(playerData.getCompoundTag("backpack"));
            NBTUtil.setString(backpack, "UID", UUID.randomUUID().toString());
            playerSave.setWornBackpack(backpack);
            playerData.removeTag("backpack");
        }

        PacketHandlerBackpack.sendWornBackpackDataToClient(player);
    }

    public void unloadPlayer(EntityPlayer player) {
        savePlayer(player);
        playerSaves.remove(player.username);
    }

    public void savePlayer(EntityPlayer player) {
        getPlayerSave(player.username).save();
    }

    public void saveAllPlayerData() {
        for(String username : playerSaves.keySet()) {
            playerSaves.remove(username).save();
        }
    }

    public ItemStack getBackpack(EntityPlayer player) {
        return getPlayerSave(player.username).getWornBackpack();
    }

    public void setBackpack(EntityPlayer player, ItemStack backpack) {
        getPlayerSave(player.username).setWornBackpack(backpack);
        savePlayer(player);
    }

    @SideOnly(Side.CLIENT)
    public ItemStack getClientBackpack() {
        return clientBackpack;
    }

    @SideOnly(Side.CLIENT)
    public void setClientBackpack(ItemStack backpack) {
        clientBackpack = backpack;
    }

    public String getLatestSeenVersion(EntityPlayer player) {
        return getPlayerSave(player.username).getLatestSeenVersion();
    }

    public void setLatestSeenVersion(EntityPlayer player, String latestVersion) {
        getPlayerSave(player.username).setLatestSeenVersion(latestVersion);
        savePlayer(player);
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
}