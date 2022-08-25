package com.almostreliable.morejs.features.structure;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class PaletteWrapper {
    private final StructureTemplate.Palette palette;
    private final Vec3i borderSize;
    private final Map<BlockPos, StructureTemplate.StructureBlockInfo> cache = new HashMap<>();

    public PaletteWrapper(StructureTemplate.Palette palette, Vec3i borderSize) {
        this.palette = palette;
        this.borderSize = borderSize;
    }

    public void clear() {
        palette.blocks().clear();
        cache.clear();
    }

    public void add(BlockPos pos, BlockState state) {
        add(pos, state, null);
    }

    public void add(BlockPos pos, BlockState state, @Nullable CompoundTag tag) {
        Preconditions.checkNotNull(pos, "Invalid position");
        Preconditions.checkNotNull(state, "Invalid state");
        Preconditions.checkArgument(0 <= pos.getX() && pos.getX() < borderSize.getX(),
                "Invalid position, x must be between 0 and " + borderSize.getX());
        Preconditions.checkArgument(0 <= pos.getX() && pos.getY() < borderSize.getY(),
                "Invalid position, y must be between 0 and " + borderSize.getY());
        Preconditions.checkArgument(0 <= pos.getX() && pos.getZ() < borderSize.getZ(),
                "Invalid position, z must be between 0 and " + borderSize.getZ());

        StructureTemplate.StructureBlockInfo info = get(pos);
        if (info instanceof StructureBlockInfoModification mod) {
            mod.setVanillaBlockState(state);
            mod.setNbt(tag);
            return;
        }

        var newInfo = new StructureTemplate.StructureBlockInfo(pos, state, tag);
        palette.blocks().add(newInfo);
        cache.put(pos, newInfo);
    }

    public void forEach(Consumer<StructureTemplate.StructureBlockInfo> consumer) {
        palette.blocks().forEach(consumer);
    }

    public void removeIf(Predicate<StructureTemplate.StructureBlockInfo> predicate) {
        palette.blocks().removeIf(block -> {
            if (predicate.test(block)) {
                cache.remove(block.pos);
                return true;
            }
            return false;
        });
    }

    @Nullable
    public StructureTemplate.StructureBlockInfo get(BlockPos pos) {
        if (cache.isEmpty()) {
            forEach(info -> cache.put(info.pos, info));
        }

        return cache.get(pos);
    }
}
