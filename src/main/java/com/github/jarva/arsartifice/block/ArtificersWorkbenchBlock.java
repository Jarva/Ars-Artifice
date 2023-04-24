package com.github.jarva.arsartifice.block;

import com.github.jarva.arsartifice.client.ArtificeCreationMenu;
import com.github.jarva.arsartifice.item.ArtificerCurio;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ArtificersWorkbenchBlock extends TableBlock {
    public ArtificersWorkbenchBlock() {
        super();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide && player.getItemInHand(hand).getItem() instanceof ArtificerCurio) {
            Minecraft.getInstance().setScreen(new ArtificeCreationMenu(hand));
            return InteractionResult.sidedSuccess(true);
        }
        return super.use(state, level, pos, player, hand, hit);
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}

