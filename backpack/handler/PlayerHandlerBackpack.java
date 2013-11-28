package backpack.handler;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.DimensionManager;
import backpack.item.ItemInfo;
import backpack.util.NBTUtil;
import backpack.util.PlayerSave;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlayerHandlerBackpack {
    protected ConcurrentHashMap<String, PlayerSave> playerSaves = new ConcurrentHashMap<String, PlayerSave>();
    protected File worldSaveDir = null;
    protected ItemStack clientBackpack;

    /**
     * Loads a players data. Moves the old backpack from the player to it's own
     * save file. Send the backpack data to the client.
     * 
     * @param player
     *            The player who should be loaded.
     */
    public void loadPlayer(EntityPlayer player) {
        PlayerSave playerSave = getPlayerSave(player.username);

        // backwards compatibility
        NBTTagCompound playerData = player.getEntityData();
        if(playerData.hasKey("backpack")) {
            ItemStack backpack = ItemStack.loadItemStackFromNBT(playerData.getCompoundTag("backpack"));
            NBTUtil.setString(backpack, ItemInfo.UID, UUID.randomUUID().toString());
            playerSave.setWornBackpack(backpack);
            playerData.removeTag("backpack");
        }

        PacketHandlerBackpack.sendWornBackpackDataToClient(player);
    }

    /**
     * Saves the players data to his file and removes it from the loaded saves.
     * 
     * @param player
     *            The player who should be unloaded.
     */
    public void unloadPlayer(EntityPlayer player) {
        savePlayer(player);
        playerSaves.remove(player.username);
    }

    /**
     * Saves the players data to his file.
     * 
     * @param player
     *            The player who should be saved.
     */
    public void savePlayer(EntityPlayer player) {
        getPlayerSave(player.username).save();
    }

    public void saveAllPlayerData() {
    /**
     * Saves the data of all players and removes them all from the loaded saves.
     */
        for(String username : playerSaves.keySet()) {
            playerSaves.remove(username).save(true);
        }
    }

    /**
     * Returns the worn backpack for the given player.
     * 
     * @param player
     *            The player whose backpack should be returned.
     * @return An ItemStack with the worn backpack or null if there is no
     *         backpack.
     */
    public ItemStack getBackpack(EntityPlayer player) {
        return getPlayerSave(player.username).getWornBackpack();
    }

    /**
     * Sets the backpack for the given player and saves the data.
     * 
     * @param player
     *            The player who should get the backpack.
     * @param backpack
     *            The ItemStack of the backpack.
     */
    public void setBackpack(EntityPlayer player, ItemStack backpack) {
        getPlayerSave(player.username).setWornBackpack(backpack);
        savePlayer(player);
    }

    /**
     * Get the backpack on the client side. Used to determine the correct
     * inventory for the GUI.
     * 
     * @return The ItemStack of the currently worn backpack or null if the
     *         player doesn't has one.
     */
    @SideOnly(Side.CLIENT)
    public ItemStack getClientBackpack() {
        return clientBackpack;
    }

    /**
     * Sets the backpack for the current player. As this is only client side
     * there is only one player.
     * 
     * @param backpack
     *            The backpack that should be set for the player.
     */
    @SideOnly(Side.CLIENT)
    public void setClientBackpack(ItemStack backpack) {
        clientBackpack = backpack;
    }

    /**
     * Retrieves what version of this mod the player has seen last.
     * 
     * @param player
     *            The player that should be checked.
     * @return The latest seen version as a String.
     */
    public String getLatestSeenVersion(EntityPlayer player) {
        return getPlayerSave(player.username).getLatestSeenVersion();
    }

    /**
     * Sets the latest seen version of this mod for the given player.
     * 
     * @param player
     *            The player that should get set the latest version.
     * @param latestVersion
     *            The latest version of this mod.
     */
    public void setLatestSeenVersion(EntityPlayer player, String latestVersion) {
        getPlayerSave(player.username).setLatestSeenVersion(latestVersion);
        savePlayer(player);
    }

    /**
     * Returns the save for the given player. If no save already loaded, load
     * it.
     * 
     * @param username
     *            The username of the player.
     * @return Returns the save of the given player.
     */
    protected PlayerSave getPlayerSave(String username) {
        PlayerSave playerSave = playerSaves.get(username);
        if(playerSave == null) {
            playerSave = new PlayerSave(username, new File(getWorldSaveDir(), "backpacks/players"));
            playerSaves.put(username, playerSave);
        }
        return playerSave;
    }

    /**
     * Returns the save game folder for the loaded world.
     * 
     * @return A File object pointing to the folder for the current world.
     */
    protected File getWorldSaveDir() {
        if(worldSaveDir == null) {
            worldSaveDir = DimensionManager.getCurrentSaveRootDirectory();
        }
        return worldSaveDir;
    }
}