/** 
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 * 
 * BuildCraft is distributed under the terms of the Minecraft Mod Public 
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.minecraft.src.buildcraft.energy;

import net.minecraft.src.Block;
import net.minecraft.src.BlockStationary;
import net.minecraft.src.BuildCraftCore;
import net.minecraft.src.BuildCraftEnergy;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraft.src.buildcraft.api.liquids.ILiquid;
import net.minecraft.src.forge.ITextureProvider;

public class BlockOilStill extends BlockStationary implements ITextureProvider, ILiquid {

	public BlockOilStill(int i, Material material) {
		super(i, material);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	 public int getRenderType() {
		 return BuildCraftCore.oilModel;
	 }
	
	// Required to allow oilStill.blockID != oilMoving.blockID + 1
	@Override
	public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par1World.getBlockId(par2, par3, par4) == this.blockID)
        {
            this.setNotStationary(par1World, par2, par3, par4);
        }
    }
	private void setNotStationary(World par1World, int par2, int par3, int par4)
    {
        int var5 = par1World.getBlockMetadata(par2, par3, par4);
        par1World.editingBlocks = true;
        par1World.setBlockAndMetadata(par2, par3, par4, BuildCraftEnergy.oilMoving.blockID, var5);
        par1World.markBlocksDirty(par2, par3, par4, par2, par3, par4);
        par1World.scheduleBlockUpdate(par2, par3, par4, BuildCraftEnergy.oilMoving.blockID, this.tickRate());
        par1World.editingBlocks = false;
    }
	 
	@Override
	public String getTextureFile() {
		return BuildCraftCore.customBuildCraftTexture;
	}
	
	@Override public int stillLiquidId() { return BuildCraftEnergy.oilStill.blockID; }
	@Override public boolean isMetaSensitive() { return false; }
	@Override public int stillLiquidMeta() { return 0; }
	
	@Override
    public boolean isBlockReplaceable( World world, int i, int j, int k ) {
	    return true;
    }

}
