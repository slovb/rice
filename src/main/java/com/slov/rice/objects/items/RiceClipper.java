package com.slov.rice.objects.items;

import com.slov.rice.event.entity.player.UseRiceClipperEvent;
import com.slov.rice.objects.blocks.RiceCropBlock;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;

public class RiceClipper extends Item {

	public RiceClipper() {
		super((new Item.Properties()).maxDamage(238).group(ItemGroup.TOOLS));
	}
	
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos blockPos = context.getPos();
		int hook = onClipperUse(context);
		if (hook != 0) {
			return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
		}
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.getBlock() instanceof RiceCropBlock) {
			RiceCropBlock block = (RiceCropBlock) blockState.getBlock();
			PlayerEntity playerEntity = context.getPlayer();
			world.playSound(playerEntity, blockPos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 0.5F, 1.0F);
			if (!world.isRemote) {
				block.decrementAge(blockState, world, blockPos);
			}
			return ActionResultType.SUCCESS;
		}
		
		return ActionResultType.PASS;
	}

	public static int onClipperUse(ItemUseContext context) {
		UseRiceClipperEvent event = new UseRiceClipperEvent(context);
		if (MinecraftForge.EVENT_BUS.post(event)) {
			return -1;
		}
		if (event.getResult() == Result.ALLOW) {
			context.getItem().damageItem(1, context.getPlayer(), player -> player.sendBreakAnimation(context.getHand()));	
		}
		return 0;
	}
}
