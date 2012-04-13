/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src;

import net.minecraft.src.buildcraft.core.CoreProxy;
import net.minecraft.src.buildcraft.core.DefaultProps;
import net.minecraft.src.buildcraft.core.EntityRobot;
import net.minecraft.src.buildcraft.core.network.EntityIds;
import net.minecraft.src.buildcraft.factory.BlockAutoWorkbench;
import net.minecraft.src.buildcraft.factory.BlockFrame;
import net.minecraft.src.buildcraft.factory.BlockMiningWell;
import net.minecraft.src.buildcraft.factory.BlockPlainPipe;
import net.minecraft.src.buildcraft.factory.BlockPump;
import net.minecraft.src.buildcraft.factory.BlockQuarry;
import net.minecraft.src.buildcraft.factory.BlockRefinery;
import net.minecraft.src.buildcraft.factory.BlockTank;
import net.minecraft.src.buildcraft.factory.BptBlockAutoWorkbench;
import net.minecraft.src.buildcraft.factory.BptBlockFrame;
import net.minecraft.src.buildcraft.factory.BptBlockRefinery;
import net.minecraft.src.buildcraft.factory.BptBlockTank;
import net.minecraft.src.buildcraft.factory.EntityMechanicalArm;
import net.minecraft.src.buildcraft.factory.GuiHandler;
import net.minecraft.src.buildcraft.factory.TankBucketHandler;
import net.minecraft.src.buildcraft.factory.TileAssemblyTable;
import net.minecraft.src.buildcraft.factory.TileAutoWorkbench;
import net.minecraft.src.buildcraft.factory.TileMiningWell;
import net.minecraft.src.buildcraft.factory.TilePump;
import net.minecraft.src.buildcraft.factory.TileQuarry;
import net.minecraft.src.buildcraft.factory.TileRefinery;
import net.minecraft.src.buildcraft.factory.TileTank;
import net.minecraft.src.buildcraft.silicon.TileLaser;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.Property;

public class BuildCraftFactory {
	public static BlockQuarry quarryBlock;
	public static BlockMiningWell miningWellBlock;
	public static BlockAutoWorkbench autoWorkbenchBlock;
	public static BlockFrame frameBlock;
	public static BlockPlainPipe plainPipeBlock;
	public static BlockPump pumpBlock;
	public static BlockTank tankBlock;
	public static BlockRefinery refineryBlock;

	public static int drillTexture;

	private static boolean initialized = false;

	public static boolean allowMining = true;

	public static void load() {
		// Register gui handler
		MinecraftForge.setGuiHandler(mod_BuildCraftFactory.instance, new GuiHandler());
		
		//MinecraftForge.registerEntity(EntityMechanicalArm.class, mod_BuildCraftFactory.instance, EntityIds.MECHANICAL_ARM, 50, 10, true);
	}

