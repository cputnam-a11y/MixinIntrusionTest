package mixinintrusiontest.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MixinProcessorConstructorHookVisitor extends ClassVisitor {
    public MixinProcessorConstructorHookVisitor(int api) {
        super(api);
    }

    public MixinProcessorConstructorHookVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor =
                super.visitMethod(access, name, descriptor, signature, exceptions);
        if (name.equals("<init>")) {
            return new MethodVisitor(Opcodes.ASM9, methodVisitor) {
                @Override
                public void visitInsn(int opcode) {
                    if (opcode == Opcodes.RETURN) {
                        mv.visitVarInsn(Opcodes.ALOAD, 0);
                        // private final MixinCoprocessors coprocessors = new MixinCoprocessors();
                        methodVisitor.visitFieldInsn(
                                Opcodes.GETFIELD,
                                "org/spongepowered/asm/mixin/transformer/MixinProcessor",
                                "coprocessors",
                                "Lorg/spongepowered/asm/mixin/transformer/MixinCoprocessors;"
                        );
                        methodVisitor.visitMethodInsn(
                                Opcodes.INVOKESTATIC,
                                "mixinintrusiontest/Hooks",
                                "registerCoprocessors",
                                "(Ljava/util/ArrayList;)V",
                                false
                        );
                    }

                    super.visitInsn(opcode);
                }
            };
        }
        return methodVisitor;
    }
}
