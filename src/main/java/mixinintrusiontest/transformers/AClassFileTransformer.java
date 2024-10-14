package mixinintrusiontest.transformers;

import mixinintrusiontest.asm.ClassAccessVisitor;
import mixinintrusiontest.asm.CollectingClassVisitorBuilder;
import mixinintrusiontest.asm.MethodAccessVisitor;
import mixinintrusiontest.asm.MixinProcessorConstructorHookVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class AClassFileTransformer implements ClassFileTransformer {
    static List<String> CLASSES_TO_PUBLICIZE = List.of(
            "org/spongepowered/asm/mixin/transformer/MixinCoprocessor",
            "org/spongepowered/asm/mixin/transformer/MixinCoprocessor$ProcessResult",
            "org/spongepowered/asm/mixin/transformer/MixinInfo"
    );
    static List<String> TRANSFORMABLES = new ArrayList<>() {{
        addAll(CLASSES_TO_PUBLICIZE);
        add("org/spongepowered/asm/mixin/transformer/MixinProcessor");
    }};
    static CollectingClassVisitorBuilder b = new CollectingClassVisitorBuilder();
//            .add(
//                    (delegate, className1) ->
//                            CLASSES_TO_PUBLICIZE.contains(className1)
//                                    ? new ClassAccessVisitor(Opcodes.ASM9, delegate)
//                                    : delegate
//            ).add(
//                    (delegate, className) ->
//                            className.equals("org/spongepowered/asm/mixin/transformer/MixinCoprocessor")
//                            ? new MethodAccessVisitor(Opcodes.ASM9, delegate, "getName", "process")
//                            : delegate
//            ).add(
//                    (delegate, className) ->
//                            className.equals("org/spongepowered/asm/mixin/transformer/MixinProcessor")
//                            ? new MixinProcessorConstructorHookVisitor(Opcodes.ASM9, delegate)
//                            : delegate
//            );

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!TRANSFORMABLES.contains(className))
            return null;
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(reader, Opcodes.ASM9);
        reader.accept(
                b.copy().addWriter(writer).build(className),
                0
        );
        return writer.toByteArray();
    }
}
