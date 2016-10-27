package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_OutputBus;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.gregtech.api.gui.GUI_MultiMachine;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class GregtechMetaTileEntity_IndustrialMacerator
extends GregtechMeta_MultiBlockBase {
	private static boolean controller;

	public GregtechMetaTileEntity_IndustrialMacerator(int aID, String aName, String aNameRegional) {
		super(aID, aName, aNameRegional);
	}

	public GregtechMetaTileEntity_IndustrialMacerator(String aName) {
		super(aName);
	}

	@Override
	public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
		return new GregtechMetaTileEntity_IndustrialMacerator(this.mName);
	}

	@Override
	public String[] getDescription() {
		return new String[]{
				"Controller Block for the Industrial Maceration Stack",
				"Size[WxHxL]: 3x6x3 (Hollow)", 
				"Controller (Center Bottom)",
				"1x Input Bus (Any bottom layer casing)",
				"5x Output Bus (Any casing besides bottom layer)",
				"1x Maintenance Hatch (Any casing)",
				"1x Energy Hatch (Any casing)",
				"Maceration Stack Casings for the rest (26 at least!)",
				CORE.GT_Tooltip};
	}

	@Override
	public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
		if (aSide == aFacing) {
			return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[64], new GT_RenderedTexture(aActive ? TexturesGtBlock.Overlay_MatterFab_Active : TexturesGtBlock.Overlay_MatterFab)};
		}
		return new ITexture[]{Textures.BlockIcons.CASING_BLOCKS[64]};
	}

	@Override
	public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
		return new GUI_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, getLocalName(), "MacerationStack.png");
	}

	@Override
	public GT_Recipe.GT_Recipe_Map getRecipeMap() {
		return GT_Recipe.GT_Recipe_Map.sMaceratorRecipes;
	}

	/*@Override
	public boolean isCorrectMachinePart(ItemStack aStack) {
		return true;
	}*/

	@Override
	public boolean isFacingValid(byte aFacing) {
		return aFacing > 1;
	}

	@Override
	public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
		super.onPreTick(aBaseMetaTileEntity, aTick);
		if ((aBaseMetaTileEntity.isClientSide()) && (aBaseMetaTileEntity.isActive()) && (aBaseMetaTileEntity.getFrontFacing() != 1) && (aBaseMetaTileEntity.getCoverIDAtSide((byte) 1) == 0) && (!aBaseMetaTileEntity.getOpacityAtSide((byte) 1))) {
			Random tRandom = aBaseMetaTileEntity.getWorld().rand;
			aBaseMetaTileEntity.getWorld().spawnParticle("smoke", aBaseMetaTileEntity.getXCoord() + 0.8F - tRandom.nextFloat() * 0.6F, aBaseMetaTileEntity.getYCoord() + 0.3f + tRandom.nextFloat() * 0.2F, aBaseMetaTileEntity.getZCoord() + 1.2F - tRandom.nextFloat() * 1.6F, 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
		super.startSoundLoop(aIndex, aX, aY, aZ);
		if (aIndex == 1) {
			GT_Utility.doSoundAtClient((String) GregTech_API.sSoundList.get(Integer.valueOf(201)), 10, 1.0F, aX, aY, aZ);
		}
	}

	@Override
	public void startProcess() {
		sendLoopStart((byte) 1);
	}

	@Override
	public boolean checkRecipe(ItemStack aStack) {
		Utils.LOG_INFO("Starting Maceration Stack.1");
		ArrayList<ItemStack> tInputList = getStoredInputs();
		for (int i = 0; i < tInputList.size() - 1; i++) {
			for (int j = i + 1; j < tInputList.size(); j++) {
				if (GT_Utility.areStacksEqual((ItemStack) tInputList.get(i), (ItemStack) tInputList.get(j))) {
					if (((ItemStack) tInputList.get(i)).stackSize >= ((ItemStack) tInputList.get(j)).stackSize) {
						tInputList.remove(j--);
					} else {
						tInputList.remove(i--);
						break;
					}
				}
			}
		}
		Utils.LOG_INFO("Starting Maceration Stack.2");
		ItemStack[] tInputs = (ItemStack[]) Arrays.copyOfRange(tInputList.toArray(new ItemStack[tInputList.size()]), 0, 2);

		boolean mOutputHatch1, mOutputHatch2, mOutputHatch3, mOutputHatch4, mOutputHatch5 = false;
		int sizeInventory1 = this.mOutputBusses.get(0).mInventory.length;
		int sizeInventory2 = this.mOutputBusses.get(0).mInventory.length;
		int sizeInventory3 = this.mOutputBusses.get(0).mInventory.length;
		int sizeInventory4 = this.mOutputBusses.get(0).mInventory.length;
		int sizeInventory5 = this.mOutputBusses.get(0).mInventory.length;


		int sizeBusInventory1 = this.mOutputBusses.get(0).getSizeInventory();
		int sizeBusInventory2 = this.mOutputBusses.get(0).getSizeInventory();
		int sizeBusInventory3 = this.mOutputBusses.get(0).getSizeInventory();
		int sizeBusInventory4 = this.mOutputBusses.get(0).getSizeInventory();
		int sizeBusInventory5 = this.mOutputBusses.get(0).getSizeInventory();

		Utils.LOG_INFO("sizeInventory: "+sizeInventory1+" | sizeBusInventory: "+sizeBusInventory1);
		Utils.LOG_INFO("sizeInventory: "+sizeInventory2+" | sizeBusInventory: "+sizeBusInventory2);
		Utils.LOG_INFO("sizeInventory: "+sizeInventory3+" | sizeBusInventory: "+sizeBusInventory3);
		Utils.LOG_INFO("sizeInventory: "+sizeInventory4+" | sizeBusInventory: "+sizeBusInventory4);
		Utils.LOG_INFO("sizeInventory: "+sizeInventory5+" | sizeBusInventory: "+sizeBusInventory5);

		mOutputHatch1 = (this.mOutputBusses.get(0).mInventory.length<=this.mOutputBusses.get(0).getSizeInventory()) ? true : false;
		mOutputHatch2 = (this.mOutputBusses.get(1).mInventory.length<=this.mOutputBusses.get(1).getSizeInventory()) ? true : false;
		mOutputHatch3 = (this.mOutputBusses.get(2).mInventory.length<=this.mOutputBusses.get(2).getSizeInventory()) ? true : false;
		mOutputHatch4 = (this.mOutputBusses.get(3).mInventory.length<=this.mOutputBusses.get(3).getSizeInventory()) ? true : false;
		mOutputHatch5 = (this.mOutputBusses.get(4).mInventory.length<=this.mOutputBusses.get(4).getSizeInventory()) ? true : false;

		Utils.LOG_INFO("Starting Maceration Stack.4");
		int validHatches=0;
		validHatches = mOutputHatch1 ? validHatches+1 : validHatches;
		validHatches = mOutputHatch2 ? validHatches+1 : validHatches;
		validHatches = mOutputHatch3 ? validHatches+1 : validHatches;
		validHatches = mOutputHatch4 ? validHatches+1 : validHatches;
		validHatches = mOutputHatch5 ? validHatches+1 : validHatches;

		Utils.LOG_INFO("Valid Output Hatches: "+validHatches);

		if (tInputList.size() > 0 && validHatches >= 1) {
			GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sMaceratorRecipes.findRecipe(getBaseMetaTileEntity(), false, 9223372036854775807L, null, tInputs);
			if ((tRecipe != null) && (tRecipe.isRecipeInputEqual(true, null, tInputs))) {
				this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
				this.mEfficiencyIncrease = 10000;

				Utils.LOG_INFO("Valid Recipe Hatches: "+validHatches);
				
				this.mEUt = (-tRecipe.mEUt);
				this.mMaxProgresstime = Math.max(1, (tRecipe.mDuration/5));
				ItemStack[] outputs = new ItemStack[mOutputItems.length];
				for (int i = 0; i < mOutputItems.length; i++){
					if (i==0) {
						outputs[0] =  tRecipe.getOutput(i);
					}
					else if (getBaseMetaTileEntity().getRandomNumber(7500) < tRecipe.getOutputChance(i)){
						outputs[i] = tRecipe.getOutput(i); 
					}
					else {
						outputs[i] = null;
					}
				}

				this.mOutputItems = outputs;
				sendLoopStart((byte) 20);
				updateSlots();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
		int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
		int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
		if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
			return false;
		}
		int tAmount = 0;
		controller = false;
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				for (int h = 0; h < 6; h++) {
					if (!(i == 0 && j == 0 && (h > 0 && h < 5)))//((h > 0)&&(h<5)) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))
					{
						IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
						if ((!addMaintenanceToMachineList(tTileEntity, 64)) && (!addInputToMachineList(tTileEntity, 64)) && (!addOutputToMachineList(tTileEntity, 64)) && (!addEnergyInputToMachineList(tTileEntity, 64)) && (!ignoreController(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)))) {
							if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
								Utils.LOG_INFO("Returned False 1");
								return false;
							}
							if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 7) {
								Utils.LOG_INFO("Returned False 2");
								return false;
							}
							tAmount++;
						}
					}
				}
			}
		}
		if (this.mOutputHatches.size() != 0 || this.mInputBusses.size() != 1 || this.mOutputBusses.size() != 5) {
			Utils.LOG_INFO("Returned False 3");
			return false;
		}
		int height = this.getBaseMetaTileEntity().getYCoord();
		if (this.mInputBusses.get(0).getBaseMetaTileEntity().getYCoord() != height) {
			Utils.LOG_INFO("height: "+height+" | Returned False 4");
			return false;
		}
		GT_MetaTileEntity_Hatch_OutputBus[] tmpHatches = new GT_MetaTileEntity_Hatch_OutputBus[5];
		for (int i = 0; i < this.mOutputBusses.size(); i++) {
			int hatchNumber = this.mOutputBusses.get(i).getBaseMetaTileEntity().getYCoord() - 1 - height;
			if (tmpHatches[hatchNumber] == null) {
				tmpHatches[hatchNumber] = this.mOutputBusses.get(i);
			} else {
				Utils.LOG_INFO("Returned False 5");
				return false;
			}
		}
		this.mOutputBusses.clear();
		for (int i = 0; i < tmpHatches.length; i++) {
			this.mOutputBusses.add(tmpHatches[i]);
		}
		return tAmount >= 26;
	}

	/*public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        int xDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetX;
        int zDir = ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()).offsetZ;
        if (!aBaseMetaTileEntity.getAirOffset(xDir, 1, zDir)) {
            return false;
        }
        int tAmount = 0;
        controller = false;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                for (int h = 0; h < 6; h++) {
                    if (!(i == 0 && j == 0 && (h > 0 && h < 5)))//((h > 0)&&(h<5)) || (((xDir + i != 0) || (zDir + j != 0)) && ((i != 0) || (j != 0)))
                    {
                        IGregTechTileEntity tTileEntity = aBaseMetaTileEntity.getIGregTechTileEntityOffset(xDir + i, h, zDir + j);
                        if ((!addMaintenanceToMachineList(tTileEntity, 64)) && (!addInputToMachineList(tTileEntity, 64)) && (!addOutputToMachineList(tTileEntity, 64)) && (!addEnergyInputToMachineList(tTileEntity, 64)) && (!ignoreController(aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j)))) {
                            if (aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j) != ModBlocks.blockCasingsMisc) {
                                return false;
                            }
                            if (aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j) != 1) {
                                return false;
                            }
                            tAmount++;
                        }
                    }
                }
            }
        }
        if (this.mOutputBusses.size() != 5 || this.mInputBusses.size() != 1) {
            return false;
        }
        int height = this.getBaseMetaTileEntity().getYCoord();
        if (this.mInputHatches.get(0).getBaseMetaTileEntity().getYCoord() != height) {
            return false;
        }
        GT_MetaTileEntity_Hatch_OutputBus[] tmpHatches = new GT_MetaTileEntity_Hatch_OutputBus[5];
        for (int i = 0; i < this.mOutputBusses.size(); i++) {
            int hatchNumber = this.mOutputBusses.get(i).getBaseMetaTileEntity().getYCoord() - 1 - height;
            if (tmpHatches[hatchNumber] == null) {
                tmpHatches[hatchNumber] = this.mOutputBusses.get(i);
            } else {
                return false;
            }
        }
        this.mOutputBusses.clear();
        for (int i = 0; i < tmpHatches.length; i++) {
            this.mOutputBusses.add(tmpHatches[i]);
        }
        return tAmount >= 26;
    }*/

	public boolean ignoreController(Block tTileEntity) {
		if (!controller && tTileEntity == GregTech_API.sBlockMachines) {
			return true;
		}
		return false;
	}

	@Override
	public int getMaxEfficiency(ItemStack aStack) {
		return 10000;
	}

	@Override
	public int getPollutionPerTick(ItemStack aStack) {
		return 0;
	}

	/*@Override
	public int getDamageToComponent(ItemStack aStack) {
		return 0;
	}*/

	@Override
	public int getAmountOfOutputs() {
		return 2;
	}

	@Override
	public boolean explodesOnComponentBreak(ItemStack aStack) {
		return false;
	}
}