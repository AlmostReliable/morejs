package com.almostreliable.missingname.util;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class ResourceOrTag {
    public static <T> Either<ResourceKey<T>, TagKey<T>> get(String s, ResourceKey<Registry<T>> registry) {
        if (s.startsWith("#")) {
            ResourceLocation rl = new ResourceLocation(s.substring(1));
            return Either.right(TagKey.create(registry, rl));
        } else {
            ResourceLocation rl = new ResourceLocation(s);
            return Either.left(ResourceKey.create(registry, rl));
        }
    }
}
