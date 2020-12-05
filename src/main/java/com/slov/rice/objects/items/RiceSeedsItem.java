package com.slov.rice.objects.items;

import com.slov.rice.init.ModBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class RiceSeedsItem extends BlockNamedItem {

	public RiceSeedsItem() {
		super(ModBlocks.RICE_CROP.get(), new Item.Properties().group(ItemGroup.MATERIALS));
	}

	protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
		context.getWorld().setBlockState(context.getPos().up(), Blocks.AIR.getDefaultState(), 27);
		return super.placeBlock(context, state);
	}
}
