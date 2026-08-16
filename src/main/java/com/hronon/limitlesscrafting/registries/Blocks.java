package com.hronon.limitlesscrafting.registries;

import com.hronon.limitlesscrafting.blocks.Workbench;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hronon.limitlesscrafting.LimitlessCraft.MODID;

public class Blocks
{
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> WORKBENCH = BLOCKS.register(
            "workbench",
            () -> new Workbench(
                    BlockBehaviour
                            .Properties
                            .of()
                            .mapColor(
                            MapColor.STONE
                    )
            )
    );

    public static void register(IEventBus bus)
    {
        BLOCKS.register(bus);
    }
}
