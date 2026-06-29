package com.auto_eating.network;

import com.auto_eating.AutoEatingMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenAutoEatGui implements IMessage {

    public PacketOpenAutoEatGui() {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public void toBytes(ByteBuf buf) {}

    public static class Handler implements IMessageHandler<PacketOpenAutoEatGui, IMessage> {
        @Override
        public IMessage onMessage(PacketOpenAutoEatGui message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().player;
            player.getServerWorld().addScheduledTask(() -> {
                player.openGui(
                    AutoEatingMod.instance,
                    AutoEatingMod.GUI_AUTO_EAT,
                    player.world,
                    (int) player.posX,
                    (int) player.posY,
                    (int) player.posZ
                );
            });
            return null;
        }
    }
}