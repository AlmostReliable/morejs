package com.almostreliable.morejs.features.structure;

import com.almostreliable.morejs.core.Events;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.function.Consumer;

public class StructureLoadEventJS extends EventJS {
    private final StructureTemplateAccess structure;
    private final ResourceLocation id;

    public StructureLoadEventJS(StructureTemplateAccess structure, ResourceLocation id) {
        this.structure = structure;
        this.id = id;

    }

    public Vec3i getStructureSize() {
        return structure.getBorderSize();
    }

    public String getId() {
        return id.toString();
    }

    public int getPalettesSize() {
        return structure.getPalettes().size();
    }

    public int getEntitiesSize() {
        return structure.getEntities().size();
    }

    public void removePalette(int index) {
        structure.getPalettes().remove(index);
    }

    public PaletteWrapper getPalette(int index) {
        return new PaletteWrapper(structure.getPalettes().get(index), structure.getBorderSize());
    }

    public void forEachPalettes(Consumer<PaletteWrapper> consumer) {
        structure.getPalettes().forEach(palette -> consumer.accept(new PaletteWrapper(palette, structure.getBorderSize())));
    }

    public EntityInfoWrapper getEntities() {
        return new EntityInfoWrapper(structure.getEntities(), structure.getBorderSize());
    }

    public static void invoke(StructureTemplate structure, ResourceLocation id) {
        if (structure instanceof StructureTemplateAccess sta) {
            new StructureLoadEventJS(sta, id).post(ScriptType.SERVER, Events.STRUCTURE_LOAD);
        }
    }
}
