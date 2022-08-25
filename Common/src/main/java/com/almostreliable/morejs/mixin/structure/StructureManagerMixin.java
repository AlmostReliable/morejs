package com.almostreliable.morejs.mixin.structure;

import com.almostreliable.morejs.features.structure.StructureLoadEventJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(StructureManager.class)
public class StructureManagerMixin {

    @Inject(method = "loadFromResource", at = @At("RETURN"))
    private void morejs$invokeEventFromResource(ResourceLocation id, CallbackInfoReturnable<Optional<StructureTemplate>> cir) {
        cir.getReturnValue().ifPresent(structureTemplate -> StructureLoadEventJS.invoke(structureTemplate, id));
    }

    @Inject(method = "loadFromGenerated", at = @At("RETURN"))
    private void morejs$invokeEventFromGenerated(ResourceLocation id, CallbackInfoReturnable<Optional<StructureTemplate>> cir) {
        cir.getReturnValue().ifPresent(structureTemplate -> StructureLoadEventJS.invoke(structureTemplate, id));
    }
}
