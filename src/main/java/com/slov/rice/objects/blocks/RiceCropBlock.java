package com.slov.rice.objects.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import com.slov.rice.init.ModBlocks;
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
import net.minecraft.state.IntegerProperty;
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

	public static final IntegerProperty AGE_REDUCTION = IntegerProperty.create("age_reduction", 0, 7);
	public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final VoxelShape[] SHAPE_BY_AGE_LOWER = new VoxelShape[] {
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D) };

	private static final VoxelShape[] SHAPE_BY_AGE_UPPER = new VoxelShape[] {
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D) };

	public RiceCropBlock() {
		super(Block.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F)
				.sound(SoundType.CROP));
		this.setDefaultState(this.stateContainer.getBaseState().with(this.getAgeProperty(), Integer.valueOf(0))
				.with(WATERLOGGED, Boolean.valueOf(false)).with(HALF, DoubleBlockHalf.LOWER).with(AGE_REDUCTION, 0));
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(AGE_REDUCTION);
		builder.add(HALF);
		builder.add(WATERLOGGED);
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
		return MathHelper.getCoordinateRandom(pos.getX(), pos.down(this.isLower(state) ? 0 : 1).getY(), pos.getZ());
	}

	@Override
	protected IItemProvider getSeedsItem() {
		return ModItems.RICE_SEEDS.get();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if (this.isLower(state)) {
			return SHAPE_BY_AGE_LOWER[state.get(this.getAgeProperty())];
		}
		return SHAPE_BY_AGE_UPPER[state.get(this.getAgeProperty())];
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		return this.getDefaultState().with(WATERLOGGED,
				Boolean.valueOf(ifluidstate.isTagged(FluidTags.WATER) && ifluidstate.getLevel() == 8));
	}

	@Override
	public void grow(World world, BlockPos pos, BlockState state) {
		int i = this.getAge(state) + this.getBonemealAgeIncrease(world);
		this.changeAge(world, pos, state, i);
	}

	/**
	 * Spawns the block's drops in the world. By the time this is called the Block
	 * has possibly been set to air via Block.removedByPlayer
	 */
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state,
			@Nullable TileEntity te, ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, Blocks.AIR.getDefaultState(), te, stack);
	}
	
	public void incrementAge(BlockState state, World world, BlockPos pos) {
		int newAge = this.getAge(state) + 1;
		this.changeAge(world, pos, state, newAge);
	}
	
	public void decrementAge(BlockState state, World world, BlockPos pos) {
		int newAge = this.getAge(state) - 1;
		this.changeAge(world, pos, state, newAge);
	}
	
	public int getReducedMaxAge(BlockState state) {
		return this.getMaxAge();// - state.get(AGE_REDUCTION);
	}
	
	public void changeAge(World world, BlockPos pos, BlockState state, int newAge) {
		// clamp
		if (newAge < 0) {
			newAge = 0;
		}
		if (newAge > this.getReducedMaxAge(state)) {
			newAge = this.getReducedMaxAge(state);
		}
		
		world.setBlockState(pos, this.withUpdatedAge(newAge, state), 2);

		// update other
		if (this.isUpper(state)) { // grow stems
			BlockState below = world.getBlockState(pos.down());
			world.setBlockState(pos.down(), this.withUpdatedAge(newAge, below), 2);
		}
		else if (this.isLower(state)) { // grow top
			BlockState above = world.getBlockState(pos.up());
			world.setBlockState(pos.up(), this.withUpdatedAge(newAge, above), 2);
		}
	}

	public boolean isLower(BlockState state) {
		return !this.isUpper(state);
	}

	public boolean isUpper(BlockState state) {
		return state.get(HALF) == DoubleBlockHalf.UPPER;
	}

	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		Block block = state.getBlock();
		if (!(block == Blocks.FARMLAND || block == Blocks.DIRT)) {
			return false;
		}

		BlockState above = worldIn.getBlockState(pos.up());
		int level = above.getFluidState().getLevel();
		if (level != 8) {
			return false;
		}

		BlockState twoAbove = worldIn.getBlockState(pos.up(2));
		int twoLevel = twoAbove.getFluidState().getLevel();
		Block twoBlock = twoAbove.getBlock();
		return (twoBlock == Blocks.AIR || twoBlock == ModBlocks.RICE_CROP.get()) && twoLevel == 0;
	}

	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		if (this.isLower(state)) {
			return super.isValidPosition(state, worldIn, pos);
		} else {
			BlockState blockstate = worldIn.getBlockState(pos.down());
			if (state.getBlock() != this) {
				return super.isValidPosition(state, worldIn, pos);
			}
			return blockstate.getBlock() == this && this.isLower(blockstate);
		}
	}

	public boolean isWaterlogged(BlockState state) {
		return state.get(WATERLOGGED);
	}

	/**
	 * Called before the Block is set to air in the world. Called regardless of if
	 * the player's tool can actually collect this block
	 */
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf doubleblockhalf = state.get(HALF);
		BlockPos blockpos = this.isLower(state) ? pos.up() : pos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		if (blockstate.getBlock() == this && blockstate.get(HALF) != doubleblockhalf) {
			BlockState replacement;
			if (this.isWaterlogged(blockstate)) {
				replacement = Blocks.WATER.getDefaultState();
			} else {
				replacement = Blocks.AIR.getDefaultState();
			}
			worldIn.setBlockState(blockpos, replacement, 35);
			worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
			if (!worldIn.isRemote && !player.isCreative()) {
				spawnDrops(state, worldIn, pos, (TileEntity) null, player, player.getHeldItemMainhand());
				spawnDrops(blockstate, worldIn, blockpos, (TileEntity) null, player, player.getHeldItemMainhand());
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place
	 * logic
	 */
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), 3);
	}

	public void placeAt(IWorld worldIn, BlockPos pos, int flags) {
		worldIn.setBlockState(pos, this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER), flags);
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), flags);
	}

	@Override
	public void tick(BlockState state, World world, BlockPos pos, Random random) {
		if (!world.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (this.isWaterlogged(state) ^ this.isLower(state)) { // XOR
			world.destroyBlock(pos, true);
		}
		if (this.isUpper(state)) {
			if (world.getLightSubtracted(pos, 0) >= 9) {
				int i = this.getAge(state);
				if (i < this.getReducedMaxAge(state)) {
					float f = getGrowthChance(this, world, pos);
					int newAge = i + 1;
					boolean grow = random.nextInt((int) (25.0F / f) + 1) == 0;
					
					// update block
					if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state, grow)) {
						world.setBlockState(pos, this.withUpdatedAge(newAge, state), 2);
						net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state);
					}

					// update stem
					if (this.isUpper(state)) {
						BlockState below = world.getBlockState(pos.down());
						if (below.getBlock() == ModBlocks.RICE_CROP.get() && net.minecraftforge.common.ForgeHooks
								.onCropsGrowPre(world, pos.down(), below, grow)) {
							world.setBlockState(pos.down(), this.withUpdatedAge(newAge, below), 2);
							net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos.down(), below);
						}
					}
				}
			}
		}
	}

	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) {
		if (this.isWaterlogged(stateIn)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	public BlockState withUpdatedAge(int age, BlockState state) {
		return super.withAge(age).with(WATERLOGGED, Boolean.valueOf(this.isWaterlogged(state))).with(HALF,
				state.get(HALF)).with(AGE_REDUCTION, state.get(AGE_REDUCTION));
	}
}
