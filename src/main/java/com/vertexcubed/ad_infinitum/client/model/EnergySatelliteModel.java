package com.vertexcubed.ad_infinitum.client.model;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vertexcubed.ad_infinitum.AdInfinitum;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;


import static com.vertexcubed.ad_infinitum.AdInfinitum.modLoc;

public class EnergySatelliteModel extends Model {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(modLoc("energy_satellite"), "main");
	private final ModelPart root;
//	private final ModelPart solar_panels;
//	private final ModelPart panel_right;
//	private final ModelPart panel_left;
//	private final ModelPart beam;
//	private final ModelPart bb_main;

	public EnergySatelliteModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);
		this.root = root;
//		this.solar_panels = root.getChild("solar_panels");
//		this.panel_right = root.getChild("panel_right");
//		this.panel_left = root.getChild("panel_left");
//		this.beam = root.getChild("beam");
//		this.bb_main = root.getChild("bb_main");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition solar_panels = partdefinition.addOrReplaceChild("solar_panels", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition panel_right = solar_panels.addOrReplaceChild("panel_right", CubeListBuilder.create().texOffs(34, 24).addBox(-4.0F, 0.0F, -1.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(32, 0).addBox(-4.0F, -1.0F, -5.0F, 12.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(-12.0F, 0.0F, 0.0F));

		PartDefinition panel_left = solar_panels.addOrReplaceChild("panel_left", CubeListBuilder.create().texOffs(32, 11).addBox(-4.0F, 0.0F, -1.0F, 12.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 24).addBox(-4.0F, -1.0F, -5.0F, 12.0F, 1.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, 0.0F, 0.0F));

		PartDefinition beam = partdefinition.addOrReplaceChild("beam", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -0.5F, -15.0F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		PartDefinition support_r1 = beam.addOrReplaceChild("support_r1", CubeListBuilder.create().texOffs(0, 35).addBox(3.6441F, -1.0F, -7.7595F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -5.5211F, 0.0F, 0.3927F, 1.5708F));

		PartDefinition support_r2 = beam.addOrReplaceChild("support_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-4.6441F, -1.0F, -7.7595F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -5.5211F, 0.0F, -0.3927F, 1.5708F));

		PartDefinition support_r3 = beam.addOrReplaceChild("support_r3", CubeListBuilder.create().texOffs(16, 35).addBox(3.6441F, -1.0F, -7.7595F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -5.5211F, 0.0F, 0.3927F, 0.0F));

		PartDefinition support_r4 = beam.addOrReplaceChild("support_r4", CubeListBuilder.create().texOffs(32, 35).addBox(-4.6441F, -1.0F, -7.7595F, 1.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -5.5211F, 0.0F, -0.3927F, 0.0F));

		PartDefinition bb_main = partdefinition.addOrReplaceChild("bb_main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition base_r1 = bb_main.addOrReplaceChild("base_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -8.0F, 0.0F, 0.3927F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//		AdInfinitum.LOGGER.info("Rendering model, stack: " + poseStack.last().pose());
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}