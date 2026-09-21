package dev.xwan.catnipfix;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import cpw.mods.modlauncher.api.ITransformerVotingContext;

import java.util.Set;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import static dev.xwan.catnipfix.CatnipFix.LOGGER;

public class CatnipFixTransformer implements ITransformer<MethodNode> {
    public static final ITransformer.Target TARGET = ITransformer.Target.targetMethod("net.createmod.catnip.render.StitchedSprite", "<clinit>", "()V");
    public static final Set<ITransformer.Target> TARGETS = Set.of(TARGET);
    public static final String[] LABELS = {"catnipfixtransformer"};
    public static final ITransformer TRANSFORMER = new CatnipFixTransformer();

    @Override
    public @NotNull MethodNode transform(MethodNode method, ITransformerVotingContext context) {
        LOGGER.info("Transforming net.createmod.catnip.render.StitchedSprite...");

        InsnList insn = new InsnList();

        insn.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/createmod/ponder/Ponder", "LOGGER", "Lorg/apache/logging/log4j/Logger;"));
        insn.add(new InsnNode(Opcodes.DUP));
        insn.add(new LdcInsnNode("catnipfix: Converting StitchedSprite.ALL to a synchronized map..."));
        insn.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V"));

        insn.add(new FieldInsnNode(Opcodes.GETSTATIC, "net/createmod/catnip/render/StitchedSprite", "ALL", "Ljava/util/Map;"));
        insn.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/util/Collections", "synchronizedMap", "(Ljava/util/Map;)Ljava/util/Map;"));
        insn.add(new FieldInsnNode(Opcodes.PUTSTATIC, "net/createmod/catnip/render/StitchedSprite", "ALL", "Ljava/util/Map;"));

        insn.add(new LdcInsnNode("catnipfix: Successfully converted StitchedSprite.ALL to a synchronized map!"));
        insn.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", "info", "(Ljava/lang/String;)V"));

        InsnList instructions = method.instructions;
        instructions.insertBefore(instructions.getLast(), insn);

        LOGGER.info("Done!");

        return method;
    }

    @Override
    public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
        return TransformerVoteResult.YES;
    }

    @Override
    public @NotNull Set<ITransformer.Target> targets() {
        return TARGETS;
    }

    @Override
    public String[] labels() {
        return LABELS;
    }
}
