package com.hronon.limitlesscrafting.utils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemHelper {
    public static ItemStack from_string(String item_id)
    {
        var location = new ResourceLocation(item_id);
        return BuiltInRegistries.ITEM.get(location).getDefaultInstance();
    }
}
