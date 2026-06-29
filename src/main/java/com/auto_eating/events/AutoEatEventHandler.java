package com.auto_eating.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.auto_eating.config.AutoEatConfig;
import com.auto_eating.capability.AutoEatData;
import com.auto_eating.capability.AutoEatProvider;
import com.auto_eating.capability.InventoryAutoEat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = com.auto_eating.Reference.MOD_ID)
public class AutoEatEventHandler {

    private static final Method ITEMFOOD_onFoodEaten;

    static {
        ITEMFOOD_onFoodEaten = ObfuscationReflectionHelper.findMethod(
            ItemFood.class, "func_77849_c", void.class, ItemStack.class, net.minecraft.world.World.class, EntityPlayer.class);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != Side.SERVER) return;
        if (event.phase != Phase.END) return;

        EntityPlayer player = event.player;
        if (player.isDead) return;

        // Auto-feeder disabled via config
        if (AutoEatConfig.disableAutofeeder) return;

        // Only auto-eat if food level <= 19 (not full)
        if (player.getFoodStats().getFoodLevel() >= 20) return;

        AutoEatData data = player.getCapability(AutoEatProvider.AUTO_EAT, null);
        if (data == null) return;

        int needed = 20 - player.getFoodStats().getFoodLevel();

        // If there is leftover food from a previous tick, consume it first
        if (data.foodleft > 0) {
            if (data.foodleft <= needed) {
                player.getFoodStats().addStats(data.foodleft, data.lastSaturation);
                data.foodleft = 0;
                data.lastSaturation = 0.0f;
            } else {
                player.getFoodStats().addStats(needed, data.lastSaturation);
                data.foodleft -= needed;
            }
            return;
        }

        // Search the food inventory for an ItemFood to consume
        for (int i = 0; i < InventoryAutoEat.SLOT_COUNT; i++) {
            ItemStack stack = data.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemFood) {
                ItemFood food = (ItemFood) stack.getItem();

                // Consume 1 item from the stack
                ItemStack consumed = stack.copy();
                consumed.setCount(1);
                stack.shrink(1);
                if (stack.isEmpty()) {
                    data.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                }

                // Invoke onFoodEaten for potion effects via reflection
                try {
                    ITEMFOOD_onFoodEaten.invoke(food, consumed, player.world, player);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }

                // Play burp sound
                player.world.playSound(null, player.posX, player.posY, player.posZ,
                    SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 1.0f, 1.0f);

                // Apply hunger and saturation, handle overflow
                int healAmount = food.getHealAmount(consumed);
                float saturationModifier = food.getSaturationModifier(consumed);

                short overflow = (short) (healAmount - needed);
                if (overflow > 0) {
                    player.getFoodStats().addStats(needed, saturationModifier);
                    data.foodleft = overflow;
                    data.lastSaturation = saturationModifier;
                } else {
                    player.getFoodStats().addStats(healAmount, saturationModifier);
                    data.foodleft = 0;
                    data.lastSaturation = 0.0f;
                }
                break;
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            AutoEatData data = player.getCapability(AutoEatProvider.AUTO_EAT, null);
            if (data != null) {
                data.foodleft = 0;
                data.lastSaturation = 0.0f;
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // Food data is already loaded from NBT via capability; no sync packet needed.
        // On login this just confirms the capability is attached.
    }
}
