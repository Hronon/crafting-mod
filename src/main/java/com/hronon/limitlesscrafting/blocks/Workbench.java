package com.hronon.limitlesscrafting.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

public class Workbench extends Block implements EntityBlock
{
    public Workbench(Properties props)
    {
        super(props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new WorkbenchBlockEntity(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState new_state, boolean moving)
    {
        if (state.getBlock() != new_state.getBlock())
        {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof WorkbenchBlockEntity)
            {
                ((WorkbenchBlockEntity) be).drops();
            }
        }

        super.onRemove(state, level, pos, new_state, moving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide)
        {
            return InteractionResult.sidedSuccess(level.isClientSide());
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof WorkbenchBlockEntity)) return InteractionResult.PASS;

        NetworkHooks.openScreen(
                (ServerPlayer) player,
                (MenuProvider) be,
                pos
        );

        return InteractionResult.CONSUME;
    }
}
