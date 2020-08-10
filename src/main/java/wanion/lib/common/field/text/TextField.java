package wanion.lib.common.field.text;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.WanionLib;
import wanion.lib.common.field.IField;

import javax.annotation.Nonnull;

public class TextField implements IField<TextField>
{
	private final String fieldName;
	private String content;
	private EntityPlayer player;

	public TextField(@Nonnull final String fieldName)
	{
		this(fieldName, "");
	}

	public TextField(@Nonnull final String fieldName, @Nonnull final String content)
	{
		this(fieldName, content, null);
	}

	public TextField(@Nonnull final String fieldName, @Nonnull final String content, final EntityPlayer player)
	{
		this.fieldName = fieldName;
		this.content = content;
		this.player = player;
	}

	@Nonnull
	@Override
	public String getFieldName()
	{
		return fieldName;
	}

	@Nonnull
	@Override
	public TextField copy()
	{
		return new TextField(fieldName, content, player);
	}

	@Override
	public boolean canInteractWith(@Nonnull final EntityPlayer player)
	{
		return this.player == null || this.player == player;
	}

	@Override
	public void startInteraction(@Nonnull final EntityPlayer player)
	{
		this.player = player;
	}

	@Override
	public void endInteraction(@Nonnull final EntityPlayer player)
	{
		if (player == this.player)
			this.player = null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getHoveringText()
	{
		return null;
	}

	@Override
	public void writeToNBT(@Nonnull final NBTTagCompound smartNBT)
	{
		smartNBT.setString(fieldName, content);
		smartNBT.setString("player", player.getName());
	}

	@Override
	public void readFromNBT(@Nonnull final NBTTagCompound smartNBT)
	{
		this.content = smartNBT.getString(fieldName);
		this.player = WanionLib.proxy.getPlayerByUsername(smartNBT.getString("player"));
	}

	@Override
	public void receiveNBT(@Nonnull final NBTTagCompound fieldUpdate)
	{
		if (fieldUpdate.hasNoTags())
			return;
		final EntityPlayer updatePlayer = fieldUpdate.hasKey("player") ? WanionLib.proxy.getPlayerByUsername(fieldUpdate.getString("player")) : null;
		if (updatePlayer == null)
			return;
		final boolean interacting = fieldUpdate.getBoolean("interacting");
		if (this.player == null && interacting)
			startInteraction(updatePlayer);
		else if (this.player == updatePlayer && !interacting)
			endInteraction(updatePlayer);
		final String content = fieldUpdate.hasKey("content") ? fieldUpdate.getString("content") : null;
		if (content != null)
			this.content = content;
	}
}