package com.hronon.limitlesscrafting.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hronon.limitlesscrafting.LimitlessCraft.MODID;

public class Items
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final RegistryObject<Item> WORKBENCH = ITEMS.register(
            "workbench",
            () -> new BlockItem(
                    Blocks.WORKBENCH.get(),
                    new Item.Properties()
            )
    );

    public static void register(IEventBus bus)
    {
        ITEMS.register(bus);
    }
}
