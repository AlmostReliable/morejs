package com.almostreliable.morejs.features.structure;

import dev.latvian.mods.kubejs.level.LevelEventJS;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.phys.AABB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StructureAfterPlaceEventJS extends LevelEventJS {

    private final Structure structure;
    private final WorldGenLevel worldGenLevel;
    private final StructureManager structureManager;
    private final ChunkGenerator chunkGenerator;
    private final RandomSource randomSource;
    private final BoundingBox boundingBox;
    private final ChunkPos chunkPos;
    private final PiecesContainer piecesContainer;
    private Map<StructurePiece, BoundingBox> intersectionMap;

    public StructureAfterPlaceEventJS(Structure structure, WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, PiecesContainer piecesContainer) {
        this.structure = structure;
        this.worldGenLevel = worldGenLevel;
        this.structureManager = structureManager;
        this.chunkGenerator = chunkGenerator;
        this.randomSource = randomSource;
        this.boundingBox = boundingBox;
        this.chunkPos = chunkPos;
        this.piecesContainer = piecesContainer;
    }

    public Structure getStructure() {
        return structure;
    }

    public StructureManager getStructureManager() {
        return structureManager;
    }

    public ChunkGenerator getChunkGenerator() {
        return chunkGenerator;
    }

    public RandomSource getRandomSource() {
        return randomSource;
    }

    public BoundingBox getChunkBoundingBox() {
        return boundingBox;
    }

    public ChunkPos getChunkPos() {
        return chunkPos;
    }

    public PiecesContainer getPiecesContainer() {
        return piecesContainer;
    }

    public BoundingBox getStructureBoundingBox() {
        return piecesContainer.calculateBoundingBox();
    }

    public ServerLevel getLevel() {
        return worldGenLevel.getLevel();
    }

    public WorldGenLevel getWorldGenLevel() {
        return worldGenLevel;
    }

    public ResourceLocation getPieceType(StructurePieceType pieceType) {
        return Objects.requireNonNull(BuiltInRegistries.STRUCTURE_PIECE.getKey(pieceType));
    }

    public ResourceLocation getId() {
        return Objects.requireNonNull(structureManager
                .registryAccess()
                .registryOrThrow(Registries.STRUCTURE)
                .getKey(structure));
    }

    public String getGenStep() {
        return structure.step().getName();
    }

    public List<BoundingBox> getIntersectionBoxes() {
        return getIntersectionMap().values().stream().toList();
    }

    public List<StructurePiece> getIntersectionPieces() {
        return getIntersectionMap().keySet().stream().toList();
    }

    public Map<StructurePiece, BoundingBox> getIntersectionMap() {
        if (intersectionMap == null) {
            Map<StructurePiece, BoundingBox> map = new HashMap<>();
            for (StructurePiece sp : piecesContainer.pieces()) {
                if (boundingBox.intersects(sp.getBoundingBox())) {
                    AABB aabb = AABB.of(boundingBox).intersect(AABB.of(sp.getBoundingBox()));
                    map.put(sp,
                            new BoundingBox((int) aabb.minX,
                                    (int) aabb.minY,
                                    (int) aabb.minZ,
                                    (int) aabb.maxX - 1,
                                    (int) aabb.maxY - 1,
                                    (int) aabb.maxZ - 1));
                }
            }
            intersectionMap = map;
        }
        return intersectionMap;
    }

    public ResourceLocation getType() {
        return Objects.requireNonNull(BuiltInRegistries.STRUCTURE_TYPE.getKey(structure.type()));
    }
}
