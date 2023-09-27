package com.almostreliable.morejs.mixin.structure;

import com.almostreliable.morejs.features.structure.StructureAfterPlaceEventJS;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.structures.DesertPyramidStructure;
import net.minecraft.world.level.levelgen.structure.structures.WoodlandMansionStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class StructuresMixin {
    @Mixin(Structure.class)
    public static class StructureMixin {
        @Inject(method = "afterPlace", at = @At("RETURN"))
        private void morejs$invokeEventAfterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer, CallbackInfo ci) {
            StructureAfterPlaceEventJS.invoke((Structure) (Object) this, worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, piecesContainer);
        }
    }

    @Mixin(DesertPyramidStructure.class)
    public static class DesertPyramidStructureMixin {
        @Inject(method = "afterPlace", at = @At("RETURN"))
        private void morejs$invokeEventAfterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer, CallbackInfo ci) {
            StructureAfterPlaceEventJS.invoke((Structure) (Object) this, worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, piecesContainer);
        }
    }

    @Mixin(WoodlandMansionStructure.class)
    public static class WoodlandMansionStructureMixin {
        @Inject(method = "afterPlace", at = @At("RETURN"))
        private void morejs$invokeEventAfterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer, CallbackInfo ci) {
            StructureAfterPlaceEventJS.invoke((Structure) (Object) this, worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, piecesContainer);
        }
    }
}
