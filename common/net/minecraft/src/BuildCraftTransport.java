/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src;

import java.util.LinkedList;

import net.minecraft.src.buildcraft.api.Action;
import net.minecraft.src.buildcraft.api.BuildCraftAPI;
import net.minecraft.src.buildcraft.api.IPipe;
import net.minecraft.src.buildcraft.api.Trigger;
import net.minecraft.src.buildcraft.core.AssemblyRecipe;
import net.minecraft.src.buildcraft.core.CoreProxy;
import net.minecraft.src.buildcraft.core.DefaultProps;
import net.minecraft.src.buildcraft.core.ItemBuildCraftTexture;
import net.minecraft.src.buildcraft.transport.ActionEnergyPulser;
import net.minecraft.src.buildcraft.transport.ActionSignalOutput;
import net.minecraft.src.buildcraft.transport.BlockGenericPipe;
import net.minecraft.src.buildcraft.transport.BptBlockPipe;
import net.minecraft.src.buildcraft.transport.BptItemPipeDiamond;
import net.minecraft.src.buildcraft.transport.BptItemPipeIron;
import net.minecraft.src.buildcraft.transport.BptItemPipeWodden;
import net.minecraft.src.buildcraft.transport.GuiHandler;
import net.minecraft.src.buildcraft.transport.ItemGate;
import net.minecraft.src.buildcraft.transport.LegacyBlock;
import net.minecraft.src.buildcraft.transport.LegacyTile;
import net.minecraft.src.buildcraft.transport.Pipe;
import net.minecraft.src.buildcraft.transport.PipeLogicWood;
import net.minecraft.src.buildcraft.transport.PipeTriggerProvider;
import net.minecraft.src.buildcraft.transport.TileDummyGenericPipe;
import net.minecraft.src.buildcraft.transport.TileDummyGenericPipe2;
import net.minecraft.src.buildcraft.transport.TileGenericPipe;
import net.minecraft.src.buildcraft.transport.TriggerPipeContents;
import net.minecraft.src.buildcraft.transport.TriggerPipeContents.Kind;
import net.minecraft.src.buildcraft.transport.TriggerPipeSignal;
import net.minecraft.src.buildcraft.transport.network.ConnectionHandler;
import net.minecraft.src.buildcraft.transport.pipes.PipeItemsCobblestone;
import net.minecraft.src.buildcraft.transport.pipes.PipeItemsDiamond;
import net.minecraft.src.buildcraft.transport.pipes.PipeItemsGold;
import net.minecraft.src.buildcraft.transport.pipes.PipeItemsIron;
import net.minecraft.src.buildcraft.transport.pipes.PipeItemsObsidian;
import net.minecraft.src.buildcraft.transport.pipes.PipeItemsStone;
import net.minecraft.src.buildcraft.transport.pipes.PipeItemsStripes;
import net.minecraft.src.buildcraft.transport.pipes.PipeItemsWood;
import net.minecraft.src.buildcraft.transport.pipes.PipeLiquidsCobblestone;
import net.minecraft.src.buildcraft.transport.pipes.PipeLiquidsGold;
import net.minecraft.src.buildcraft.transport.pipes.PipeLiquidsIron;
import net.minecraft.src.buildcraft.transport.pipes.PipeLiquidsStone;
import net.minecraft.src.buildcraft.transport.pipes.PipeLiquidsWood;
import net.minecraft.src.buildcraft.transport.pipes.PipePowerGold;
import net.minecraft.src.buildcraft.transport.pipes.PipePowerStone;
import net.minecraft.src.buildcraft.transport.pipes.PipePowerWood;
import net.minecraft.src.buildcraft.transport.pipes.PipeStructureCobblestone;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.IIDCallback;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.Property;

public class BuildCraftTransport {

	private static boolean initialized = false;

	public static BlockGenericPipe genericPipeBlock;

	public static int [] diamondTextures = new int [6];

	public static boolean alwaysConnectPipes;
	public static int maxItemsInPipes;

	public static Item pipeWaterproof;
	public static Item pipeGate;
	public static Item pipeGateAutarchic;
	public static Item redPipeWire;
	public static Item bluePipeWire;
	public static Item greenPipeWire;
	public static Item yellowPipeWire;

	public static Item pipeItemsWood;
	public static Item pipeItemsStone;
	public static Item pipeItemsCobblestone;
	public static Item pipeItemsIron;
	public static Item pipeItemsGold;
	public static Item pipeItemsDiamond;
	public static Item pipeItemsObsidian;

