/** 
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.minecraft.src.buildcraft.energy;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class ItemEngine extends ItemBlock
{

    public ItemEngine(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(int i)
    {
    	return i;
    }

    @SuppressWarnings({ "all" })
    public String getItemNameIS(ItemStack itemstack)
    {
        if (itemstack.getItemDamage() == 0) {
    		return "tile.engineWood";
    	} else if (itemstack.getItemDamage() == 1) {
    		return "tile.engineStone";
    	} else {
    		return "tile.engineIron";
    	}
    }
}
