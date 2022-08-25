package com.almostreliable.morejs.mixin.structure;

import com.almostreliable.morejs.features.structure.StructureTemplateAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(StructureTemplate.class)
public class StructureTemplateMixin implements StructureTemplateAccess {

    @Shadow @Final private List<StructureTemplate.Palette> palettes;

    @Shadow @Final private List<StructureTemplate.StructureEntityInfo> entityInfoList;

    @Shadow private Vec3i size;

    @Override
    public List<StructureTemplate.Palette> getPalettes() {
        return this.palettes;
    }

    @Override
    public List<StructureTemplate.StructureEntityInfo> getEntities() {
        return this.entityInfoList;
    }

    @Override
    public Vec3i getBorderSize() {
        return this.size;
    }
}