	public static Item pipeLiquidsWood;
	public static Item pipeLiquidsCobblestone;
	public static Item pipeLiquidsStone;
	public static Item pipeLiquidsIron;
	public static Item pipeLiquidsGold;

	public static Item pipePowerWood;
	public static Item pipePowerStone;
	public static Item pipePowerGold;

	public static Item pipeItemsStipes;
	public static Item pipeStructureCobblestone;
	public static int groupItemsTrigger;

	public static Trigger triggerPipeEmpty = new TriggerPipeContents(DefaultProps.TRIGGER_PIPE_EMPTY, Kind.Empty);
	public static Trigger triggerPipeItems = new TriggerPipeContents(DefaultProps.TRIGGER_PIPE_ITEMS, Kind.ContainsItems);
	public static Trigger triggerPipeLiquids = new TriggerPipeContents(DefaultProps.TRIGGER_PIPE_LIQUIDS, Kind.ContainsLiquids);
	public static Trigger triggerPipeEnergy = new TriggerPipeContents(DefaultProps.TRIGGER_PIPE_ENERGY, Kind.ContainsEnergy);
	public static Trigger triggerRedSignalActive = new TriggerPipeSignal(DefaultProps.TRIGGER_RED_SIGNAL_ACTIVE, true, IPipe.WireColor.Red);
	public static Trigger triggerRedSignalInactive = new TriggerPipeSignal(DefaultProps.TRIGGER_RED_SIGNAL_INACTIVE, false, IPipe.WireColor.Red);
	public static Trigger triggerBlueSignalActive = new TriggerPipeSignal(DefaultProps.TRIGGER_BLUE_SIGNAL_ACTIVE, true, IPipe.WireColor.Blue);
	public static Trigger triggerBlueSignalInactive = new TriggerPipeSignal(DefaultProps.TRIGGER_BLUE_SIGNAL_INACTIVE, false, IPipe.WireColor.Blue);
	public static Trigger triggerGreenSignalActive = new TriggerPipeSignal(DefaultProps.TRIGGER_GREEN_SIGNAL_ACTIVE, true, IPipe.WireColor.Green);
	public static Trigger triggerGreenSignalInactive = new TriggerPipeSignal(DefaultProps.TRIGGER_GREEN_SIGNAL_INACTIVE, false, IPipe.WireColor.Green);
	public static Trigger triggerYellowSignalActive = new TriggerPipeSignal(DefaultProps.TRIGGER_YELLOW_SIGNAL_ACTIVE, true, IPipe.WireColor.Yellow);
	public static Trigger triggerYellowSignalInactive = new TriggerPipeSignal(DefaultProps.TRIGGER_YELLOW_SIGNAL_INACTIVE, false, IPipe.WireColor.Yellow);

	public static Action actionRedSignal = new ActionSignalOutput(DefaultProps.ACTION_RED_SIGNAL, IPipe.WireColor.Red);
	public static Action actionBlueSignal = new ActionSignalOutput(DefaultProps.ACTION_BLUE_SIGNAL, IPipe.WireColor.Blue);
	public static Action actionGreenSignal = new ActionSignalOutput(DefaultProps.ACTION_GREEN_SIGNAL, IPipe.WireColor.Green);
	public static Action actionYellowSignal = new ActionSignalOutput(DefaultProps.ACTION_YELLOW_SIGNAL, IPipe.WireColor.Yellow);
	public static Action actionEnergyPulser = new ActionEnergyPulser(DefaultProps.ACTION_ENERGY_PULSER);

	private static class PipeRecipe {
		boolean isShapeless = false; // pipe recipes come shaped and unshaped.
		ItemStack result;
		Object [] input;
	}

	private static LinkedList <PipeRecipe> pipeRecipes = new LinkedList <PipeRecipe> ();

	public static void load() {
		// Register connection handler
		MinecraftForge.registerConnectionHandler(new ConnectionHandler());

		// Register gui handler
		MinecraftForge.setGuiHandler(mod_BuildCraftTransport.instance, new GuiHandler());
	}

