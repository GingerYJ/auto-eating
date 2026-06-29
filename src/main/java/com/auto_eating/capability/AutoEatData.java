package com.auto_eating.capability;

import net.minecraft.nbt.NBTTagCompound;

public class AutoEatData {

    public final InventoryAutoEat inventory = new InventoryAutoEat();
    public short foodleft = 0;
    public float lastSaturation = 1.0f;

    public void saveNBTData(NBTTagCompound tags) {
        this.inventory.saveNBTData(tags);
        tags.setShort("foodleft", this.foodleft);
        tags.setFloat("lastSaturation", this.lastSaturation);
    }

    public void loadNBTData(NBTTagCompound tags) {
        this.inventory.loadNBTData(tags);
        this.foodleft = tags.getShort("foodleft");
        this.lastSaturation = tags.getFloat("lastSaturation");
    }
}
