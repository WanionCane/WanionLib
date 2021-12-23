package wanion.lib.client.gui;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import com.google.common.collect.Lists;
import joptsimple.internal.Strings;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.Reference;
import wanion.lib.client.gui.interaction.WInteraction;
import wanion.lib.common.control.energy.EnergyControl;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class EnergyElement extends WElement<EnergyElement>
{
    protected final EnergyControl energyControl;

    public EnergyElement(@Nonnull final EnergyControl energyControl, @Nonnull final WGuiContainer<?> wGuiContainer, final int x, final int y)
    {
        super(wGuiContainer, x, y, 18, 54);
        this.energyControl = energyControl;
        setInteractionCheck(interaction -> false);
        setTooltipSupplier((interaction, stackSupplier) -> Lists.newArrayList(energyControl.getEnergyStored() + " / " + energyControl.getMaxEnergyStored() + " FE", Strings.EMPTY, TextFormatting.GOLD + I18n.format("wanionlib.consumes", energyControl.getEnergyUsage()), TextFormatting.GOLD + I18n.format("wanionlib.per.operation")));
    }

    @Override
    public void draw(@Nonnull final WInteraction wInteraction)
    {
        wGuiContainer.mc.getTextureManager().bindTexture(Reference.GUI_TEXTURES);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        Gui.drawModalRectWithCustomSizedTexture(getUsableX(), getUsableY(), 0, 0, width, height, 128, 128);
        final int size = scalePowerCentage();
        if (size != 0)
            Gui.drawModalRectWithCustomSizedTexture(getUsableX(), getUsableY() + height - size, 18, height - size, 18, size, 128, 128);
    }

    private int scalePowerCentage()
    {
        final int energyStored = energyControl.getEnergyStored();
        return energyStored != 0 ? (int) (height * energyStored / (double) energyControl.getMaxEnergyStored()) : 0;
    }
}