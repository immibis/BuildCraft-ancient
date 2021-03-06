/** 
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.minecraft.src.buildcraft.energy;

import net.minecraft.src.BuildCraftEnergy;
import net.minecraft.src.ItemBucket;
import net.minecraft.src.forge.ITextureProvider;

public class ItemBucketOil extends ItemBucket implements ITextureProvider {

	public ItemBucketOil(int i) {
	    // Hack: The full liquid ID can't be set here because oilMoving might
	    // not be assigned - so we create the item with 0 and then set it
	    // later through reflection
		super(i, 0);
		iconIndex = 0 * 16 + 1;
	}

	@Override
	public String getTextureFile() {
		return "/net/minecraft/src/buildcraft/core/gui/item_textures.png";
	}

}
