package dev.eas.mixin_interface;

import net.minecraft.world.phys.HitResult;

public interface IEntity {
    default HitResult minecraft_fuzzy_aim_pick_tweak$pickWithOffsetYawPitch(double reach, float timeFactor, boolean checkFluid, float yawOffset, float pitchOffset) {
        return null;
    }
}
