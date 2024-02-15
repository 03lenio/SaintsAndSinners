package de.nulldrei.saintsandsinners.armor.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.item.ArmorItem;

public class ReclaimedMaskModel extends SASArmorModel {

    public ReclaimedMaskModel(ModelPart part, ArmorItem.Type type) {
        super(part, type);
    }

    public static LayerDefinition createBodyLayer() {

        MeshDefinition meshDefinition = HumanoidModel.createMesh(new CubeDeformation(0), 0);
        PartDefinition partdefinition = createHumanoidAlias(meshDefinition);

        PartDefinition head = partdefinition.getChild("Head");

        PartDefinition mask = head.addOrReplaceChild("mask", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition leftHorn = mask.addOrReplaceChild("leftHorn", CubeListBuilder.create(), PartPose.offset(-6.9F, 0.0F, 0.0F));

        PartDefinition cube_r1 = leftHorn.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(161, 108).addBox(1.2F, -12.5F, -8.7F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3942F, 0.0772F, 0.1582F));

        PartDefinition cube_r2 = leftHorn.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(160, 107).addBox(1.3F, -11.5F, -8.4F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3043F, 0.0262F, 0.0832F));

        PartDefinition cube_r3 = leftHorn.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(162, 105).addBox(3.6F, -9.2F, -5.8F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        PartDefinition rightHorn = mask.addOrReplaceChild("rightHorn", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r4 = rightHorn.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(158, 109).addBox(4.7F, -11.2F, -8.7F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3968F, -0.0619F, -0.1717F));

        PartDefinition cube_r5 = rightHorn.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(157, 108).addBox(4.0F, -10.4F, -8.4F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.3019F, -0.0471F, -0.1499F));

        PartDefinition cube_r6 = rightHorn.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(159, 106).addBox(1.6F, -10.0F, -5.8F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1309F));

        PartDefinition baseMask = mask.addOrReplaceChild("baseMask", CubeListBuilder.create().texOffs(158, 106).addBox(-5.0F, -9.0F, -5.0F, 10.0F, 9.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(156, 105).addBox(-3.0F, -6.7F, -6.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(156, 105).addBox(-3.0F, -6.7F, -6.0F, 6.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(157, 107).addBox(-1.0F, -2.7F, -8.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(157, 107).addBox(-1.0F, -4.7F, -7.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition eyeRight_r1 = baseMask.addOrReplaceChild("eyeRight_r1", CubeListBuilder.create().texOffs(174, 105).addBox(3.9F, -1.1F, -6.4F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition eyeRight_r2 = baseMask.addOrReplaceChild("eyeRight_r2", CubeListBuilder.create().texOffs(174, 105).addBox(-5.9F, -1.1F, -6.4F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0472F));

        PartDefinition hood = mask.addOrReplaceChild("hood", CubeListBuilder.create().texOffs(155, 82).addBox(-5.0F, -9.0F, -4.0F, 10.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(159, 87).addBox(-5.0F, -9.0F, 1.0F, 10.0F, 7.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(161, 86).addBox(-5.0F, -9.0F, 3.0F, 10.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(162, 90).addBox(-5.0F, -4.0F, -4.0F, 10.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(161, 89).addBox(-5.0F, -4.0F, -3.0F, 10.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(162, 90).addBox(-5.0F, -4.0F, -1.0F, 10.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(208, 57).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(213, 88).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition RightArm = partdefinition.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(40, 208).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(40, 224).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

        PartDefinition LeftArm = partdefinition.addOrReplaceChild("LeftArm", CubeListBuilder.create().texOffs(32, 240).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(48, 240).addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(5.0F, 2.0F, 0.0F));

        PartDefinition RightLeg = partdefinition.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(163, 224).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(163, 240).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

        PartDefinition LeftLeg = partdefinition.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(16, 240).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 240).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offset(1.9F, 12.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 256, 256);
    }


    public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}