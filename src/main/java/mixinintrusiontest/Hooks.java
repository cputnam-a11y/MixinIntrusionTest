package mixinintrusiontest;

import org.spongepowered.asm.mixin.transformer.MixinCoprocessor;

import java.util.ArrayList;

public class Hooks {
    public static void registerCoprocessors(ArrayList<MixinCoprocessor> coprocessors) {
//        coprocessors.add(new CustomCoprocessor());
    }
}
