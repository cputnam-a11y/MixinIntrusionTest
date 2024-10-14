package mixinintrusiontest.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Function;

public class CollectingClassVisitorBuilder {
    ArrayList<ClassVisitorFactory> visitors = new ArrayList<>();
    private ClassVisitor startVisitor = null;
    private ClassVisitor endVisitor = null;
    private ClassWriter writer = null;

    public CollectingClassVisitorBuilder() {
    }

    private CollectingClassVisitorBuilder(ArrayList<ClassVisitorFactory> visitors, ClassWriter writer) {
        this.writer = writer;
        this.visitors = new ArrayList<>(visitors);
    }

    public CollectingClassVisitorBuilder add(SimpleClassVisitorFactory visitor) {
        visitors.add(visitor);
        return this;
    }

    public CollectingClassVisitorBuilder add(ComplexClassVisitorFactory visitor) {
        visitors.add(visitor);
        return this;
    }

    public CollectingClassVisitorBuilder addWriter(ClassWriter writer) {
        if (this.writer != null) {
            throw new IllegalStateException("Writer Already Added");
        }
        this.writer = writer;
        return this;
    }

    public CollectingClassVisitorBuilder className(String className) {
        return this;
    }

    public CollectingClassVisitorBuilder copy() {
        return new CollectingClassVisitorBuilder(visitors, writer);
    }

    public ClassVisitor build() {
        CollectingClassVisitorBuilder $this = this.copy();
        if ($this.writer != null) {
            $this.startVisitor = $this.writer;
            $this.endVisitor = $this.startVisitor;
        }
        for (ClassVisitorFactory factory : $this.visitors.reversed()) {
            if ($this.startVisitor == null) {
                $this.startVisitor = ClassVisitorFactory.apply(factory, null, null);
                $this.endVisitor = $this.startVisitor;
            } else {
                $this.endVisitor = ClassVisitorFactory.apply(factory, $this.endVisitor, null);
            }
        }
        return $this.endVisitor;
    }

    public ClassVisitor build(String className) {
        CollectingClassVisitorBuilder $this = this.copy();
        if ($this.writer != null) {
            $this.startVisitor = $this.writer;
            $this.endVisitor = $this.startVisitor;
        }
        for (ClassVisitorFactory factory : $this.visitors.reversed()) {
            if ($this.startVisitor == null) {
                $this.startVisitor = ClassVisitorFactory.apply(factory, null, className);
                $this.endVisitor = $this.startVisitor;
            } else {
                $this.endVisitor = ClassVisitorFactory.apply(factory, $this.endVisitor, className);
            }
        }
        return $this.endVisitor;
    }

    sealed interface ClassVisitorFactory {
        private static ClassVisitor apply(ClassVisitorFactory f, ClassVisitor visitor, String className) {
            switch (f) {
                case SimpleClassVisitorFactory simpleFactory -> {
                    return simpleFactory.create(visitor);
                }
                case ComplexClassVisitorFactory complexFactory -> {
                    if (className == null)
                        throw new IllegalStateException("ComplexClassVisitorFactory requires a class name, no class name was provided");
                    return complexFactory.create(visitor, className);
                }
            }
        }
    }

    @FunctionalInterface
    public non-sealed interface SimpleClassVisitorFactory extends ClassVisitorFactory {
        ClassVisitor create(ClassVisitor delegate);
    }

    @FunctionalInterface
    public non-sealed interface ComplexClassVisitorFactory extends ClassVisitorFactory {
        ClassVisitor create(ClassVisitor delegate, String className);
    }
}
