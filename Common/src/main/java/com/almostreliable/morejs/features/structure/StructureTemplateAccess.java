package com.almostreliable.morejs.features.structure;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;

public interface StructureTemplateAccess {


    List<StructureTemplate.Palette> getPalettes();

    List<StructureTemplate.StructureEntityInfo> getEntities();

    Vec3i getBorderSize();
}
