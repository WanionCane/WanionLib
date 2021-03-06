package wanion.lib.common.control.energy;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.IEnergyStorage;
import wanion.lib.common.control.IControl;

import javax.annotation.Nonnull;

public class EnergyControl implements IEnergyStorage, IControl<EnergyControl>
{
	private final int capacity;
	private int energyUsage;
	private int energy;

	public EnergyControl(final int capacity, final int energyUsage)
	{
		this(capacity, energyUsage, 0);
	}

	public EnergyControl(final int capacity, final int energyUsage, final int energy)
	{
		//this(capacity, baseEnergyUsage, null);
		this.capacity = capacity;
		this.energyUsage = energyUsage;
		this.energy = energy;
	}

	/*
	public EnergyControl(final int capacity, final int baseEnergyUsage, final ISpeedControl speedControl)
	{
		this.capacity = capacity;
		this.baseEnergyUsage = baseEnergyUsage;
		this.energy = 0;
	}
	*/

	@Override
	public int receiveEnergy(final int maxReceive, final boolean simulate)
	{
		if (!canReceive())
			return 0;

		int energyReceived = Math.min(capacity - energy, Math.min(this.capacity, maxReceive));
		if (!simulate)
			energy += energyReceived;
		return energyReceived;
	}

	public int getEnergyUsage()
	{
		return energyUsage;
	}

	public void setEnergyUsage(final int energyUsage)
	{
		this.energyUsage = energyUsage;
	}

	@Override
	public int extractEnergy(final int maxExtract, final boolean simulate)
	{
		return 0;
	}

	@Override
	public int getEnergyStored()
	{
		return energy;
	}

	@Override
	public int getMaxEnergyStored()
	{
		return capacity;
	}

	@Override
	public boolean canExtract()
	{
		return false;
	}

	@Override
	public boolean canReceive()
	{
		return true;
	}

	private void useEnergy(final int energy)
	{
		this.energy -= energy;
	}

	public void setEnergyStored(final int energy)
	{
		this.energy = energy;
	}

	@Override
	public boolean canOperate()
	{
		return getEnergyStored() >= energyUsage;
	}

	@Override
	public void operate()
	{
		useEnergy(energyUsage);
	}

	@Nonnull
	@Override
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound nbtTagCompound = new NBTTagCompound();
		nbtTagCompound.setInteger("Energy", energy);
		return nbtTagCompound;
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		if (nbtTagCompound.hasKey("Energy"))
			setEnergyStored(nbtTagCompound.getInteger("Energy"));
	}

	@Nonnull
	@Override
	public EnergyControl copy()
	{
		return new EnergyControl(capacity, energyUsage, energy);
	}

	@Override
	public boolean equals(final Object obj)
	{
		return obj == this || (obj instanceof EnergyControl && this.energy == ((EnergyControl) obj).energy);
	}
}