	public static void initialize () {
		if (initialized)
			return;

		initialized = true;

		mod_BuildCraftCore.initialize();

		Property alwaysConnect = BuildCraftCore.mainConfiguration
				.getOrCreateBooleanProperty("pipes.alwaysConnect",
						Configuration.CATEGORY_GENERAL,
						DefaultProps.PIPES_ALWAYS_CONNECT);
		alwaysConnect.comment = "set to false to deactivate pipe connection rules, true by default";

		alwaysConnectPipes = Boolean.parseBoolean(alwaysConnect.value);

		Property exclusionList = BuildCraftCore.mainConfiguration
				.getOrCreateProperty("woodenPipe.exclusion",
						Configuration.CATEGORY_BLOCK, "");

		PipeLogicWood.excludedBlocks = exclusionList.value.split(",");

		Property maxItemInPipesProp = BuildCraftCore.mainConfiguration
		.getOrCreateIntProperty("pipes.maxItems",
				Configuration.CATEGORY_GENERAL,
				100);
		maxItemInPipesProp.comment = "pipes containing more than this amount of items will explode, not dropping any item";

		maxItemsInPipes = Integer.parseInt(maxItemInPipesProp.value);

		Property groupItemsTriggerProp = BuildCraftCore.mainConfiguration
		.getOrCreateIntProperty("pipes.groupItemsTrigger",
				Configuration.CATEGORY_GENERAL,
				32);
		groupItemsTriggerProp.comment = "when reaching this amount of objects in a pipes, items will be automatically grouped";

		groupItemsTrigger = Integer.parseInt(groupItemsTriggerProp.value);

		BuildCraftCore.mainConfiguration.save();

        
		
		MinecraftForge.registerBlockID(mod_BuildCraftTransport.instance, "pipe", new IIDCallback() {

            @Override
            public void register(String name, int id) {
                genericPipeBlock = new BlockGenericPipe(id);
                CoreProxy.registerBlock(genericPipeBlock);
            }

            @Override
            public void unregister(String name, int id) {
                Block.blocksList[id] = null;
                Item.itemsList[id] = null;
            }
		    
		});
		
		MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeWaterproof", new IIDCallback() {
            
            @Override
            public void unregister(String name, int id) {
                Item.itemsList[id] = null;
            }
            
            @Override
            public void register(String name, int id) {
                pipeWaterproof = new ItemBuildCraftTexture(id - 256).setIconIndex(2 * 16 + 1);
                pipeWaterproof.setItemName("pipeWaterproof");
                CoreProxy.addName(pipeWaterproof, "Pipe Waterproof");
            }
        });

		for (int j = 0; j < PipeLogicWood.excludedBlocks.length; ++j)
			PipeLogicWood.excludedBlocks[j] = PipeLogicWood.excludedBlocks[j]
					.trim();

		// Fixing retro-compatiblity
		mod_BuildCraftTransport.registerTilePipe(TileDummyGenericPipe.class,
				"net.minecraft.src.buildcraft.GenericPipe");
		mod_BuildCraftTransport.registerTilePipe(TileDummyGenericPipe2.class,
				"net.minecraft.src.buildcraft.transport.TileGenericPipe");

		mod_BuildCraftTransport.registerTilePipe(TileGenericPipe.class,
				"net.minecraft.src.buildcraft.transport.GenericPipe");
		
		IIDCallback idc = new IIDCallback() {
            
            @Override
            public void unregister(String name, int id) {
                Item.itemsList[id] = null;
            }
            
            @Override
            public void register(String name, int id) {
                
                if(name.equals("pipeItemsWood")) {
                    pipeItemsWood = createPipe (id, PipeItemsWood.class, "Wooden Transport Pipe", Block.planks, Block.glass, Block.planks);
                    BuildCraftCore.itemBptProps [pipeItemsWood.shiftedIndex] = new BptItemPipeWodden();
                }
                else if(name.equals("pipeItemsCobblestone"))
                    pipeItemsCobblestone = createPipe(id, PipeItemsCobblestone.class, "Cobblestone Transport Pipe", Block.cobblestone, Block.glass, Block.cobblestone);
                else if(name.equals("pipeItemsStone"))
                    pipeItemsStone = createPipe (id, PipeItemsStone.class, "Stone Transport Pipe", Block.stone, Block.glass, Block.stone);
                else if(name.equals("pipeItemsIron")) {
                    pipeItemsIron = createPipe (id, PipeItemsIron.class, "Iron Transport Pipe", Item.ingotIron, Block.glass, Item.ingotIron);
                    BuildCraftCore.itemBptProps [pipeItemsIron.shiftedIndex] = new BptItemPipeIron();
                }
                else if(name.equals("pipeItemsGold"))
                    pipeItemsGold = createPipe (id, PipeItemsGold.class, "Golden Transport Pipe", Item.ingotGold, Block.glass, Item.ingotGold);
                else if(name.equals("pipeItemsDiamond")) {
                    pipeItemsDiamond = createPipe (id, PipeItemsDiamond.class, "Diamond Transport Pipe", Item.diamond, Block.glass, Item.diamond);
                    BuildCraftCore.itemBptProps [pipeItemsDiamond.shiftedIndex] = new BptItemPipeDiamond();
                }
                else if(name.equals("pipeItemsObsidian"))
                    pipeItemsObsidian = createPipe (id, PipeItemsObsidian.class, "Obsidian Transport Pipe", Block.obsidian, Block.glass, Block.obsidian);

                else if(name.equals("pipeLiquidsWood")) {
                    pipeLiquidsWood = createPipe (id, PipeLiquidsWood.class, "Wooden Waterproof Pipe", pipeWaterproof, pipeItemsWood, null);
                    BuildCraftCore.itemBptProps [pipeLiquidsWood.shiftedIndex] = new BptItemPipeWodden();
                }
                else if(name.equals("pipeLiquidsCobblestone"))
                    pipeLiquidsCobblestone = createPipe (id, PipeLiquidsCobblestone.class, "Cobblestone Waterproof Pipe", pipeWaterproof, pipeItemsCobblestone, null);
                else if(name.equals("pipeLiquidsStone"))
                    pipeLiquidsStone = createPipe (id, PipeLiquidsStone.class, "Stone Waterproof Pipe", pipeWaterproof, pipeItemsStone, null);
                else if(name.equals("pipeLiquidsIron")) {
                    pipeLiquidsIron = createPipe (id, PipeLiquidsIron.class, "Iron Waterproof Pipe", pipeWaterproof, pipeItemsIron, null);
                    BuildCraftCore.itemBptProps [pipeLiquidsIron.shiftedIndex] = new BptItemPipeIron();
                }
                else if(name.equals("pipeLiquidsGold"))
                    pipeLiquidsGold = createPipe (id, PipeLiquidsGold.class, "Golden Waterproof Pipe", pipeWaterproof, pipeItemsGold, null);

                else if(name.equals("pipePowerWood"))
                    pipePowerWood = createPipe (id, PipePowerWood.class, "Wooden Conductive Pipe", Item.redstone,  pipeItemsWood, null);
                else if(name.equals("pipePowerStone"))
                    pipePowerStone = createPipe (id, PipePowerStone.class, "Stone Conductive Pipe", Item.redstone, pipeItemsStone, null);
                else if(name.equals("pipePowerGold"))
                    pipePowerGold = createPipe(id, PipePowerGold.class, "Golden Conductive Pipe", Item.redstone, pipeItemsGold, null);
                
                else if(name.equals("pipeStructureCobblestone"))
                    pipeStructureCobblestone = createPipe (id, PipeStructureCobblestone.class, "Cobblestone Structure Pipe", Block.gravel,  pipeItemsCobblestone, null);
                else if(name.equals("pipeItemsStripes"))
                    pipeItemsStipes = createPipe (id, PipeItemsStripes.class, "Stripes Transport Pipe", new ItemStack (Item.dyePowder, 1, 0),  Block.glass, new ItemStack (Item.dyePowder, 1, 11));
            }
        };
        
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeItemsWood", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeItemsCobblestone", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeItemsStone", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeItemsIron", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeItemsGold", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeItemsDiamond", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeItemsObsidian", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeLiquidsWood", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeLiquidsCobblestone", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeLiquidsStone", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeLiquidsIron", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeLiquidsGold", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipePowerWood", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipePowerStone", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipePowerGold", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeStructureCobblestone", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeItemsStripes", idc);

