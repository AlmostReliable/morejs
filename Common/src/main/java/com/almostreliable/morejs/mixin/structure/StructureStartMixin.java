package com.almostreliable.morejs.mixin.structure;

import com.almostreliable.morejs.core.Events;
import com.almostreliable.morejs.features.structure.StructureAfterPlaceEventJS;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StructureStart.class)
public class StructureStartMixin {

    @Shadow @Final private PiecesContainer pieceContainer;

    @Shadow @Final private Structure structure;

    @Inject(method = "placeInChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/structure/Structure;afterPlace(Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/StructureManager;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/world/level/levelgen/structure/pieces/PiecesContainer;)V", shift = At.Shift.AFTER))
    private void morejs$invokeEventAfterPlace(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, CallbackInfo ci) {
        if (!Events.STRUCTURE_AFTER_PLACE.hasListeners()) return;
        var event = new StructureAfterPlaceEventJS(this.structure,
                worldGenLevel,
                structureManager,
                chunkGenerator,
                randomSource,
                boundingBox,
                chunkPos,
                this.pieceContainer);
        Events.STRUCTURE_AFTER_PLACE.post(event);
    }
}
