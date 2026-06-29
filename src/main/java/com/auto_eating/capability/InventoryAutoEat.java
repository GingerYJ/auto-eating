package com.auto_eating.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class InventoryAutoEat implements IInventory {

    public static final int SLOT_COUNT = 3;

    private final NonNullList<ItemStack> slots = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private boolean dirty = false;

    @Override
    public int getSizeInventory() {
        return SLOT_COUNT;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= 0 && index < SLOT_COUNT ? slots.get(index) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(slots, index, count);
        if (!stack.isEmpty()) {
            this.markDirty();
        }
        return stack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (!slots.get(index).isEmpty()) {
            ItemStack stack = slots.get(index);
            slots.set(index, ItemStack.EMPTY);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        slots.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        this.markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
        this.dirty = true;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof net.minecraft.item.ItemFood;
    }

    @Override
    public int getField(int id) { return 0; }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() { return 0; }

    @Override
    public void clear() {
        slots.clear();
    }

    @Override
    public String getName() {
        return "container.auto_eating.title";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(this.getName());
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : slots) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    public void saveNBTData(NBTTagCompound tags) {
        ItemStackHelper.saveAllItems(tags, this.slots);
    }

    public void loadNBTData(NBTTagCompound tags) {
        this.slots.clear();
        ItemStackHelper.loadAllItems(tags, this.slots);
    }
}
