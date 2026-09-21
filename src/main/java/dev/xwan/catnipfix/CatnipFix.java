package dev.xwan.catnipfix;

import net.minecraftforge.fml.common.Mod;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import cpw.mods.modlauncher.TransformingClassLoader;
import cpw.mods.modlauncher.ClassTransformer;
import cpw.mods.modlauncher.TransformStore;
import cpw.mods.modlauncher.TransformTargetLabel;
import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformationService;

import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

@Mod("catnipfix")
public class CatnipFix {
    public static final Logger LOGGER = LogManager.getLogger("catnipfix");

    static {
        commitCrimes();
    }

    private static void commitCrimes() {
        LOGGER.info("Definitely not up to no good");

        ClassLoader _cl = Thread.currentThread().getContextClassLoader();

        if (!(_cl instanceof TransformingClassLoader)) {
            throw new IllegalStateException("context class loader is not a TransformingClassLoader");
        }

        TransformingClassLoader cl = (TransformingClassLoader)_cl;

        Field classTransformerField;
        try {
            classTransformerField = TransformingClassLoader.class.getDeclaredField("classTransformer");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("couldnt get classTransformer field", e);
        }

        if (classTransformerField.getType() != ClassTransformer.class) {
            throw new IllegalStateException("classTransformer is not a ClassTransformer");
        }

        classTransformerField.setAccessible(true);

        ClassTransformer classTransformer;
        try {
            classTransformer = (ClassTransformer)classTransformerField.get(cl);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("couldnt get value of classTransformer field", e);
        }

        Field transformersField;
        try {
            transformersField = ClassTransformer.class.getDeclaredField("transformers");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("couldnt get transformers field", e);
        }

        if (transformersField.getType() != TransformStore.class) {
            throw new IllegalStateException("transformers is not a TransformStore");
        }

        transformersField.setAccessible(true);

        TransformStore transformers;
        try {
            transformers = (TransformStore)transformersField.get(classTransformer);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("couldnt get value of transformers field", e);
        }

        Constructor transformTargetLabelConstructor;
        try {
            transformTargetLabelConstructor = TransformTargetLabel.class.getDeclaredConstructor(ITransformer.Target.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("couldnt get TransformTargetLabel constructor", e);
        }

        transformTargetLabelConstructor.setAccessible(true);

        Method addTransformerMethod;
        try {
            addTransformerMethod = TransformStore.class.getDeclaredMethod("addTransformer", TransformTargetLabel.class, ITransformer.class, ITransformationService.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("couldnt get addTransformer method", e);
        }

        addTransformerMethod.setAccessible(true);

        TransformTargetLabel targetLabel;
        try {
            targetLabel = (TransformTargetLabel)transformTargetLabelConstructor.newInstance(CatnipFixTransformer.TARGET);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("couldnt create TransformTargetLabel", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("error occured in TransformTargetLabel constructor", e.getCause());
        }

        try {
            addTransformerMethod.invoke(transformers, targetLabel, CatnipFixTransformer.TRANSFORMER, CatnipFixTransformationService.TRANSFORMATION_SERVICE);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("couldnt invoke addTransformer", e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("error occured in addTransformer", e.getCause());
        }

        LOGGER.info("Successfully commited crimes against forge");
    }
}
