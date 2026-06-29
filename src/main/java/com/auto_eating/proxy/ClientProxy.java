package com.auto_eating.proxy;

import com.auto_eating.AutoEatingMod;
import com.auto_eating.network.PacketOpenAutoEatGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {

    public static KeyBinding keyOpenAutoEat;

    @Override
    public void init(FMLInitializationEvent event) {
        keyOpenAutoEat = new KeyBinding(
            "key.auto_eating.open_gui", Keyboard.KEY_P, "key.auto_eating.category");
        ClientRegistry.registerKeyBinding(keyOpenAutoEat);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && keyOpenAutoEat.isPressed()) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            if (player != null && Minecraft.getMinecraft().inGameHasFocus) {
                AutoEatingMod.network.sendToServer(new PacketOpenAutoEatGui());
            }
        }
    }
}