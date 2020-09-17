package wanion.lib.common;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;
import wanion.lib.common.control.ControlController;
import wanion.lib.common.control.IControlContainer;
import wanion.lib.common.field.FieldController;
import wanion.lib.common.field.IFieldContainer;
import wanion.lib.common.matching.IMatchingContainer;
import wanion.lib.common.matching.MatchingController;

import javax.annotation.Nonnull;
import java.util.Collection;

public class WContainer<T extends WTileEntity> extends Container implements IControlContainer, IFieldContainer, IMatchingContainer
{
    private final Dependencies<IController<?, ?>> controllerHandler = new Dependencies<>();
    private final Collection<IController<?, ?>> controllers = controllerHandler.getInstances();
    private final T wTileEntity;

    public WContainer(@Nonnull final T wTileEntity)
    {
        this.wTileEntity = wTileEntity;
        wTileEntity.getControllers().forEach(controller -> controllerHandler.add((IController<?, ?>) controller.copy()));
    }

    public final T getTile()
    {
        return wTileEntity;
    }

    public final String getTileName()
    {
        return wTileEntity.getName();
    }

    @Override
    public void addListener(@Nonnull final IContainerListener listener)
    {
        super.addListener(listener);
        if (!(listener instanceof EntityPlayerMP))
            return;
        controllers.forEach(controller -> controller.addListener(windowId, this, (EntityPlayerMP) listener));
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();
        controllers.forEach(controller -> controller.detectAndSendChanges(windowId, this));
    }

    @Override
    public final boolean canInteractWith(@Nonnull final EntityPlayer playerIn)
    {
        return wTileEntity.isUsableByPlayer(playerIn);
    }

    @Nonnull
    @Override
    public final ControlController getContainerControlController()
    {
        return controllerHandler.get(ControlController.class);
    }

    @Nonnull
    @Override
    public final FieldController getContainerFieldController()
    {
        return controllerHandler.get(FieldController.class);
    }

    @Nonnull
    @Override
    public final MatchingController getContainerMatchingController()
    {
        return controllerHandler.get(MatchingController.class);
    }

    @Override
    public final Collection<IContainerListener> getListeners()
    {
        return listeners;
    }

    @Override
    public final void readNBT(@Nonnull final NBTTagCompound smartNBT)
    {
        controllers.forEach(controller -> controller.readNBT(smartNBT));
        wTileEntity.markDirty();
    }

    @Override
    public final void receiveNBT(@Nonnull final NBTTagCompound nbtTagCompound)
    {
        getContainerFieldController().readNBT(nbtTagCompound);
        wTileEntity.markDirty();
    }

    @Nonnull
    @Override
    public final ControlController getControlController()
    {
        return wTileEntity.getController(ControlController.class);
    }

    @Nonnull
    @Override
    public final FieldController getFieldController()
    {
        return wTileEntity.getController(FieldController.class);
    }

    @Nonnull
    @Override
    public final MatchingController getMatchingController()
    {
        return wTileEntity.getController(MatchingController.class);
    }
}