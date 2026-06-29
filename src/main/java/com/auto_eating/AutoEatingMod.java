package com.auto_eating;

import com.auto_eating.capability.AutoEatData;
import com.auto_eating.capability.AutoEatProvider;
import com.auto_eating.capability.AutoEatStorage;
import com.auto_eating.proxy.IProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION,
     acceptedMinecraftVersions = Reference.MCVERSION)
public class AutoEatingMod {

    @Mod.Instance
    public static AutoEatingMod instance;

    @SidedProxy(clientSide = "com.auto_eating.proxy.ClientProxy",
                serverSide = "com.auto_eating.proxy.CommonProxy")
    public static IProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(
            AutoEatData.class,
            new AutoEatStorage(),
            AutoEatData::new
        );

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(AutoEatProvider.ID, new AutoEatProvider());
        }
    }
}