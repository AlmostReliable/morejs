package com.almostreliable.morejs.features.structure;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EntityInfoWrapper {
    private final List<StructureTemplate.StructureEntityInfo> entities;
    private final Vec3i borderSize;

    public EntityInfoWrapper(List<StructureTemplate.StructureEntityInfo> entities, Vec3i borderSize) {
        this.entities = entities;
        this.borderSize = borderSize;
    }

    public void forEach(Consumer<StructureTemplate.StructureEntityInfo> consumer) {
        entities.forEach(consumer);
    }

    public void removeIf(Predicate<StructureTemplate.StructureEntityInfo> predicate) {
        entities.removeIf(predicate);
    }

    public void add(CompoundTag tag) {
        Preconditions.checkNotNull(tag, "Invalid tag");

        if (!tag.contains("id")) {
            throw new IllegalArgumentException("Invalid tag, missing entity id");
        }

        ListTag motionTag = tag.getList("Motion", 6);
        if (motionTag.size() != 3) {
            throw new IllegalArgumentException("Invalid or missing tag, `Motion` tag must have 3 entries");
        }

        ListTag rotationTag = tag.getList("Rotation", 5);
        if (rotationTag.size() != 2) {
            throw new IllegalArgumentException("Invalid or missing tag, `Rotation` tag must have 2 entries");
        }

        ListTag posTag = tag.getList("Pos", 6);
        if (posTag.size() != 3) {
            throw new IllegalArgumentException("Invalid or missing tag, `Pos` tag must have 3 entries");
        }

        Vec3 pos = new Vec3(
                Mth.clamp(posTag.getDouble(0), 0, borderSize.getX()),
                Mth.clamp(posTag.getDouble(1), 0, borderSize.getY()),
                Mth.clamp(posTag.getDouble(2), 0, borderSize.getZ())
        );
        BlockPos blockPos = new BlockPos(pos);
        this.entities.add(new StructureTemplate.StructureEntityInfo(pos, blockPos, tag));
    }
}
