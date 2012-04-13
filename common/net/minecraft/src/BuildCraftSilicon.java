/**
 * BuildCraft is open-source. It is distributed under the terms of the
 * BuildCraft Open Source License. It grants rights to read, modify, compile
 * or run the code. It does *NOT* grant the right to redistribute this software
 * or its modifications in any form, binary or source, except if expressively
 * granted by the copyright holder.
 */

package net.minecraft.src;

import net.minecraft.src.buildcraft.api.bptblocks.BptBlockInventory;
import net.minecraft.src.buildcraft.api.bptblocks.BptBlockRotateMeta;
import net.minecraft.src.buildcraft.core.AssemblyRecipe;
import net.minecraft.src.buildcraft.core.CoreProxy;
import net.minecraft.src.buildcraft.core.DefaultProps;
import net.minecraft.src.buildcraft.core.ItemRedstoneChipset;
import net.minecraft.src.buildcraft.silicon.BlockAssemblyTable;
import net.minecraft.src.buildcraft.silicon.BlockLaser;
import net.minecraft.src.buildcraft.silicon.GuiHandler;
import net.minecraft.src.buildcraft.silicon.network.ConnectionHandler;
import net.minecraft.src.forge.IIDCallback;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.Property;

public class BuildCraftSilicon {

	public static int laserBlockModel;

	public static Item redstoneChipset;
	public static BlockLaser laserBlock;
	public static BlockAssemblyTable assemblyTableBlock;

	private static boolean initialized = false;

	public static void load() {
		// Register connection handler
		MinecraftForge.registerConnectionHandler(new ConnectionHandler());

		// Register gui handler
		MinecraftForge.setGuiHandler(mod_BuildCraftSilicon.instance, new GuiHandler());
	}

