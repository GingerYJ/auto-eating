package com.auto_eating.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class AutoEatStorage implements Capability.IStorage<AutoEatData> {

    @Override
    public NBTBase writeNBT(Capability<AutoEatData> capability, AutoEatData instance, EnumFacing side) {
        NBTTagCompound tags = new NBTTagCompound();
        instance.saveNBTData(tags);
        return tags;
    }

    @Override
    public void readNBT(Capability<AutoEatData> capability, AutoEatData instance, EnumFacing side, NBTBase nbt) {
        instance.loadNBTData((NBTTagCompound) nbt);
    }
}
