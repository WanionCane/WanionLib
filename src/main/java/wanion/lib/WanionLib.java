package wanion.lib;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;
import wanion.lib.client.ClientTickHandler;
import wanion.lib.common.Dependencies;
import wanion.lib.common.control.ControlsContainer;
import wanion.lib.common.control.IControl;
import wanion.lib.network.ControlsSync;
import wanion.lib.proxy.CommonProxy;

import javax.annotation.Nonnull;

import static wanion.lib.Reference.*;

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, acceptedMinecraftVersions = ACCEPTED_MINECRAFT)
public final class WanionLib
{
	@Mod.Instance(MOD_ID)
	public static WanionLib instance;
	@SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
	public static CommonProxy proxy;
	public static SimpleNetworkWrapper networkWrapper;

	private Dependencies<IDependency> dependencies = new Dependencies<>();
	private Logger logger;

	public static Dependencies<IDependency> getDependencies()
	{
		return instance.dependencies;
	}

	public static Logger getLogger()
	{
		return instance.logger;
	}

	@SideOnly(Side.CLIENT)
	public static ClientTickHandler getClientTickHandler()
	{
		return instance.dependencies.get(ClientTickHandler.class);
	}

	@Mod.EventHandler
	public void preInit(final FMLPreInitializationEvent event)
	{
		networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
		proxy.preInit();
		logger = event.getModLog();
	}

	@Mod.EventHandler
	public void loadComplete(final FMLLoadCompleteEvent event)
	{
		dependencies = null;
	}

	public interface IDependency {}
}