package com.slov.rice.objects.items;

import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RiceSeedsItem extends BlockNamedItem {

	public RiceSeedsItem(Block blockIn, Item.Properties properties) {
		super(blockIn, properties);
	}
	
	/*
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
	    BlockPos blockpos1 = blockpos.offset(context.getFace());
	    
		if (applyRiceSeeds(context.getItem(), world, blockpos, context.getPlayer())) {
			if (!world.isRemote) {
				//world.playEvent()
			}
			return ActionResultType.PASS;
		}
		return ActionResultType.SUCCESS;
	}
	*/
	
	/*
	public ActionResultType onItemUse(ItemUseContext context) {
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
	    BlockPos blockpos1 = blockpos.offset(context.getFace());
	    if (applyBonemeal(context.getItem(), world, blockpos, context.getPlayer())) {
	         if (!world.isRemote) {
	            world.playEvent(2005, blockpos, 0);
	         }

	         return ActionResultType.SUCCESS;
	      } else {
	         BlockState blockstate = world.getBlockState(blockpos);
	         boolean flag = blockstate.func_224755_d(world, blockpos, context.getFace());
	         if (flag && growSeagrass(context.getItem(), world, blockpos1, context.getFace())) {
	            if (!world.isRemote) {
	               world.playEvent(2005, blockpos1, 0);
	            }

	            return ActionResultType.SUCCESS;
	         } else {
	            return ActionResultType.PASS;
	         }
		return ActionResultType.SUCCESS;
	}
	
	private boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos blockpos, PlayerEntity player) {
	      BlockState blockstate = worldIn.getBlockState(pos);
	      int hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, worldIn, pos, blockstate, stack);
	      if (hook != 0) return hook > 0;
	      if (blockstate.getBlock() instanceof IGrowable) {
	         IGrowable igrowable = (IGrowable)blockstate.getBlock();
	         if (igrowable.canGrow(worldIn, pos, blockstate, worldIn.isRemote)) {
	            if (!worldIn.isRemote) {
	               if (igrowable.canUseBonemeal(worldIn, worldIn.rand, pos, blockstate)) {
	                  igrowable.grow(worldIn, worldIn.rand, pos, blockstate);
	               }

	               stack.shrink(1);
	            }

	            return true;
	         }
	      }

	      return false;
	}
	*/
}
