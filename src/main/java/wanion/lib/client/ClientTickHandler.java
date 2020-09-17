package wanion.lib.client;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import wanion.lib.WanionLib;
import wanion.lib.client.animation.Animation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public final class ClientTickHandler implements WanionLib.IDependency
{
	private final List<Animation> registeredAnimations = new ArrayList<>();

	private ClientTickHandler()
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void registerAnimation(@Nonnull final Animation animation)
	{
		registeredAnimations.add(animation);
	}

	@SubscribeEvent
	public void tickEvent(final TickEvent.ClientTickEvent event)
	{
		registeredAnimations.forEach(Animation::updateAnimation);
	}

	public static ClientTickHandler getInstance()
	{
		return WanionLib.getClientTickHandler();
	}
}