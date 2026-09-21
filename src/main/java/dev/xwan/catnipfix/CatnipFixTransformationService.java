package dev.xwan.catnipfix;

import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformer;

import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.List;

public class CatnipFixTransformationService implements ITransformationService {
    public static final String NAME = "catnipfixtransformer";
    public static final List<ITransformer> TRANSFORMERS = List.of(CatnipFixTransformer.TRANSFORMER);
    public static final ITransformationService TRANSFORMATION_SERVICE = new CatnipFixTransformationService();

    @Override
    public @NotNull String name() {
        return NAME;
    }

    @Override
    public void initialize(IEnvironment env) {
        // do nothing
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) {
        // do nothing
    }

    @Override
    public @NotNull List<ITransformer> transformers() {
        return TRANSFORMERS;
    }
}
