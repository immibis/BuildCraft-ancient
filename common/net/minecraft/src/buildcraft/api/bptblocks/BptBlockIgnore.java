/** 
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.minecraft.src.buildcraft.api.bptblocks;

import java.util.LinkedList;

import net.minecraft.src.ItemStack;
import net.minecraft.src.buildcraft.api.BptBlock;
import net.minecraft.src.buildcraft.api.BptSlotInfo;
import net.minecraft.src.buildcraft.api.IBptContext;

public class BptBlockIgnore extends BptBlock {

	public BptBlockIgnore(int blockId) {
		super(blockId);
	}

	@Override
	public void addRequirements(BptSlotInfo slot, IBptContext context, LinkedList <ItemStack> requirements) {
		requirements.add(new ItemStack (slot.blockId, 0, 0));
	}

	@Override
	public boolean isValid(BptSlotInfo slot, IBptContext context) {
		return true;
	}

	@Override
	public void rotateLeft(BptSlotInfo slot, IBptContext context) {

	}
	
	@Override
	public boolean ignoreBuilding(BptSlotInfo slot) {
		return true;
	}
	
}
