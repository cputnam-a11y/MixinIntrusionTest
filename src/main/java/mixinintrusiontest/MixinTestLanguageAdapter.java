package mixinintrusiontest;

import mixinintrusiontest.transformers.AClassFileTransformer;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.api.LanguageAdapterException;
import net.fabricmc.loader.api.ModContainer;
import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

public class MixinTestLanguageAdapter implements LanguageAdapter {
    static List<String> CLASSES_TO_PUBLICIZE = List.of(
            "org/spongepowered/asm/mixin/transformer/MixinCoprocessor",
            "org/spongepowered/asm/mixin/transformer/MixinCoprocessor$ProcessResult",
            "org/spongepowered/asm/mixin/transformer/MixinInfo"
    );
    static List<String> DO_ANYTHING_WITH = new ArrayList<>() {{
        addAll(CLASSES_TO_PUBLICIZE);
        add("org/spongepowered/asm/mixin/transformer/MixinProcessor");
    }};
    static {
        ByteBuddyAgent.install();
        Instrumentation i = ByteBuddyAgent.getInstrumentation();
//        i.addTransformer(new AClassFileTransformer());
    }

    @Override
    public <T> T create(ModContainer mod, String value, Class<T> type) throws LanguageAdapterException {
        throw new Error("MixinTestLanguageAdapter does nothing");
    }
}
