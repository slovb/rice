package com.slov.rice.objects.blocks;

import com.slov.rice.init.ModItems;

import net.minecraft.block.CropsBlock;
import net.minecraft.util.IItemProvider;

public class RiceBlock extends CropsBlock {

	public RiceBlock(Properties builder) {
		super(builder);
	}
	
	@Override
	protected IItemProvider getSeedsItem() {
		return ModItems.RICE_SEEDS;
	}
}
