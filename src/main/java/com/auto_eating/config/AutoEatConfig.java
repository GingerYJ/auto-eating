package com.auto_eating.config;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.Name;
import com.auto_eating.Reference;

@Config(modid = Reference.MOD_ID)
public class AutoEatConfig {

    @Name("Disable Auto Feeder")
    @Comment("Set to true to disable automatic food consumption from the auto-eat inventory slots.")
    public static boolean disableAutofeeder = false;
}
