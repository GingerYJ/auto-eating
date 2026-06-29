package com.auto_eating.proxy;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public interface IProxy {
    default void preInit(FMLPreInitializationEvent event) {}
    default void init(FMLInitializationEvent event) {}
}
