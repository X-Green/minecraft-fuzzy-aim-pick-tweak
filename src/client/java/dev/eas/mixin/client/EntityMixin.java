package dev.eas.mixin.client;

import dev.eas.mixin_interface.IEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity {

    @Shadow
    public abstract Vec3 getEyePosition(float f);

    @Shadow
    public abstract float getViewXRot(float f);
    // XRot is pitch

    @Shadow
    public abstract float getViewYRot(float f);
    // YRot is yaw

    @Shadow
    public abstract Vec3 calculateViewVector(float f, float g);

    @Shadow
    public abstract Level level();

    @Shadow
    @Final
    private static Logger LOGGER;

    @Override
    public HitResult minecraft_fuzzy_aim_pick_tweak$pickWithOffsetYawPitch(double reach, float timeFactor, boolean checkFluid, float yawOffsetDegree, float pitchOffsetDegree) {
        LOGGER.debug("EntityMixin pick called with yawOffsetDegree: {}, pitchOffsetDegree: {}", yawOffsetDegree, pitchOffsetDegree);
        Vec3 eyePosition = this.getEyePosition(timeFactor);
        Vec3 rotatedViewVector = this.calculateViewVector(
                this.getViewXRot(timeFactor) + pitchOffsetDegree,
//                Math.clamp(this.getViewXRot(timeFactor) + pitchOffsetDegree, -90.0f, 90.0f),
                (this.getViewYRot(timeFactor) + yawOffsetDegree) % 360.0f
        );
        Vec3 vec33 = eyePosition.add(rotatedViewVector.x * reach, rotatedViewVector.y * reach, rotatedViewVector.z * reach);
        return this.level().clip(new ClipContext(eyePosition, vec33, ClipContext.Block.OUTLINE, checkFluid ? net.minecraft.world.level.ClipContext.Fluid.ANY : net.minecraft.world.level.ClipContext.Fluid.NONE, (Entity) (Object) this));
    }
}
