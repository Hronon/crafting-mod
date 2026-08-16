package com.hronon.limitlesscrafting.registries;

import com.hronon.limitlesscrafting.recipes.WorkbenchRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hronon.limitlesscrafting.LimitlessCraft.MODID;

public class Recipes
{
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    public static final RegistryObject<RecipeSerializer<WorkbenchRecipe>> WORKBENCH = SERIALIZERS.register(
            "workbench_limitless",
            () -> WorkbenchRecipe.Serializer.INSTANCE
    );

    public static void register(IEventBus bus)
    {
        SERIALIZERS.register(bus);
    }
}
