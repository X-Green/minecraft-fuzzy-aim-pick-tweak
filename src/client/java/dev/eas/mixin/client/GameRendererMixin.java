package dev.eas.mixin.client;

import dev.eas.mixin_interface.IEntity;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static dev.eas.config.Configs.FeatureConfigs.ENABLE_FUZZY_AIM;
import static dev.eas.config.Configs.FeatureConfigs.FUZZY_AIM_RADIUS;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Unique
    private final List<HitResult> sampledHitResults = new ArrayList<>(9);

    @Redirect(
            method = "pick(Lnet/minecraft/world/entity/Entity;DDF)Lnet/minecraft/world/phys/HitResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;pick(DFZ)Lnet/minecraft/world/phys/HitResult;")
    )
    private HitResult replacedHitResult(Entity instance, double d, float f, boolean bl) {
        if (!ENABLE_FUZZY_AIM.getBooleanValue()) {
            return instance.pick(d, f, bl);
        }
        float offset = FUZZY_AIM_RADIUS.getFloatValue();

        // Sample for 9 Times. (yaw, pitch) = (0,0), (10,0), (-10,0), (0,10), (0,-10), (10,10), (10,-10), (-10,10), (-10,-10)
        sampledHitResults.clear();
        for (int iYaw = -1; iYaw <= 1; iYaw++) {
            for (int jPitch = -1; jPitch <= 1; jPitch++) {
                sampledHitResults.add(
                        ((IEntity) instance).minecraft_fuzzy_aim_pick_tweak$pickWithOffsetYawPitch(
                                d, f, bl,
                                iYaw * offset,
                                jPitch * offset
                        )
                );
            }
        }

        return sampledHitResults.stream().min(
                Comparator.comparingDouble(
                        hr -> hr.getLocation().distanceToSqr(instance.getEyePosition())
                )
        ).orElse(null); // Fancy Java way to get the minimum element of a list
    }

}