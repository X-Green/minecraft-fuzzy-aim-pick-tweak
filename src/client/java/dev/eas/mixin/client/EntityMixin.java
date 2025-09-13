package dev.eas.mixin.client;

import dev.eas.mixin_interface.IEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntity {

    @Shadow
    public abstract Vec3 getEyePosition(float f);

    @Shadow
    public abstract Vec3 getViewVector(float f);

    @Shadow
    public abstract Level level();

    @Override
    public HitResult pick(double reach, float timeFactor, boolean checkFluid, float yawOffset, float pitchOffset) {
        System.out.println("EntityMixin pick called with yawOffset: " + yawOffset + ", pitchOffset: " + pitchOffset);
        Vec3 vec3 = this.getEyePosition(timeFactor);
        Vec3 vec32 = this.getViewVector(timeFactor);
        Vec3 vec33 = vec3.add(vec32.x * reach, vec32.y * reach, vec32.z * reach);
        return this.level()
                .clip(
                        new ClipContext(
                                vec3, vec33, ClipContext.Block.OUTLINE,
                                checkFluid ?
                                        net.minecraft.world.level.ClipContext.Fluid.ANY :
                                        net.minecraft.world.level.ClipContext.Fluid.NONE, (Entity) (Object) this
                        )
                );
    }
}
