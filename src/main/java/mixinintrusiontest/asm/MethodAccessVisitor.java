package mixinintrusiontest.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodAccessVisitor extends ClassVisitor {
    final String[] names;
    public MethodAccessVisitor(int api, String... names) {
        super(api);
        this.names = names;
    }
    public MethodAccessVisitor(int api, ClassVisitor classVisitor, String... names) {
        super(api, classVisitor);
        this.names = names;
    }
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        for (String n : names) {
            if (n.equals(name)) {
                access = (access & (~Opcodes.ACC_PRIVATE)) | Opcodes.ACC_PROTECTED;
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
