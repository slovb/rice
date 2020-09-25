package com.slov.rice;

import com.slov.rice.init.ModBlocks;
import com.slov.rice.objects.blocks.RiceBlock;
import com.slov.rice.objects.items.RiceSeedsItem;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.IForgeRegistryEntry;

@EventBusSubscriber(modid = Rice.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventSubscriber {

	@SubscribeEvent
	public static void onRegisterItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().registerAll(
			setup(new RiceSeedsItem(ModBlocks.RICE, new Item.Properties().group(ItemGroup.MATERIALS)), "rice_seeds"),
			setup(new BlockItem(ModBlocks.ASDF, new Item.Properties().group(ItemGroup.MATERIALS)), "asdf")
		);
		
	}
	
	@SubscribeEvent
	public static void onRegisterBlocks(RegistryEvent.Register<Block> event) {
		event.getRegistry().registerAll(
			setup(new Block(Block.Properties.create(Material.PLANTS).hardnessAndResistance(0.0F).sound(SoundType.CROP)), "asdf"),
			setup(new RiceBlock(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.CROP)), "rice")
		);
	}
	
	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final String name) {
		return setup(entry, new ResourceLocation(Rice.MODID, name));
	}

	public static <T extends IForgeRegistryEntry<T>> T setup(final T entry, final ResourceLocation registryName) {
		entry.setRegistryName(registryName);
		return entry;
	}
}
