/**
 * Copyright (c) SpaceToad, 2011
 * http://www.mod-buildcraft.com
 *
 * BuildCraft is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package net.minecraft.src.buildcraft.transport;


import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Slot;
import net.minecraft.src.TileEntity;
import net.minecraft.src.buildcraft.api.Action;
import net.minecraft.src.buildcraft.api.BuildCraftAPI;
import net.minecraft.src.buildcraft.api.Orientations;
import net.minecraft.src.buildcraft.api.Position;
import net.minecraft.src.buildcraft.api.Trigger;
import net.minecraft.src.buildcraft.api.TriggerParameter;
import net.minecraft.src.buildcraft.core.BuildCraftContainer;

class CraftingGateInterface extends BuildCraftContainer {

	IInventory playerIInventory;
	Pipe pipe;
	
	private final LinkedList<Trigger> _potentialTriggers = new LinkedList <Trigger> ();
	private final LinkedList<Action> _potentialActions = new LinkedList <Action> ();


	public CraftingGateInterface(IInventory playerInventory, Pipe pipe) {
		super(pipe.container.getSizeInventory());
		this.playerIInventory = playerInventory;

		for (int l = 0; l < 3; l++)
			for (int k1 = 0; k1 < 9; k1++)
				addSlot(new Slot(playerInventory, k1 + l * 9 + 9, 8 + k1 * 18,
						123 + l * 18));

		for (int i1 = 0; i1 < 9; i1++)
			addSlot(new Slot(playerInventory, i1, 8 + i1 * 18, 181));

		this.pipe = pipe;
		
		_potentialActions.addAll(pipe.getActions());

		_potentialTriggers.addAll (BuildCraftAPI.getPipeTriggers(pipe));

		for (Orientations o : Orientations.dirs()) {
			Position pos = new Position (pipe.xCoord, pipe.yCoord, pipe.zCoord, o);
			pos.moveForwards(1.0);
			TileEntity tile = pipe.worldObj.getBlockTileEntity((int) pos.x, (int) pos.y, (int) pos.z);
			int blockID = pipe.worldObj.getBlockId((int) pos.x, (int) pos.y, (int) pos.z);
			Block block = Block.blocksList [blockID];

			LinkedList <Trigger> nearbyTriggers = BuildCraftAPI.getNeighborTriggers(block, tile);

			for (Trigger t : nearbyTriggers)
				if (!_potentialTriggers.contains(t))
					_potentialTriggers.add(t);

			LinkedList <Action> nearbyActions = BuildCraftAPI.getNeighborActions(block, tile);

			for (Action a : nearbyActions)
				if (!_potentialActions.contains(a))
					_potentialActions.add(a);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return pipe.container.isUseableByPlayer(entityplayer);
	}
	
	/** TRIGGERS **/
	public boolean hasTriggers(){
		return _potentialTriggers.size() > 0;
	}
	
	public Trigger getFirstTrigger(){
		return _potentialTriggers.size() > 0 ? _potentialTriggers.getFirst() : null; 
	}
	
	public Trigger getLastTrigger(){
		return _potentialTriggers.size() > 0 ? _potentialTriggers.getLast() : null;
	}
	
	public Iterator<Trigger> getTriggerIterator(boolean descending){
		return descending ? _potentialTriggers.descendingIterator():_potentialTriggers.iterator(); 
	}
	
	public boolean isNearbyTriggerActive(Trigger trigger, TriggerParameter parameter){
		return pipe.isNearbyTriggerActive(trigger, parameter);
	}
	
	public void setTrigger(int position, Trigger trigger){
		pipe.setTrigger(position, trigger);
	}
	
	public void setTriggerParameter(int position, TriggerParameter parameter){
		pipe.setTriggerParameter(position, parameter);
	}

	
	/** ACTIONS **/
	public boolean hasActions() {
		return _potentialActions.size() > 0;
	}
	
	public Action getFirstAction(){
		return _potentialActions.size() > 0 ? _potentialActions.getFirst() : null;
	}
	
	public Action getLastAction() {
		return _potentialActions.size() > 0 ? _potentialActions.getLast() : null;
	}
	
	public Iterator<Action> getActionIterator(boolean descending) {
		return descending ? _potentialActions.descendingIterator() : _potentialActions.iterator();
	}
	
	public void setAction(int position, Action action){
		pipe.setAction(position, action);
	}
	
	
	public String getGateGuiFile(){
		return pipe.gate.getGuiFile();
	}
	
	public int getGateOrdinal(){
		return pipe.gate.kind.ordinal();
	}
	
	public String getGateName(){
		return pipe.gate.getName();
	}

}