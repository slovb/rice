package com.slov.rice.objects.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.slov.rice.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RiceCropBlock extends CropsBlock implements IWaterLoggable {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D) };

	public RiceCropBlock() {
		super(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F)
				.sound(SoundType.CROP));
		this.setDefaultState(this.stateContainer.getBaseState().with(this.getAgeProperty(), Integer.valueOf(0))
				.with(WATERLOGGED, Boolean.valueOf(false)).with(HALF, DoubleBlockHalf.LOWER));
	}

	public void placeAt(IWorld worldIn, BlockPos pos, int flags) {
		worldIn.setBlockState(pos, this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER), flags);
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), flags);
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), 3);
	}
	
	public BlockState withUpdatedAge(int age, BlockState state) {
		return super.withAge(age)
				.with(WATERLOGGED, Boolean.valueOf(this.isWaterlogged(state)))
				.with(HALF, state.get(HALF));
	}

	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		Block block = state.getBlock();
		BlockState above = worldIn.getBlockState(pos.up());
		int level = above.getFluidState().getLevel();
		return (block == Blocks.FARMLAND || block == Blocks.DIRT) && level == 8;
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		if (state.get(HALF) != DoubleBlockHalf.UPPER) {
			return super.isValidPosition(state, worldIn, pos);
		} else {
			BlockState blockstate = worldIn.getBlockState(pos.down());
			if (state.getBlock() != this)
				return super.isValidPosition(state, worldIn, pos);
			return blockstate.getBlock() == this && blockstate.get(HALF) == DoubleBlockHalf.LOWER;
		}
	}

	/**
	 * Spawns the block's drops in the world. By the time this is called the Block
	 * has possibly been set to air via Block.removedByPlayer
	 */
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state,
			@Nullable TileEntity te, ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, Blocks.AIR.getDefaultState(), te, stack);
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf doubleblockhalf = state.get(HALF);
		BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		if (blockstate.getBlock() == this && blockstate.get(HALF) != doubleblockhalf) {
			worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
			worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
			if (!worldIn.isRemote && !player.isCreative()) {
				spawnDrops(state, worldIn, pos, (TileEntity) null, player, player.getHeldItemMainhand());
				spawnDrops(blockstate, worldIn, blockpos, (TileEntity) null, player, player.getHeldItemMainhand());
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	public boolean isWaterlogged(BlockState state) {
		return state.get(WATERLOGGED);
	}

	@Override
	public void grow(World worldIn, BlockPos pos, BlockState state) {
		int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
		int j = this.getMaxAge();
		if (i > j) {
			i = j;
		}

		worldIn.setBlockState(pos, this.withUpdatedAge(i, state), 2);
	}

	@Override
	public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
		super.tick(state, worldIn, pos, random);
		if (!worldIn.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (!this.isWaterlogged(state) ^ state.get(HALF) == DoubleBlockHalf.UPPER) { // XOR
			worldIn.destroyBlock(pos, true);
		}
		if (worldIn.getLightSubtracted(pos, 0) >= 9) {
			int i = this.getAge(state);
			if (i < this.getMaxAge()) {
				float f = getGrowthChance(this, worldIn, pos);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state,
						random.nextInt((int) (25.0F / f) + 1) == 0)) {
					worldIn.setBlockState(pos, this.withUpdatedAge(i + 1, state), 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
				}
			}
		}
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(WATERLOGGED,
				Boolean.valueOf(ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8));
	}

	@Override
	protected IItemProvider getSeedsItem() {
		return ModItems.RICE_SEEDS.get();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE_BY_AGE[state.get(this.getAgeProperty())];
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if (this.isWaterlogged(stateIn)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(WATERLOGGED);
		builder.add(HALF);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IFluidState getFluidState(BlockState state) {
		return this.isWaterlogged(state) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
	}

	/**
	 * Return a random long to be passed to {@link IBakedModel#getQuads}, used for
	 * random model rotations
	 */
	@OnlyIn(Dist.CLIENT)
	public long getPositionRandom(BlockState state, BlockPos pos) {
		return MathHelper.getCoordinateRandom(pos.getX(),
				pos.down(state.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
	}
}
