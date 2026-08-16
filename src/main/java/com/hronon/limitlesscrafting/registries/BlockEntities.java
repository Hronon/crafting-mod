package com.hronon.limitlesscrafting.registries;

import com.hronon.limitlesscrafting.blocks.WorkbenchBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.hronon.limitlesscrafting.LimitlessCraft.MODID;

public class BlockEntities
{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public static final RegistryObject<BlockEntityType<WorkbenchBlockEntity>> WORKBENCH = BLOCK_ENTITIES.register(
            "workbench_be",
            () -> BlockEntityType
                    .Builder
                    .of(
                            WorkbenchBlockEntity::new,
                            Blocks.WORKBENCH.get()
                    )
                    .build(null)
    );

    public static void register(IEventBus bus)
    {
        BLOCK_ENTITIES.register(bus);
    }
}
