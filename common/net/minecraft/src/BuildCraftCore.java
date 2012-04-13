/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src;

import java.io.File;
import java.util.LinkedList;
import java.util.TreeMap;

import net.minecraft.src.buildcraft.api.Action;
import net.minecraft.src.buildcraft.api.BuildCraftAPI;
import net.minecraft.src.buildcraft.api.LiquidData;
import net.minecraft.src.buildcraft.api.PowerFramework;
import net.minecraft.src.buildcraft.api.Trigger;
import net.minecraft.src.buildcraft.core.ActionMachineControl;
import net.minecraft.src.buildcraft.core.ActionMachineControl.Mode;
import net.minecraft.src.buildcraft.core.ActionRedstoneOutput;
import net.minecraft.src.buildcraft.core.AssemblyRecipe;
import net.minecraft.src.buildcraft.core.BlockIndex;
import net.minecraft.src.buildcraft.core.BptItem;
import net.minecraft.src.buildcraft.core.BuildCraftConfiguration;
import net.minecraft.src.buildcraft.core.CoreProxy;
import net.minecraft.src.buildcraft.core.DefaultActionProvider;
import net.minecraft.src.buildcraft.core.DefaultProps;
import net.minecraft.src.buildcraft.core.DefaultTriggerProvider;
import net.minecraft.src.buildcraft.core.EntityBlock;
import net.minecraft.src.buildcraft.core.EntityLaser;
import net.minecraft.src.buildcraft.core.EntityRobot;
import net.minecraft.src.buildcraft.core.ItemBuildCraftTexture;
import net.minecraft.src.buildcraft.core.RedstonePowerFramework;
import net.minecraft.src.buildcraft.core.TriggerInventory;
import net.minecraft.src.buildcraft.core.TriggerLiquidContainer;
import net.minecraft.src.buildcraft.core.TriggerMachine;
import net.minecraft.src.buildcraft.core.network.ConnectionHandler;
import net.minecraft.src.buildcraft.core.network.EntityIds;
import net.minecraft.src.buildcraft.core.network.PacketUpdate;
import net.minecraft.src.buildcraft.transport.TriggerRedstoneInput;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.IIDCallback;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.Property;

public class BuildCraftCore {

	public static enum RenderMode {Full, NoDynamic};

	public static RenderMode render = RenderMode.Full;

	public static boolean debugMode = false;
	public static boolean modifyWorld = false;
	public static boolean trackNetworkUsage = false;

	public static boolean dropBrokenBlocks = true; // Set to false to prevent the filler from dropping broken blocks.

	public static int updateFactor = 10;

	public static BuildCraftConfiguration mainConfiguration;

	public static TreeMap<BlockIndex, PacketUpdate> bufferedDescriptions = new TreeMap<BlockIndex, PacketUpdate>();

	public static final int trackedPassiveEntityId = 156;

	public static boolean continuousCurrentModel;

	private static boolean initialized = false;
	private static boolean gearsInitialized = false;

	public static Item woodenGearItem;
	public static Item stoneGearItem;
	public static Item ironGearItem;
	public static Item goldGearItem;
	public static Item diamondGearItem;
	public static Item wrenchItem;

	public static int redLaserTexture;
	public static int blueLaserTexture;
	public static int stripesLaserTexture;
	public static int transparentTexture;

	public static int blockByEntityModel;
	public static int pipeModel;
	public static int markerModel;
	public static int oilModel;