//      dockingStationBlock = new BlockDockingStation(Integer.parseInt(dockingStationId.value));
//		ModLoader.registerBlock(dockingStationBlock);
//		CoreProxy.addName(dockingStationBlock.setBlockName("dockingStation"),
//		"Docking Station");

//		ModLoader.RegisterTileEntity(TileDockingStation.class, "net.minecraft.src.buildcraft.TileDockingStation");

		for (int j = 0; j < 6; ++j)
			diamondTextures [j] = 1 * 16 + 6 + j;
		
		idc = new IIDCallback() {
            
            @Override
            public void unregister(String name, int id) {
                Item.itemsList[id] = null;
            }
            
            @Override
            public void register(String name, int id) {
                if(name.equals("redPipeWire")) {
                    redPipeWire = new ItemBuildCraftTexture(id - 256).setIconIndex(4 * 16 + 0);
                    redPipeWire.setItemName("redPipeWire");
                    CoreProxy.addName(redPipeWire, "Red Pipe Wire");
                                    
                } else if(name.equals("bluePipeWire")) {
                    bluePipeWire = new ItemBuildCraftTexture(id - 256).setIconIndex(4 * 16 + 1);
                    bluePipeWire.setItemName("bluePipeWire");
                    CoreProxy.addName(bluePipeWire, "Blue Pipe Wire");
                    
                } else if(name.equals("greenPipeWire")) {
                    greenPipeWire = new ItemBuildCraftTexture(id - 256).setIconIndex(4 * 16 + 2);
                    greenPipeWire.setItemName("greenPipeWire");
                    CoreProxy.addName(greenPipeWire, "Green Pipe Wire");
                    
                } else if(name.equals("yellowPipeWire")) {
                    yellowPipeWire = new ItemBuildCraftTexture(id - 256).setIconIndex(4 * 16 + 3);
                    yellowPipeWire.setItemName("yellowPipeWire");
                    CoreProxy.addName(yellowPipeWire, "Yellow Pipe Wire");
                    
                } else if(name.equals("pipeGate")) {
                    pipeGate = new ItemGate(id - 256, 0).setIconIndex(2 * 16 + 3);
                    pipeGate.setItemName("pipeGate");

                } else if(name.equals("pipeGateAutarchic")) {
                    pipeGateAutarchic = new ItemGate(id - 256, 1).setIconIndex(2 * 16 + 3);
                    pipeGateAutarchic.setItemName("pipeGateAutarchic");
                }
            }
        };
        
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "redPipeWire", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "bluePipeWire", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "greenPipeWire", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "yellowPipeWire", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeGate", idc);
        MinecraftForge.registerItemID(mod_BuildCraftTransport.instance, "pipeGateAutarchic", idc);

		BuildCraftAPI.registerTriggerProvider(new PipeTriggerProvider());

		MinecraftForge.addRecipeCallback(new Runnable() {
		    public void run() {
		        new BptBlockPipe (genericPipeBlock.blockID);
		        
		        if (BuildCraftCore.loadDefaultRecipes)
		            loadRecipes();
		        loadAssemblyRecipes();
		    }
		});
	}
	
	public static void loadAssemblyRecipes() {
	    BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(Item.dyePowder, 1, 1),
                new ItemStack(Item.redstone, 1),
                new ItemStack(Item.ingotIron, 1) }, 500, new ItemStack(
                redPipeWire, 8)));
	    BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(Item.dyePowder, 1, 4),
                new ItemStack(Item.redstone, 1),
                new ItemStack(Item.ingotIron, 1) }, 500, new ItemStack(
                bluePipeWire, 8)));
	    BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(Item.dyePowder, 1, 2),
                new ItemStack(Item.redstone, 1),
                new ItemStack(Item.ingotIron, 1) }, 500, new ItemStack(
                greenPipeWire, 8)));
	    BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(Item.dyePowder, 1, 11),
                new ItemStack(Item.redstone, 1),
                new ItemStack(Item.ingotIron, 1) }, 500, new ItemStack(
                yellowPipeWire, 8)));
	}

	public static void loadRecipes () {
		CraftingManager craftingmanager = CraftingManager.getInstance();

		// Add base recipe for pipe waterproof.
		craftingmanager.addRecipe(new ItemStack(pipeWaterproof, 1), new Object[] {
			"W ", "  ",
			Character.valueOf('W'), new ItemStack(Item.dyePowder, 1, 2)});

		// Add pipe recipes
		for (PipeRecipe p : pipeRecipes)
			if(p.isShapeless)
				craftingmanager.addShapelessRecipe(p.result, p.input);
			else
				craftingmanager.addRecipe(p.result, p.input);
	}

	private static Item createPipe (int id, Class <? extends Pipe> clas, String descr, Object ingredient1, Object ingredient2, Object ingredient3) {
		String name = Character.toLowerCase(clas.getSimpleName().charAt(0))
				+ clas.getSimpleName().substring(1);

		Item res =  BlockGenericPipe.registerPipe (id, clas);
		res.setItemName(clas.getSimpleName());
		CoreProxy.addName(res, descr);

		// Add appropriate recipe to temporary list
		PipeRecipe recipe = new PipeRecipe ();

		if (ingredient1 != null && ingredient2 != null && ingredient3 != null) {
			recipe.result = new ItemStack(res, 8);
			recipe.input = new Object[] {
				"   ", "ABC", "   ",
				Character.valueOf('A'), ingredient1,
				Character.valueOf('B'), ingredient2,
				Character.valueOf('C'), ingredient3};

			pipeRecipes.add(recipe);
		} else if (ingredient1 != null && ingredient2 != null) {
			recipe.isShapeless = true;
			recipe.result = new ItemStack(res, 1);
			recipe.input = new Object[] { ingredient1, ingredient2};

			pipeRecipes.add(recipe);
		}

		return res;
	}
}
