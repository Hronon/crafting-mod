package com.hronon.limitlesscrafting.registries;

import com.hronon.limitlesscrafting.gui.WorkbenchMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hronon.limitlesscrafting.LimitlessCraft.MODID;

public class Menus
{
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

    public static final RegistryObject<MenuType<WorkbenchMenu>> WORKBENCH = MENUS.register(
            "workbench_menu",
            () -> IForgeMenuType.create(WorkbenchMenu::new)
    );

    public static void register(IEventBus bus)
    {
        MENUS.register(bus);
    }
}
