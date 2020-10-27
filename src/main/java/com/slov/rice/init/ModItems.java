package com.slov.rice.init;

import com.slov.rice.Rice;
import com.slov.rice.objects.items.RiceSeedsItem;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Rice.MODID);

	public static final RegistryObject<Item> RICE_SEEDS = ITEMS.register("rice_seeds", RiceSeedsItem::new);
}
