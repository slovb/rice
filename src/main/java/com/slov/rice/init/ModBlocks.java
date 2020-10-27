package com.slov.rice.init;

import com.slov.rice.Rice;
import com.slov.rice.objects.blocks.RiceCropBlock;

import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Rice.MODID);

	public static final RegistryObject<Block> RICE_CROP = BLOCKS.register("rice_crop", RiceCropBlock::new);
}
