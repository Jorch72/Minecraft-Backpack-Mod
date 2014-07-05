package de.eydamos.backpack.helper;

import de.eydamos.backpack.Backpack;
import de.eydamos.backpack.misc.ConfigurationBackpack;

import java.util.*;

public class BackpackHelper {
    protected class Size {
        public int slots;
        public int columns = 9;
        public int scrollbar = 6;

        public Size(int slotAmount) {
            slots = slotAmount;
        }
    }

    protected List<String> materials = new ArrayList<String>();
    protected Map<Integer, HashMap<Integer, Size>> sizes = new HashMap<Integer, HashMap<Integer, Size>>();

    public static void init() {
        Backpack.backpackHelper.setMaterials(ConfigurationBackpack.MATERIALS.split("/,/"));
        Backpack.backpackHelper.addSizes(0, ConfigurationBackpack.BACKPACKS_S.split("/,/"));
        Backpack.backpackHelper.addSizes(1, ConfigurationBackpack.BACKPACKS_M.split("/,/"));
        Backpack.backpackHelper.addSizes(2, ConfigurationBackpack.BACKPACKS_L.split("/,/"));
        Backpack.backpackHelper.checkSizes();
    }

    protected void setMaterials(String[] materialArray) {
        materials.add("*leather*");
        materials.addAll(Arrays.asList(materialArray));
    }

    protected void addSizes(int tier, String[] sizesArr) {
        for(int i = 0; i < sizesArr.length; i++) {
            String[] sizeArr = sizesArr[i].split("/:/");
            Size size = new Size(Integer.valueOf(sizeArr[0].trim()));

            if(sizeArr.length >= 2 && !sizeArr[1].trim().isEmpty()) {
                size.columns = Integer.valueOf(sizeArr[1].trim());
            }
            if(sizeArr.length == 3 && !sizeArr[2].trim().isEmpty()) {
                size.scrollbar = Integer.valueOf(sizeArr[2].trim());
            }

            HashMap<Integer, Size> tierData = getTierData(tier);
            tierData.put(i, size);
        }
    }

    protected void checkSizes() {
        for(int tier = 2; tier >= 0; tier--) {
            HashMap<Integer, Size> tierData = getTierData(tier);
        }
    }

    protected HashMap<Integer, Size> getTierData(int tier) {
        if(!sizes.containsKey(tier)) {
            sizes.put(tier, new HashMap<Integer, Size>());
        }
        return sizes.get(tier);
    }
}
