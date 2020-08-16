package wanion.lib.common.field.text;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.WanionLib;
import wanion.lib.common.INBTMessage;
import wanion.lib.common.field.IField;

import javax.annotation.Nonnull;

public class TextField implements IField<TextField>
{
	private final String fieldName;
	private String content;
	private EntityPlayerMP player;

	public TextField(@Nonnull final String fieldName)
	{
		this(fieldName, "");
	}

	public TextField(@Nonnull final String fieldName, @Nonnull final String content)
	{
		this(fieldName, content, null);
	}

	public TextField(@Nonnull final String fieldName, @Nonnull final String content, final EntityPlayerMP player)
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
	public boolean canInteractWith(@Nonnull final EntityPlayerMP player)
	{
		return this.player == null || this.player == player;
	}

	@Override
	public void startInteraction(@Nonnull final EntityPlayerMP player)
	{
		this.player = player;
	}

	@Override
	public void endInteraction(@Nonnull final EntityPlayerMP player)
	{
		if (player == this.player)
			this.player = null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getHoveringText(@Nonnull final EntityPlayerMP player)
	{
		return this.player != null && this.player != player ? I18n.format("wanionlib.field.occupied", this.player.getName()) : null;
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
		final EntityPlayerMP updatePlayer = WanionLib.proxy.getPlayerByUsername(fieldUpdate.getString("player"));
		final boolean interacting = fieldUpdate.getBoolean("interacting");
		if (this.player == null && interacting)
			startInteraction(updatePlayer);
		else if (this.player == updatePlayer && !interacting)
			endInteraction(updatePlayer);
		final String content = fieldUpdate.hasKey("content") ? fieldUpdate.getString("content") : null;
		if (content != null)
			this.content = content;
	}

	public void sendTextFieldNBT(final int windowId, @Nonnull final EntityPlayerMP entityPlayer, boolean interacting)
	{
		sendTextFieldNBT(windowId, entityPlayer, interacting, null);
	}

	public void sendTextFieldNBT(final int windowId, @Nonnull final EntityPlayerMP entityPlayer, boolean interacting, final String content)
	{
		final NBTTagCompound textFieldNBT = new NBTTagCompound();
		textFieldNBT.setString("player", entityPlayer.getName());
		textFieldNBT.setBoolean("interacting", interacting);
		if (content != null)
			textFieldNBT.setString("content", content);
		INBTMessage.sendNBT(windowId, textFieldNBT);
	}
}