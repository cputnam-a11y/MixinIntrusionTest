package mixinintrusiontest;

import org.spongepowered.asm.mixin.transformer.MixinCoprocessor;

public class CustomCoprocessor extends MixinCoprocessor {
    @Override
    protected String getName() {
        return "custom coprocessor";
    }

}
