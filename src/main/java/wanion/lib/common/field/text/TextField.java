package wanion.lib.common.field.text;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.WanionLib;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.common.INBTMessage;
import wanion.lib.common.field.IField;
import wanion.lib.network.SmartNBTMessage;

import javax.annotation.Nonnull;
import java.util.Objects;

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

	public boolean canInteractWith(@Nonnull final EntityPlayer player)
	{
		return this.player == null || this.player == player || this.player.getName().equals(player.getName());
	}

	@Override
	public void startInteraction(@Nonnull final EntityPlayer player)
	{
		this.player = player;
	}

	@Override
	public void endInteraction(@Nonnull final EntityPlayer player)
	{
		if (player == this.player || player.equals(this.player))
			this.player = null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getHoveringText(@Nonnull final WInteraction wInteraction)
	{
		final EntityPlayer player = wInteraction.getEntityPlayer();
		return this.player != null && this.player != player ? I18n.format("wanionlib.field.occupied", this.player.getName()) : null;
	}

	@Nonnull
	@Override
	public NBTTagCompound writeNBT()
	{
		final NBTTagCompound fieldNBT = new NBTTagCompound();
		fieldNBT.setString("fieldName", fieldName);
		fieldNBT.setString("content", content);
		if (player != null)
			fieldNBT.setString("player", player.getName());
		return fieldNBT;
	}

	// since the player only haves a value when there is a player interacting with this field, it should not be saved.
	@Override
	public void afterWriteNBT(@Nonnull final NBTTagCompound smartNBT)
	{
		smartNBT.removeTag("player");
	}

	@Override
	public void readNBT(@Nonnull final NBTTagCompound smartNBT)
	{
		setContent(smartNBT.getString("content"));
		this.player = smartNBT.hasKey("player") ? WanionLib.proxy.getPlayerByUsername(smartNBT.getString("player")) : null;
	}

	@Override
	public void receiveNBT(@Nonnull final NBTTagCompound fieldUpdate)
	{
		if (fieldUpdate.hasNoTags() || !fieldName.equals(fieldUpdate.getString("fieldName")))
			return;
		final EntityPlayer updatePlayer = WanionLib.proxy.getPlayerByUsername(fieldUpdate.getString("player"));
		final boolean interacting = fieldUpdate.getBoolean("interacting");
		if (this.player == null && interacting)
			startInteraction(updatePlayer);
		else if (this.player == updatePlayer && !interacting)
			endInteraction(updatePlayer);
		final String content = fieldUpdate.hasKey("content") ? fieldUpdate.getString("content") : null;
		if (content != null)
			setContent(content);
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj instanceof TextField && fieldName.equals(((TextField) obj).fieldName) && content.equals(((TextField) obj).content) && Objects.equals(this.player, ((TextField) obj).player);
	}

	public int length()
	{
		return content.length();
	}

	// synchronized may be not necessary, but who knows.
	@Nonnull
	public synchronized String getContent()
	{
		return this.content;
	}

	public synchronized void setContent(@Nonnull final String content)
	{
		this.content = content;
	}

	public final boolean isEmpty()
	{
		return content.isEmpty();
	}

	@SideOnly(Side.CLIENT)
	public void sendUpdateToServer(final int windowId)
	{
		final NBTTagCompound smartNBT = new NBTTagCompound();
		final NBTTagList fieldTagList = new NBTTagList();
		fieldTagList.appendTag(writeNBT());
		smartNBT.setTag("field", fieldTagList);
		WanionLib.networkWrapper.sendToServer(new SmartNBTMessage(windowId, smartNBT));
	}

	public void sendTextFieldNBT(final int windowId, @Nonnull final EntityPlayer entityPlayer, boolean interacting)
	{
		sendTextFieldNBT(windowId, entityPlayer, interacting, null);
	}

	public void sendTextFieldNBT(final int windowId, @Nonnull final EntityPlayer entityPlayer, boolean interacting, final String content)
	{
		if (WanionLib.proxy.isServer())
			return;
		final NBTTagCompound textFieldNBT = new NBTTagCompound();
		textFieldNBT.setString("fieldName", fieldName);
		textFieldNBT.setString("player", entityPlayer.getName());
		textFieldNBT.setBoolean("interacting", interacting);
		if (content != null)
			textFieldNBT.setString("content", content);
		INBTMessage.sendNBT(windowId, textFieldNBT);
	}

	@Override
	public String toString()
	{
		return fieldName;
	}
}