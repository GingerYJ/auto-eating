package com.auto_eating.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import com.auto_eating.Reference;

public class AutoEatProvider implements ICapabilitySerializable<NBTTagCompound> {

    @CapabilityInject(AutoEatData.class)
    public static final Capability<AutoEatData> AUTO_EAT = null;

    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "auto_eat");

    private final AutoEatData instance;

    public AutoEatProvider() {
        this.instance = new AutoEatData();
    }

    public AutoEatProvider(AutoEatData instance) {
        this.instance = instance;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == AUTO_EAT;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        return capability == AUTO_EAT ? AUTO_EAT.cast(this.instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound tags = new NBTTagCompound();
        this.instance.saveNBTData(tags);
        return tags;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.instance.loadNBTData(nbt);
    }
}
