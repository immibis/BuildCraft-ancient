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
import net.minecraft.src.forge.IIDCallback;
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

		allowMining = Boolean
				.parseBoolean(BuildCraftCore.mainConfiguration
						.getOrCreateBooleanProperty("mining.enabled",
								Configuration.CATEGORY_GENERAL, true).value);
		
		BuildCraftCore.mainConfiguration.save();

		IIDCallback idc = new IIDCallback() {
            
            @Override
            public void unregister(String name, int id) {
                Item.itemsList[id] = null;
                Block.blocksList[id] = null;
            }
            
            @Override
            public void register(String name, int id) {
                
                if(name.equals("miningWell")) {
                    miningWellBlock = new BlockMiningWell(id);
                    CoreProxy.registerBlock(miningWellBlock);
                    CoreProxy.addName(miningWellBlock.setBlockName("miningWellBlock"), "Mining Well");
                
                } else if(name.equals("drill")) {
                    plainPipeBlock = new BlockPlainPipe(id);
                    CoreProxy.registerBlock(plainPipeBlock);
                    CoreProxy.addName(plainPipeBlock.setBlockName("plainPipeBlock"), "Mining Pipe");
                
                } else if(name.equals("autoWorkbench")) {
                    autoWorkbenchBlock = new BlockAutoWorkbench(id);
                    CoreProxy.registerBlock(autoWorkbenchBlock);
                    CoreProxy.addName(autoWorkbenchBlock.setBlockName("autoWorkbenchBlock"),
                            "Automatic Crafting Table");
                    
                } else if(name.equals("frame")) {
                    frameBlock = new BlockFrame(id);
                    CoreProxy.registerBlock(frameBlock);
                    CoreProxy.addName(frameBlock.setBlockName("frameBlock"), "Frame");
                    
                } else if(name.equals("quarry")) {
                    quarryBlock = new BlockQuarry(id);
                    CoreProxy.registerBlock(quarryBlock);

                    CoreProxy.addName(quarryBlock.setBlockName("machineBlock"),
                    "Quarry");
                
                } else if(name.equals("pump")) {
                    pumpBlock = new BlockPump(id);
                    CoreProxy.addName(pumpBlock.setBlockName("pumpBlock"),
                    "Pump");
                    CoreProxy.registerBlock(pumpBlock);
                
                } else if(name.equals("tank")) {
                    tankBlock = new BlockTank(id);
                    CoreProxy.addName(tankBlock.setBlockName("tankBlock"),
                    "Tank");
                    CoreProxy.registerBlock(tankBlock);
                
                } else if(name.equals("refinery")) {
                    refineryBlock = new BlockRefinery(id);
                    CoreProxy.addName(refineryBlock.setBlockName("refineryBlock"),
                    "Refinery");
                    CoreProxy.registerBlock(refineryBlock);
                    
                }
            }
        };
        
        MinecraftForge.registerBlockID(mod_BuildCraftFactory.instance, "miningWell", idc);
        MinecraftForge.registerBlockID(mod_BuildCraftFactory.instance, "drill", idc);
        MinecraftForge.registerBlockID(mod_BuildCraftFactory.instance, "autoWorkbench", idc);
        MinecraftForge.registerBlockID(mod_BuildCraftFactory.instance, "frame", idc);
        MinecraftForge.registerBlockID(mod_BuildCraftFactory.instance, "quarry", idc);
        MinecraftForge.registerBlockID(mod_BuildCraftFactory.instance, "pump", idc);
        MinecraftForge.registerBlockID(mod_BuildCraftFactory.instance, "tank", idc);
        MinecraftForge.registerBlockID(mod_BuildCraftFactory.instance, "refinery", idc);

		MinecraftForge.registerCustomBucketHandler(new TankBucketHandler());

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
		
		MinecraftForge.addRecipeCallback(new Runnable() {
		    public void run() {
                new BptBlockAutoWorkbench(autoWorkbenchBlock.blockID);
                new BptBlockFrame(frameBlock.blockID);
                new BptBlockTank(tankBlock.blockID);
                new BptBlockRefinery(refineryBlock.blockID);

                if(BuildCraftCore.loadDefaultRecipes)
		            loadRecipes();
		    }
		});
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
