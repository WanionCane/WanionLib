package wanion.lib.module;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.Logger;
import wanion.lib.WanionLib;
import wanion.lib.common.Instantiator;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class AbstractModule
{
	protected final Manager manager;
	private final String moduleName;

	protected AbstractModule(@Nonnull final String moduleName, @Nonnull final Instantiator<AbstractModuleThread> moduleThreadInstantiator)
	{
		this.moduleName = moduleName;
		manager = new Manager(moduleThreadInstantiator);
	}

	protected abstract void init();

	final void start(@Nonnull final LoadStage loadStage, @Nonnull final Manager manager)
	{
		final List<AbstractModuleThread> threadList = manager.getInstances(loadStage);
		if (threadList.isEmpty())
			return;
		final ExecutorService moduleThreadExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		final Logger logger = WanionLib.getLogger();
		final ModContainer modContainer = Loader.instance().activeModContainer();
		if (modContainer == null)
			throw new RuntimeException("This should never happen.");
		logger.info(modContainer.getName() + ": Initializing Module: " + moduleName + ", Side: " + WordUtils.capitalizeFully(FMLCommonHandler.instance().getSide().name()));
		try {
			final long initialTime = System.nanoTime();
			final List<Future<String>> futureOfThreads = moduleThreadExecutor.invokeAll(threadList);
			final long took = System.nanoTime() - initialTime;
			for (final Future<String> threadModuleSay : futureOfThreads)
				logger.info(threadModuleSay.get());
			logger.info(modContainer.getName() + ": All " + threadList.size() + " " + moduleName + "s took " + took / 1000000 + "ms to finish. at load stage " + WordUtils.capitalizeFully(loadStage.name().replace("_", " ")).replace(" ", ""));
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Something really bad happened on " + moduleName + " at load stage " + loadStage.name());
			e.printStackTrace();
		}
	}

	@Nonnull
	public String getModuleName()
	{
		return moduleName;
	}

	@Nonnull
	public Manager getManager()
	{
		return manager;
	}

	public static class Manager
	{
		private final Map<LoadStage, Set<Class<? extends AbstractModuleThread>>> loadStageMap = new EnumMap<>(LoadStage.class);
		private final Set<Class<? extends AbstractModuleThread>> threadClassSet = new HashSet<>();
		private final Instantiator<AbstractModuleThread> instantiator;

		public Manager(@Nonnull final Instantiator<AbstractModuleThread> instantiator)
		{
			this.instantiator = instantiator;
			for (final LoadStage loadStage : LoadStage.values())
				loadStageMap.put(loadStage, new LinkedHashSet<>());
		}

		public boolean add(@Nonnull final Class<? extends AbstractModuleThread> moduleThreadClass)
		{
			final LoadStage loadStage = (moduleThreadClass.isAnnotationPresent(SpecifiedLoadStage.class)) ? moduleThreadClass.getAnnotation(SpecifiedLoadStage.class).stage() : LoadStage.POST_INIT;
			final Set<Class<? extends AbstractModuleThread>> classSet = loadStageMap.get(loadStage);
			return !classSet.contains(moduleThreadClass) && !threadClassSet.contains(moduleThreadClass) && threadClassSet.add(moduleThreadClass) && classSet.add(moduleThreadClass);
		}

		public boolean isEmpty()
		{
			return loadStageMap.values().stream().allMatch(Set::isEmpty);
		}

		public boolean isEmpty(final LoadStage loadStage)
		{
			return loadStageMap.get(loadStage).isEmpty();
		}

		public List<AbstractModuleThread> getInstances(final LoadStage loadStage)
		{
			final List<AbstractModuleThread> abstractModuleThreads = new ArrayList<>();
			if (!loadStageMap.containsKey(loadStage))
				return null;
			loadStageMap.get(loadStage).forEach(t -> {
				try {
					abstractModuleThreads.add(instantiator.instantiate(t));
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			});
			return abstractModuleThreads;
		}

		public boolean containsInstance(@Nonnull Class<? extends AbstractModuleThread> threadClass)
		{
			return threadClassSet.contains(threadClass);
		}
	}
}