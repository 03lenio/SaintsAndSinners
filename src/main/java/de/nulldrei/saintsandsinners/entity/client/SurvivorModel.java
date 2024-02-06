package de.nulldrei.saintsandsinners.entity.client;// Made with Blockbench 4.9.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import de.nulldrei.saintsandsinners.entity.animations.SASAnimationUtils;
import de.nulldrei.saintsandsinners.entity.animations.pose.SurvivorArmPose;
import de.nulldrei.saintsandsinners.entity.neutral.AbstractSurvivor;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Mob;

public class SurvivorModel<T extends Mob> extends PlayerModel<T> {
	private final PartPose bodyDefault = this.body.storePose();
	private final PartPose headDefault = this.head.storePose();
	private final PartPose leftArmDefault = this.leftArm.storePose();
	private final PartPose rightArmDefault = this.rightArm.storePose();

	public SurvivorModel(ModelPart p_170810_) {
		super(p_170810_, false);
	}

	public static MeshDefinition createMesh(CubeDeformation p_170812_) {
		MeshDefinition meshdefinition = PlayerModel.createMesh(p_170812_, false);
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, p_170812_), PartPose.ZERO);
		addHead(p_170812_, meshdefinition);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		return meshdefinition;
	}

	public static void addHead(CubeDeformation p_262174_, MeshDefinition p_262011_) {
		PartDefinition partdefinition = p_262011_.getRoot();
	}

	public void setupAnim(T p_103366_, float p_103367_, float p_103368_, float p_103369_, float p_103370_, float p_103371_) {
		this.body.loadPose(this.bodyDefault);
		this.head.loadPose(this.headDefault);
		this.leftArm.loadPose(this.leftArmDefault);
		this.rightArm.loadPose(this.rightArmDefault);
		super.setupAnim(p_103366_, p_103367_, p_103368_, p_103369_, p_103370_, p_103371_);
		float f = ((float)Math.PI / 6F);
		float f1 = p_103369_ * 0.1F + p_103367_ * 0.5F;
		float f2 = 0.08F + p_103368_ * 0.4F;
		if (p_103366_ instanceof AbstractSurvivor abstractSurvivor) {
			SurvivorArmPose Survivorarmpose = abstractSurvivor.getArmPose();
			if (Survivorarmpose == SurvivorArmPose.ATTACKING_WITH_MELEE_WEAPON && this.attackTime == 0.0F) {
				this.holdWeaponHigh(p_103366_);
			} else if (Survivorarmpose == SurvivorArmPose.CROSSBOW_HOLD) {
				AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, !p_103366_.isLeftHanded());
			} else if (Survivorarmpose == SurvivorArmPose.CROSSBOW_CHARGE) {
				AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, p_103366_, !p_103366_.isLeftHanded());
			} else if (Survivorarmpose == SurvivorArmPose.BEGGING_FOR_ITEM) {
				SASAnimationUtils.animateBegging(this.rightArm, this.leftArm, p_103366_, !p_103366_.isLeftHanded());
			} else if (Survivorarmpose == SurvivorArmPose.ADMIRING_ITEM) {
				this.head.xRot = 0.5F;
				this.head.yRot = 0.0F;
				if (p_103366_.isLeftHanded()) {
					this.rightArm.yRot = -0.5F;
					this.rightArm.xRot = -0.9F;
				} else {
					this.leftArm.yRot = 0.5F;
					this.leftArm.xRot = -0.9F;
				}
			}
		}

		this.leftPants.copyFrom(this.leftLeg);
		this.rightPants.copyFrom(this.rightLeg);
		this.leftSleeve.copyFrom(this.leftArm);
		this.rightSleeve.copyFrom(this.rightArm);
		this.jacket.copyFrom(this.body);
		this.hat.copyFrom(this.head);
	}

	private void holdWeaponHigh(T p_103361_) {
		if (p_103361_.isLeftHanded()) {
			this.leftArm.xRot = -1.8F;
		} else {
			this.rightArm.xRot = -1.8F;
		}

	}
}