package wanion.lib.common.field;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.client.gui.interaction.WInteraction;

import javax.annotation.Nonnull;

public class CheckBox implements IField<CheckBox>
{
	private final String fieldName;
	private final boolean defaultChecked;
	private boolean checked;

	public CheckBox(@Nonnull final String fieldName)
	{
		this(fieldName, false);
	}

	public CheckBox(@Nonnull final String fieldName, final boolean defaultChecked)
	{
		this.fieldName = fieldName;
		this.defaultChecked = (this.checked = defaultChecked);
	}

	public final CheckBox toggle()
	{
		checked ^= true;
		return this;
	}

	public boolean isChecked()
	{
		return checked;
	}

	@Nonnull
	@Override
	public CheckBox copy()
	{
		return new CheckBox(fieldName, checked);
	}

	@Nonnull
	@Override
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound fieldNBT = new NBTTagCompound();
		fieldNBT.setString("fieldName", fieldName);
		fieldNBT.setBoolean("checked", checked);
		return fieldNBT;
	}

	@Override
	public void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound)
	{
		readNBT(nbtTagCompound);
	}

	@Override
	public String getHoveringText(@Nonnull final WInteraction wInteraction)
	{
		return I18n.format(fieldName);
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound smartNBT)
	{
		this.checked = smartNBT.hasKey("checked") ? smartNBT.getBoolean("checked") : defaultChecked;
	}

	@Nonnull
	@Override
	public String getFieldName()
	{
		return fieldName;
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof CheckBox && fieldName.equals(((CheckBox) obj).fieldName) && checked == ((CheckBox) obj).checked;
	}
}