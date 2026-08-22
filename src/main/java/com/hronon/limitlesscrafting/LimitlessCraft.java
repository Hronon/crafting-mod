package com.hronon.limitlesscrafting;

import com.hronon.limitlesscrafting.gui.WorkbenchScreen;
import com.hronon.limitlesscrafting.registries.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(LimitlessCraft.MODID)
public class LimitlessCraft
{
    public static final String MODID = "limitlesscrafting";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LimitlessCraft(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        Blocks.register(modEventBus);
        BlockEntities.register(modEventBus);
        Items.register(modEventBus);
        Recipes.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(Items.WORKBENCH);
    }
}
