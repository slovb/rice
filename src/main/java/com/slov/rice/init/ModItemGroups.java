package com.slov.rice.init;

import java.util.function.Supplier;

import com.slov.rice.Rice;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroups {
	
	public static final ItemGroup MOD_ITEM_GROUP = new ModItemGroup(Rice.MODID, () -> new ItemStack(ModItems.RICE_SEEDS));

	public static class ModItemGroup extends ItemGroup {
		
		private final Supplier<ItemStack> iconSupplier;
		
		public ModItemGroup(final String name, final Supplier<ItemStack> iconSupplier) {
			super(name);
			this.iconSupplier = iconSupplier;
		}

		@Override
		public ItemStack createIcon() {
			return iconSupplier.get();
		}
	}
}
