/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.minecraft.src;

import net.minecraft.src.buildcraft.core.DefaultProps;
import net.minecraft.src.buildcraft.devel.BlockCheat;
import net.minecraft.src.forge.IIDCallback;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.forge.NetworkMod;
import net.minecraft.src.forge.Property;

public class mod_BuildCraftDevel extends NetworkMod {

	public static BlockCheat cheatBlock;

	@Override
    public void load() {

    	BuildCraftCore.debugMode = true;

		mod_BuildCraftCore.initialize();
		
		MinecraftForge.registerBlockID(this, "cheat", new IIDCallback() {
            
            @Override
            public void unregister(String name, int id) {
                Block.blocksList[id] = null;
                Item.itemsList[id] = null;
            }
            
            @Override
            public void register(String name, int id) {
                cheatBlock = new BlockCheat(id);
                ModLoader.registerBlock(cheatBlock);
            }
        });
		
		MinecraftForge.addRecipeCallback(new Runnable() {
		    public void run() {
		        CraftingManager.getInstance().addRecipe(new ItemStack(cheatBlock, 1), new Object[] {
		            "# ", "  ", Character.valueOf('#'), Block.dirt });
		    }
		});
	}

	@Override
	public String getVersion() {
		return DefaultProps.VERSION;
	}

	@Override public boolean clientSideRequired() { return true; }
	@Override public boolean serverSideRequired() { return true; }
}
