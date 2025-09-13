package dev.eas.mixin_interface;

import net.minecraft.world.phys.HitResult;

public interface IEntity {
    default HitResult pick(double reach, float timeFactor, boolean checkFluid, float yawOffset, float pitchOffset) {
        return null;
    }
}