	public static void initialize () {
		if (initialized)
			return;
		else
			initialized = true;

		mod_BuildCraftCore.initialize();
		BuildCraftCore.initializeGears();

		allowMining = Boolean
				.parseBoolean(BuildCraftCore.mainConfiguration
						.getOrCreateBooleanProperty("mining.enabled",
								Configuration.CATEGORY_GENERAL, true).value);

		Property minigWellId = BuildCraftCore.mainConfiguration
				.getOrCreateBlockIdProperty("miningWell.id",
						DefaultProps.MINING_WELL_ID);
		Property plainPipeId = BuildCraftCore.mainConfiguration
				.getOrCreateBlockIdProperty("drill.id", DefaultProps.DRILL_ID);
		Property autoWorkbenchId = BuildCraftCore.mainConfiguration
				.getOrCreateBlockIdProperty("autoWorkbench.id",
						DefaultProps.AUTO_WORKBENCH_ID);
		Property frameId = BuildCraftCore.mainConfiguration
				.getOrCreateBlockIdProperty("frame.id", DefaultProps.FRAME_ID);
		Property quarryId = BuildCraftCore.mainConfiguration
				.getOrCreateBlockIdProperty("quarry.id", DefaultProps.QUARRY_ID);
		Property pumpId = BuildCraftCore.mainConfiguration
		   .getOrCreateBlockIdProperty("pump.id", DefaultProps.PUMP_ID);
		Property tankId = BuildCraftCore.mainConfiguration
		   .getOrCreateBlockIdProperty("tank.id", DefaultProps.TANK_ID);
		Property refineryId = BuildCraftCore.mainConfiguration
		.getOrCreateBlockIdProperty("refinery.id", DefaultProps.REFINERY_ID);

		BuildCraftCore.mainConfiguration.save();

		MinecraftForge.registerCustomBucketHandler(new TankBucketHandler());

		miningWellBlock = new BlockMiningWell(Integer.parseInt(minigWellId.value));
		CoreProxy.registerBlock(miningWellBlock);
		CoreProxy.addName(miningWellBlock.setBlockName("miningWellBlock"), "Mining Well");

		plainPipeBlock = new BlockPlainPipe(Integer.parseInt(plainPipeId.value));
		CoreProxy.registerBlock(plainPipeBlock);
		CoreProxy.addName(plainPipeBlock.setBlockName("plainPipeBlock"), "Mining Pipe");

		autoWorkbenchBlock = new BlockAutoWorkbench(
				Integer.parseInt(autoWorkbenchId.value));
		CoreProxy.registerBlock(autoWorkbenchBlock);
		CoreProxy.addName(autoWorkbenchBlock.setBlockName("autoWorkbenchBlock"),
				"Automatic Crafting Table");

		frameBlock = new BlockFrame(Integer.parseInt(frameId.value));
		CoreProxy.registerBlock(frameBlock);
		CoreProxy.addName(frameBlock.setBlockName("frameBlock"), "Frame");

		quarryBlock = new BlockQuarry(Integer.parseInt(quarryId.value));
		CoreProxy.registerBlock(quarryBlock);

		CoreProxy.addName(quarryBlock.setBlockName("machineBlock"),
		"Quarry");

		tankBlock = new BlockTank(Integer.parseInt(tankId.value));
		CoreProxy.addName(tankBlock.setBlockName("tankBlock"),
		"Tank");
		CoreProxy.registerBlock(tankBlock);

		pumpBlock = new BlockPump(Integer.parseInt(pumpId.value));
		CoreProxy.addName(pumpBlock.setBlockName("pumpBlock"),
		"Pump");
		CoreProxy.registerBlock(pumpBlock);

		refineryBlock = new BlockRefinery(Integer.parseInt(refineryId.value));
		CoreProxy.addName(refineryBlock.setBlockName("refineryBlock"),
		"Refinery");
		CoreProxy.registerBlock(refineryBlock);

		CoreProxy.registerTileEntity(TileQuarry.class, "Machine");
		CoreProxy.registerTileEntity(TileMiningWell.class, "MiningWell");
		CoreProxy.registerTileEntity(TileAutoWorkbench.class, "AutoWorkbench");
		CoreProxy.registerTileEntity(TilePump.class,
				"net.minecraft.src.buildcraft.factory.TilePump");
		CoreProxy.registerTileEntity(TileTank.class,
		"net.minecraft.src.buildcraft.factory.TileTank");
		CoreProxy.registerTileEntity(TileRefinery.class,
				"net.minecraft.src.buildcraft.factory.Refinery");
		CoreProxy.registerTileEntity(TileLaser.class, "net.minecraft.src.buildcraft.factory.TileLaser");
		CoreProxy.registerTileEntity(TileAssemblyTable.class, "net.minecraft.src.buildcraft.factory.TileAssemblyTable");

		drillTexture = 2 * 16 + 1;

		BuildCraftCore.mainConfiguration.save();

		new BptBlockAutoWorkbench(autoWorkbenchBlock.blockID);
		new BptBlockFrame(frameBlock.blockID);
		new BptBlockRefinery(refineryBlock.blockID);
		new BptBlockTank(tankBlock.blockID);

		if (BuildCraftCore.loadDefaultRecipes)
			loadRecipes();
	}

	public static void loadRecipes () {
		CraftingManager craftingmanager = CraftingManager.getInstance();

		if (allowMining) {
			craftingmanager.addRecipe(new ItemStack(miningWellBlock, 1), new Object[] {
				"ipi", "igi", "iPi", Character.valueOf('p'), Item.redstone,
				Character.valueOf('i'), Item.ingotIron, Character.valueOf('g'),
				BuildCraftCore.ironGearItem, Character.valueOf('P'),
				Item.pickaxeSteel });

			craftingmanager.addRecipe(
					new ItemStack(quarryBlock),
					new Object[] { "ipi", "gig", "dDd",
						Character.valueOf('i'), BuildCraftCore.ironGearItem,
						Character.valueOf('p'), Item.redstone,
						Character.valueOf('g'),	BuildCraftCore.goldGearItem,
						Character.valueOf('d'),	BuildCraftCore.diamondGearItem,
						Character.valueOf('D'),	Item.pickaxeDiamond,
					});
		}

		craftingmanager.addRecipe(
				new ItemStack(autoWorkbenchBlock),
				new Object[] { " g ", "gwg", " g ", Character.valueOf('w'),
						Block.workbench, Character.valueOf('g'),
						BuildCraftCore.woodenGearItem });

		craftingmanager.addRecipe(
				new ItemStack(pumpBlock),
				new Object[] { "T ", "W ",
					Character.valueOf('T'), tankBlock,
					Character.valueOf('W'), miningWellBlock,
				});

		craftingmanager.addRecipe(
				new ItemStack(tankBlock),
				new Object[] { "ggg", "g g", "ggg",
					Character.valueOf('g'), Block.glass,
				});

		craftingmanager.addRecipe(
				new ItemStack(refineryBlock),
				new Object[] { "   ", "RTR", "TGT",
					Character.valueOf('T'), tankBlock,
					Character.valueOf('G'), BuildCraftCore.diamondGearItem,
					Character.valueOf('R'), Block.torchRedstoneActive,
				});
	}
}