	public static void initialize() {
		if (initialized)
			return;

		initialized = true;

		mod_BuildCraftCore.initialize();
		
		IIDCallback idc = new IIDCallback() {

            @Override
            public void register(String name, int id) {
                
                if(name.equals("laser")) {
                    laserBlock = new BlockLaser (id);
                    CoreProxy.addName(laserBlock.setBlockName("laserBlock"), "Laser");
                    CoreProxy.registerBlock(laserBlock);
                    
                } else if(name.equals("assemblyTable")) {
                    assemblyTableBlock = new BlockAssemblyTable (id);
                    CoreProxy.addName(assemblyTableBlock.setBlockName("assemblyTableBlock"), "Assembly Table");
                    CoreProxy.registerBlock(assemblyTableBlock);
                    
                }
            }

            @Override
            public void unregister(String name, int id) {
                Block.blocksList[id] = null;
                Item.itemsList[id] = null;
            }
		    
		};
		
		MinecraftForge.registerBlockID(mod_BuildCraftSilicon.instance, "laser", idc);
		MinecraftForge.registerBlockID(mod_BuildCraftSilicon.instance, "assemblyTable", idc);
		
		MinecraftForge.registerItemID(mod_BuildCraftSilicon.instance, "redstoneChipset", new IIDCallback() {
            
            @Override
            public void unregister(String name, int id) {
                Item.itemsList[id] = null;
            }
            
            @Override
            public void register(String name, int id) {
                redstoneChipset = new ItemRedstoneChipset(id - 256);
                redstoneChipset.setItemName("redstoneChipset");
                
                CoreProxy.addName(new ItemStack(redstoneChipset, 1, 0), "Redstone Chipset");
                CoreProxy.addName(new ItemStack(redstoneChipset, 1, 1), "Redstone Iron Chipset");
                CoreProxy.addName(new ItemStack(redstoneChipset, 1, 2), "Redstone Golden Chipset");
                CoreProxy.addName(new ItemStack(redstoneChipset, 1, 3), "Redstone Diamond Chipset");
                CoreProxy.addName(new ItemStack(redstoneChipset, 1, 4), "Pulsating Chipset");
            }
        });

		MinecraftForge.addRecipeCallback(new Runnable() {
		    public void run() {
                CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGate, 1, 0), "Gate");
                CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 0), "Autarchic Gate");
                CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGate, 1, 1), "Iron AND Gate");
                CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 1), "Autarchic Iron AND Gate");
                CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGate, 1, 2), "Iron OR Gate");
                CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 2), "Autarchic Iron OR Gate");
		        CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGate, 1, 3), "Gold AND Gate");
        		CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 3), "Autarchic Gold AND Gate");
        		CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGate, 1, 4), "Gold OR Gate");
        		CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 4), "Autarchic Gold OR Gate");
        		CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGate, 1, 5), "Diamond AND Gate");
        		CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 5), "Autarchic Diamond AND Gate");
        		CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGate, 1, 6), "Diamond OR Gate");
        		CoreProxy.addName(new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 6), "Autarchic Diamond OR Gate");
		    }
		});

		MinecraftForge.addRecipeCallback(new Runnable() {
		    public void run() {
                new BptBlockRotateMeta(laserBlock.blockID, new int [] {2, 5, 3, 4}, true);
                new BptBlockInventory(assemblyTableBlock.blockID);

                if (BuildCraftCore.loadDefaultRecipes)
		            loadRecipes();
		        loadAssemblyRecipes();
		    }
		});
	}
	
	public static void loadAssemblyRecipes() {
	  /// REDSTONE CHIPSETS
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(
                new ItemStack[] { new ItemStack(Item.redstone) }, 10000,
                new ItemStack(redstoneChipset, 1, 0)));
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(Item.redstone), new ItemStack(Item.ingotIron) },
                20000, new ItemStack(redstoneChipset, 1, 1)));
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(Item.redstone), new ItemStack(Item.ingotGold) },
                40000, new ItemStack(redstoneChipset, 1, 2)));
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(Item.redstone), new ItemStack(Item.diamond) },
                80000, new ItemStack(redstoneChipset, 1, 3)));
        // PULSATING CHIPSETS
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(Item.redstone), new ItemStack(Item.enderPearl) },
                40000, new ItemStack(redstoneChipset, 2, 4)));
        
        /// REDSTONE GATES
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(
                new ItemStack[] { new ItemStack(redstoneChipset, 1, 0) },
                20000, new ItemStack(BuildCraftTransport.pipeGate, 1, 0)));
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(
                new ItemStack[] {
                        new ItemStack(BuildCraftTransport.pipeGate, 1, 0),
                        new ItemStack(redstoneChipset, 1, 4),
                        new ItemStack(redstoneChipset, 1, 1)
                },
                10000, new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 0)));
        
        /// IRON AND GATES
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(redstoneChipset, 1, 1),
                new ItemStack(BuildCraftTransport.redPipeWire) }, 40000,
                new ItemStack(BuildCraftTransport.pipeGate, 1, 1)));
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(
                new ItemStack[] {
                        new ItemStack(BuildCraftTransport.pipeGate, 1, 1),
                        new ItemStack(redstoneChipset, 1, 4),
                        new ItemStack(redstoneChipset, 1, 1)
                },
                20000, new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 1)));
        
        /// IRON OR GATES
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(redstoneChipset, 1, 1),
                new ItemStack(BuildCraftTransport.redPipeWire) }, 40000,
                new ItemStack(BuildCraftTransport.pipeGate, 1, 2)));
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(
                new ItemStack[] {
                        new ItemStack(BuildCraftTransport.pipeGate, 1, 2),
                        new ItemStack(redstoneChipset, 1, 4),
                        new ItemStack(redstoneChipset, 1, 1)
                },
                20000, new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 2)));
        
        /// GOLD AND GATES
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(redstoneChipset, 1, 2),
                new ItemStack(BuildCraftTransport.redPipeWire),
                new ItemStack(BuildCraftTransport.bluePipeWire) }, 80000,
                new ItemStack(BuildCraftTransport.pipeGate, 1, 3)));
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(
                new ItemStack[] {
                        new ItemStack(BuildCraftTransport.pipeGate, 1, 3),
                        new ItemStack(redstoneChipset, 1, 4),
                        new ItemStack(redstoneChipset, 1, 1)
                },
                40000, new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 3)));
        
        /// GOLD OR GATES
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(redstoneChipset, 1, 2),
                new ItemStack(BuildCraftTransport.redPipeWire),
                new ItemStack(BuildCraftTransport.bluePipeWire) }, 80000,
                new ItemStack(BuildCraftTransport.pipeGate, 1, 4)));
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(
                new ItemStack[] {
                        new ItemStack(BuildCraftTransport.pipeGate, 1, 4),
                        new ItemStack(redstoneChipset, 1, 4),
                        new ItemStack(redstoneChipset, 1, 1)
                },
                40000, new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 4)));
        
        /// DIAMOND AND GATES
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(redstoneChipset, 1, 3),
                new ItemStack(BuildCraftTransport.redPipeWire),
                new ItemStack(BuildCraftTransport.bluePipeWire),
                new ItemStack(BuildCraftTransport.greenPipeWire),
                new ItemStack(BuildCraftTransport.yellowPipeWire) }, 160000,
                new ItemStack(BuildCraftTransport.pipeGate, 1, 5)));
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(
                new ItemStack[] {
                        new ItemStack(BuildCraftTransport.pipeGate, 1, 5),
                        new ItemStack(redstoneChipset, 1, 4),
                        new ItemStack(redstoneChipset, 1, 1)
                },
                80000, new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 5)));
        
        /// DIAMOND OR GATES
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(new ItemStack[] {
                new ItemStack(redstoneChipset, 1, 3),
                new ItemStack(BuildCraftTransport.redPipeWire),
                new ItemStack(BuildCraftTransport.bluePipeWire),
                new ItemStack(BuildCraftTransport.greenPipeWire),
                new ItemStack(BuildCraftTransport.yellowPipeWire) }, 160000,
                new ItemStack(BuildCraftTransport.pipeGate, 1, 6)));
        BuildCraftCore.assemblyRecipes.add(new AssemblyRecipe(
                new ItemStack[] {
                        new ItemStack(BuildCraftTransport.pipeGate, 1, 6),
                        new ItemStack(redstoneChipset, 1, 4),
                        new ItemStack(redstoneChipset, 1, 1)
                },
                80000, new ItemStack(BuildCraftTransport.pipeGateAutarchic, 1, 6)));
	}

	public static void loadRecipes () {
		CraftingManager craftingmanager = CraftingManager.getInstance();

		craftingmanager.addRecipe(
				new ItemStack(laserBlock),
				new Object[] { "ORR", "DDR", "ORR",
					Character.valueOf('O'), Block.obsidian,
					Character.valueOf('R'), Item.redstone,
					Character.valueOf('D'), Item.diamond,
				});

		craftingmanager.addRecipe(
				new ItemStack(assemblyTableBlock),
				new Object[] { "ORO", "ODO", "OGO",
					Character.valueOf('O'), Block.obsidian,
					Character.valueOf('R'), Item.redstone,
					Character.valueOf('D'), Item.diamond,
					Character.valueOf('G'), BuildCraftCore.diamondGearItem,
				});
	}

	public static void initializeModel(BaseMod mod) {
		laserBlockModel = ModLoader.getUniqueBlockModelID(mod, true);
	}
}