	public static Trigger triggerMachineActive = new TriggerMachine(DefaultProps.TRIGGER_MACHINE_ACTIVE, true);
	public static Trigger triggerMachineInactive = new TriggerMachine(DefaultProps.TRIGGER_MACHINE_INACTIVE, false);
	public static Trigger triggerEmptyInventory = new TriggerInventory(DefaultProps.TRIGGER_EMPTY_INVENTORY, TriggerInventory.State.Empty);
	public static Trigger triggerContainsInventory = new TriggerInventory(DefaultProps.TRIGGER_CONTAINS_INVENTORY, TriggerInventory.State.Contains);
	public static Trigger triggerSpaceInventory = new TriggerInventory(DefaultProps.TRIGGER_SPACE_INVENTORY, TriggerInventory.State.Space);
	public static Trigger triggerFullInventory = new TriggerInventory(DefaultProps.TRIGGER_FULL_INVENTORY, TriggerInventory.State.Full);
	public static Trigger triggerEmptyLiquid = new TriggerLiquidContainer(DefaultProps.TRIGGER_EMPTY_LIQUID, TriggerLiquidContainer.State.Empty);
	public static Trigger triggerContainsLiquid = new TriggerLiquidContainer(DefaultProps.TRIGGER_CONTAINS_LIQUID, TriggerLiquidContainer.State.Contains);
	public static Trigger triggerSpaceLiquid = new TriggerLiquidContainer(DefaultProps.TRIGGER_SPACE_LIQUID, TriggerLiquidContainer.State.Space);
	public static Trigger triggerFullLiquid = new TriggerLiquidContainer(DefaultProps.TRIGGER_FULL_LIQUID, TriggerLiquidContainer.State.Full);
	public static Trigger triggerRedstoneActive = new TriggerRedstoneInput(DefaultProps.TRIGGER_REDSTONE_ACTIVE, true);
	public static Trigger triggerRedstoneInactive = new TriggerRedstoneInput(DefaultProps.TRIGGER_REDSTONE_INACTIVE, false);

	public static Action actionRedstone = new ActionRedstoneOutput(DefaultProps.ACTION_REDSTONE);
	public static Action actionOn = new ActionMachineControl(DefaultProps.ACTION_ON, Mode.On);
	public static Action actionOff = new ActionMachineControl(DefaultProps.ACTION_OFF, Mode.Off);
	public static Action actionLoop = new ActionMachineControl(DefaultProps.ACTION_LOOP, Mode.Loop);

	public static String customBuildCraftTexture =
		"/net/minecraft/src/buildcraft/core/gui/block_textures.png";
	
	public static String externalBuildCraftTexture = 
			"/net/minecraft/src/buildcraft/core/gui/external-textures.png";
	
	public static String customBuildCraftSprites =
		"/net/minecraft/src/buildcraft/core/gui/item_textures.png";

	public static String triggerTextures =
		"/net/minecraft/src/buildcraft/core/gui/trigger_textures.png";

	public static LinkedList<AssemblyRecipe> assemblyRecipes = new LinkedList<AssemblyRecipe>();

	public static boolean loadDefaultRecipes = true;
	public static boolean forcePneumaticPower = false;
	public static boolean consumeWaterSources = true;

	public static BptItem[] itemBptProps = new BptItem[Item.itemsList.length];

	public static void load() {
		// Register connection handler
		MinecraftForge.registerConnectionHandler(new ConnectionHandler());
		
		MinecraftForge.registerEntity(EntityRobot.class, mod_BuildCraftCore.instance, EntityIds.ROBOT, 50, 10, true);
		MinecraftForge.registerEntity(EntityBlock.class, mod_BuildCraftCore.instance, EntityIds.BLOCK, 50, 10, true);
		MinecraftForge.registerEntity(EntityLaser.class, mod_BuildCraftCore.instance, EntityIds.LASER, 50, 10, true);
	}

