package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import wanion.lib.client.gui.interaction.WGInteraction;
import wanion.lib.client.gui.interaction.WGKeyInteraction;
import wanion.lib.client.gui.interaction.WGMouseInteraction;
import wanion.lib.common.WContainer;
import wanion.lib.common.WTileEntity;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public abstract class WGContainer<T extends WTileEntity> extends GuiContainer
{
	private final ResourceLocation guiTextureLocation;
	private final WContainer<T> wContainer;
	private final List<WGElement> iwgElements = new ArrayList<>();
	protected final Slot firstPlayerSlot = inventorySlots.getSlot(inventorySlots.inventorySlots.size() - 36);

	public WGContainer(@Nonnull final WContainer<T> wContainer, @Nonnull final ResourceLocation guiTextureLocation)
	{
		super(wContainer);
		this.guiTextureLocation = guiTextureLocation;
		this.wContainer = wContainer;
	}

	public void addElement(@Nonnull final WGElement element)
	{
		iwgElements.add(element);
	}

	@Nonnull
	public WContainer<T> getContainer()
	{
		return wContainer;
	}

	@Nonnull
	public T getTile()
	{
		return wContainer.getTile();
	}

	@Nonnull
	public EntityPlayer getEntityPlayer()
	{
		return Minecraft.getMinecraft().player;
	}

	@Override
	public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
	{
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		final WGInteraction interaction = new WGInteraction(this, mouseX, mouseY);
		getEnabledElements().forEach(element -> element.draw(interaction));
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected final void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(guiTextureLocation);
		final boolean smallGui = xSize < 256 && ySize < 256;
		drawModalRectWithCustomSizedTexture(guiLeft, guiTop, 0, 0, xSize, ySize, smallGui ? 256 : Math.max(xSize, ySize), smallGui ? 256 : Math.max(xSize, ySize));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY)
	{
		fontRenderer.drawString(I18n.format(wContainer.getTileName()), 7, 7, 0x404040);
		fontRenderer.drawString(I18n.format("container.inventory"), firstPlayerSlot.xPos - 1, firstPlayerSlot.yPos - 11, 0x404040);
		final WGInteraction interaction = new WGInteraction(this, mouseX, mouseY);
		getEnabledElements().forEach(element -> element.drawForegroundLayer(interaction));
		for (final GuiButton guibutton : this.buttonList)
			if (guibutton.isMouseOver())
				guibutton.drawButtonForegroundLayer(mouseX, mouseY);
	}
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		final WGKeyInteraction keyInteraction = new WGKeyInteraction(this);
		getInteractableElements(keyInteraction).forEach(element -> element.interaction(keyInteraction));
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		final WGMouseInteraction mouseInteraction = new WGMouseInteraction(this);
		getInteractableElements(mouseInteraction).forEach(element -> element.interaction(mouseInteraction));
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	private Collection<WGElement> getEnabledElements()
	{
		return iwgElements.stream().filter(WGElement::isEnabled).collect(Collectors.toList());
	}

	private Collection<WGElement> getInteractableElements(@Nonnull final WGInteraction wgInteraction)
	{
		return iwgElements.stream().filter(element -> element.isEnabled() && element.canInteractWith(wgInteraction)).collect(Collectors.toList());
	}
}