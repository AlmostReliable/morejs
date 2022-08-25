package com.almostreliable.morejs.features.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Map;

public interface StructureBlockInfoModification {
    String getId();

    Block getBlock();

    void setBlock(ResourceLocation id);

    void setBlock(ResourceLocation id, Map<String, Object> properties);

    Map<String, Object> getProperties();

    boolean hasNbt();

    @Nullable
    CompoundTag getNbt();

    void setNbt(@Nullable CompoundTag nbt);

    BlockPos getPosition();

    void setVanillaBlockState(BlockState state);
}
