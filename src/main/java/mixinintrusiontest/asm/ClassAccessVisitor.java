package mixinintrusiontest.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ClassAccessVisitor extends ClassVisitor {
    public ClassAccessVisitor(int api) {
        super(api);
    }
    public ClassAccessVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        access = (access & (~Opcodes.ACC_PRIVATE)) | Opcodes.ACC_PUBLIC;
        System.out.println("publicising " + name);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        if (name != null && name.startsWith("<init>"))
            if ((access & Opcodes.ACC_PUBLIC) == 0)
                access = ((access & (~Opcodes.ACC_PRIVATE))) | Opcodes.ACC_PROTECTED;

        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }


}
