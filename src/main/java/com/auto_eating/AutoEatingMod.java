package com.auto_eating;

import com.auto_eating.capability.AutoEatData;
import com.auto_eating.capability.AutoEatProvider;
import com.auto_eating.capability.AutoEatStorage;
import com.auto_eating.gui.ContainerAutoEat;
import com.auto_eating.gui.GuiAutoEat;
import com.auto_eating.network.PacketOpenAutoEatGui;
import com.auto_eating.proxy.IProxy;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.VERSION,
     acceptedMinecraftVersions = Reference.MCVERSION)
public class AutoEatingMod {

    @Mod.Instance
    public static AutoEatingMod instance;

    @SidedProxy(clientSide = "com.auto_eating.proxy.ClientProxy",
                serverSide = "com.auto_eating.proxy.CommonProxy")
    public static IProxy proxy;

    public static final int GUI_AUTO_EAT = 0;
    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        CapabilityManager.INSTANCE.register(
            AutoEatData.class, new AutoEatStorage(), AutoEatData::new);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new IGuiHandler() {
            @Override
            public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
                if (id == GUI_AUTO_EAT) return new ContainerAutoEat(player);
                return null;
            }
            @Override
            public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
                if (id == GUI_AUTO_EAT) return new GuiAutoEat(new ContainerAutoEat(player));
                return null;
            }
        });

        network = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_ID);
        network.registerMessage(PacketOpenAutoEatGui.Handler.class, PacketOpenAutoEatGui.class, 0, Side.SERVER);

        proxy.init(event);
    }

    @SubscribeEvent
    public void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(AutoEatProvider.ID, new AutoEatProvider());
        }
    }
}