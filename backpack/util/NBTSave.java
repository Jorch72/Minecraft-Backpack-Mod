package backpack.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class NBTSave {
    protected File saveFile = null;
    protected NBTTagCompound nbtData = null;
    protected boolean isDirty = false;

    public NBTSave(String fileName, File saveLocation) {
        saveFile = new File(saveLocation, fileName + ".dat");

        if(!saveFile.getParentFile().exists()) {
            saveFile.getParentFile().mkdirs();
        }

        load();
    }

    protected void load() {
        nbtData = new NBTTagCompound();

        DataInputStream inputStream = null;
        try {
            if(!saveFile.exists()) {
                saveFile.createNewFile();
            }
            if(saveFile.length() > 0) {
                inputStream = new DataInputStream(new FileInputStream(saveFile));
                nbtData = (NBTTagCompound) NBTBase.readNamedTag(inputStream);
            }
        }
        catch (Exception e) {}
        finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {}
            }
        }
    }

    public final void save() {
        save(false);
    }

    public final void save(boolean force) {
        if(!isDirty() && !force) {
            return;
        }

        DataOutputStream outputStream = null;
        try {
            outputStream = new DataOutputStream(new FileOutputStream(saveFile));
            NBTBase.writeNamedTag(nbtData, outputStream);
            setDirty(false);
        }
        catch (Exception e) {}
        finally {
            if(outputStream != null) {
                try {
                    outputStream.close();
                }
                catch(IOException e) {}
            }
        }
    }

    public final void setDirty(boolean state) {
        isDirty = state;
    }

    public final boolean isDirty() {
        return isDirty;
    }
}