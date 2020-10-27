package com.slov.rice.objects.items;

import com.slov.rice.init.ModBlocks;

import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class RiceSeedsItem extends BlockNamedItem {

	public RiceSeedsItem() {
		super(ModBlocks.RICE_CROP.get(), new Item.Properties().group(ItemGroup.MATERIALS));
	}
}