	@SuppressWarnings({ "all" })
	public static void initialize () {
		if (initialized)
			return;

		ModLoader.getLogger().fine ("Starting BuildCraft " + mod_BuildCraftCore.version());
		ModLoader.getLogger().fine ("Copyright (c) SpaceToad, 2011");
		ModLoader.getLogger().fine ("http://www.mod-buildcraft.com");

		System.out.println ("Starting BuildCraft " + mod_BuildCraftCore.version());
		System.out.println ("Copyright (c) SpaceToad, 2011-2012");
		System.out.println ("http://www.mod-buildcraft.com");

		initialized = true;

		mainConfiguration = new BuildCraftConfiguration(new File(
				CoreProxy.getBuildCraftBase(), "config/buildcraft.cfg"), true);
		mainConfiguration.load();

		redLaserTexture = 0 * 16 + 2;
		blueLaserTexture = 0 * 16 + 1;
		stripesLaserTexture = 0 * 16 + 3;
		transparentTexture = 0 * 16 + 0;

		Property continuousCurrent = BuildCraftCore.mainConfiguration
				.getOrCreateBooleanProperty("current.continuous",
						Configuration.CATEGORY_GENERAL,
						DefaultProps.CURRENT_CONTINUOUS);
		continuousCurrent.comment = "set to true for allowing machines to be driven by continuous current";

		continuousCurrentModel = Boolean.parseBoolean(continuousCurrent.value);

		Property trackNetwork = BuildCraftCore.mainConfiguration
		.getOrCreateBooleanProperty("trackNetworkUsage",
				Configuration.CATEGORY_GENERAL,
				false);

		trackNetworkUsage = Boolean.parseBoolean(trackNetwork.value);

		Property dropBlock = BuildCraftCore.mainConfiguration.getOrCreateBooleanProperty("dropBrokenBlocks", Configuration.CATEGORY_GENERAL, true);
		dropBlock.comment = "set to false to prevent fillers from dropping blocks.";
		dropBrokenBlocks = Boolean.parseBoolean(dropBlock.value);

		Property powerFrameworkClass = BuildCraftCore.mainConfiguration
				.getOrCreateProperty("power.framework",
						Configuration.CATEGORY_GENERAL,
						"buildcraft.energy.PneumaticPowerFramework");

		Property factor = BuildCraftCore.mainConfiguration
		.getOrCreateIntProperty("network.updateFactor",
				Configuration.CATEGORY_GENERAL, 10);
		factor.comment =
			"increasing this number will decrease network update frequency, useful for overloaded servers";

		updateFactor = Integer.parseInt(factor.value);

		String prefix = "";

		if (BuildCraftCore.class.getName().startsWith("net.minecraft.src."))
			prefix = "net.minecraft.src.";

		if (forcePneumaticPower)
			try {
				PowerFramework.currentFramework = (PowerFramework) Class
						.forName(prefix + "buildcraft.energy.PneumaticPowerFramework")
						.getConstructor(null).newInstance(null);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		else
			try {
				String className = powerFrameworkClass.value;
				if (className.startsWith("net.minecraft.src."))
					className = className.replace("net.minecraft.src.", "");

				PowerFramework.currentFramework = (PowerFramework) Class
				.forName(prefix + className).getConstructor(null)
				.newInstance(null);
			} catch (Throwable e) {
				e.printStackTrace();
				PowerFramework.currentFramework = new RedstonePowerFramework();
			}
		

		Property modifyWorld = BuildCraftCore.mainConfiguration
        .getOrCreateBooleanProperty("modifyWorld",
                Configuration.CATEGORY_GENERAL, true);
        modifyWorld.comment = "set to false if BuildCraft should not generate custom blocks (e.g. oil)";
        
        BuildCraftCore.modifyWorld = modifyWorld.value.equals("true");
        
        BuildCraftAPI.registerTriggerProvider(new DefaultTriggerProvider());
        BuildCraftAPI.registerActionProvider(new DefaultActionProvider());

		mainConfiguration.save();
		
		
		
        MinecraftForge.registerItemID(mod_BuildCraftCore.instance, "wrench", new IIDCallback() {
            
            @Override
            public void unregister(String name, int id) {
                Item.itemsList[id] = null;
            }
            
            @Override
            public void register(String name, int id) {
                wrenchItem = (new ItemBuildCraftTexture(id - 256))
                             .setIconIndex(0 * 16 + 2)
                             .setItemName("wrenchItem");
                CoreProxy.addName(wrenchItem, "Wrench");
            }
        });

        IIDCallback idc = new IIDCallback() {

            @Override
            public void register(String name, int id) {
                ItemBuildCraftTexture item = new ItemBuildCraftTexture(id - 256);
                item.setItemName(name);
                
                if(name.equals("woodenGearItem")) {
                    woodenGearItem = item;
                    item.setIconIndex(1 * 16 + 0);
                    CoreProxy.addName(item, "Wooden Gear");
                
                } else if(name.equals("stoneGearItem")) {
                    stoneGearItem = item;
                    item.setIconIndex(1 * 16 + 1);
                    CoreProxy.addName(item, "Stone Gear");
                
                } else if(name.equals("ironGearItem")) {
                    ironGearItem = item;
                    item.setIconIndex(1 * 16 + 2);
                    CoreProxy.addName(item, "Iron Gear");
                
                } else if(name.equals("goldGearItem")) {
                    goldGearItem = item;
                    item.setIconIndex(1 * 16 + 3);
                    CoreProxy.addName(item, "Gold Gear");
                
                } else if(name.equals("diamondGearItem")) {
                    diamondGearItem = item;
                    item.setIconIndex(1 * 16 + 4);
                    CoreProxy.addName(item, "Diamond Gear");
                }
            }

            @Override
            public void unregister(String name, int id) {
                Item.itemsList[id] = null;
            }
		    
		};
		
		MinecraftForge.registerItemID(mod_BuildCraftCore.instance, "woodenGearItem", idc);
        MinecraftForge.registerItemID(mod_BuildCraftCore.instance, "stoneGearItem", idc);
        MinecraftForge.registerItemID(mod_BuildCraftCore.instance, "ironGearItem", idc);
        MinecraftForge.registerItemID(mod_BuildCraftCore.instance, "goldGearItem", idc);
        MinecraftForge.registerItemID(mod_BuildCraftCore.instance, "diamondGearItem", idc);
		
		MinecraftForge.addRecipeCallback(new Runnable() {
		    public void run() {
		        BuildCraftAPI.liquids.add(new LiquidData(Block.waterStill.blockID, Block.waterMoving.blockID,
		                Item.bucketWater));
		        BuildCraftAPI.liquids.add(new LiquidData(Block.lavaStill.blockID, Block.lavaMoving.blockID,
		                Item.bucketLava));

		        BuildCraftAPI.softBlocks [Block.tallGrass.blockID] = true;
		        BuildCraftAPI.softBlocks [Block.snow.blockID] = true;
		        BuildCraftAPI.softBlocks [Block.waterMoving.blockID] = true;
		        BuildCraftAPI.softBlocks [Block.waterStill.blockID] = true;

		        if (BuildCraftCore.loadDefaultRecipes)
		            loadRecipes();
		    }
		});
	}

	public static void loadRecipes () {
		CraftingManager craftingmanager = CraftingManager.getInstance();

		craftingmanager.addRecipe(new ItemStack(wrenchItem), new Object[] {
			"I I", " G ", " I ", Character.valueOf('I'), Item.ingotIron,
			Character.valueOf('G'), stoneGearItem });

		craftingmanager.addRecipe(new ItemStack(woodenGearItem), new Object[] {
			" S ", "S S", " S ", Character.valueOf('S'), Item.stick});

		craftingmanager.addRecipe(new ItemStack(stoneGearItem), new Object[] {
			" I ", "IGI", " I ", Character.valueOf('I'), Block.cobblestone,
			Character.valueOf('G'), woodenGearItem });

		craftingmanager.addRecipe(new ItemStack(ironGearItem), new Object[] {
			" I ", "IGI", " I ", Character.valueOf('I'), Item.ingotIron,
			Character.valueOf('G'), stoneGearItem });

		craftingmanager.addRecipe(new ItemStack(goldGearItem), new Object[] {
			" I ", "IGI", " I ", Character.valueOf('I'), Item.ingotGold,
			Character.valueOf('G'), ironGearItem });

		craftingmanager.addRecipe(new ItemStack(diamondGearItem), new Object[] {
			" I ", "IGI", " I ", Character.valueOf('I'), Item.diamond,
			Character.valueOf('G'), goldGearItem });
	}


	public static void initializeModel (BaseMod mod) {
		 blockByEntityModel = ModLoader.getUniqueBlockModelID(mod, true);
		 pipeModel = ModLoader.getUniqueBlockModelID(mod, true);
		 markerModel = ModLoader.getUniqueBlockModelID(mod, false);
		 oilModel = ModLoader.getUniqueBlockModelID(mod, false);
	}

}
