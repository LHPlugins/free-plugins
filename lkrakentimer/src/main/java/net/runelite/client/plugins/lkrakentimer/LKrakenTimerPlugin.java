/*
 * Copyright (c) 2022 LH <https://github.com/LHPlugins>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.runelite.client.plugins.lkrakentimer;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.inject.Provides;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ProjectileSpawned;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.iutils.InventoryUtils;
import net.runelite.client.plugins.iutils.iUtils;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static net.runelite.api.MenuAction.ITEM_USE;
import static net.runelite.api.MenuAction.ITEM_USE_ON_NPC;

@Extension
@PluginDependency(iUtils.class)
@PluginDescriptor(
	name = "<html><font size=\"\" color=\"green\"<b>LKrakenTimer</font></b></html>",
	enabledByDefault = false,
	description = "Adds a timer on the whirlpool on krakens to show when to attack for efficiency",
	tags = {"lh"}
)
@Singleton
public class LKrakenTimerPlugin extends Plugin
{
	@Inject
	private LKrakenTimerConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private TimerOverlay timerOverlay;
	@Inject
	private Client client;
	@Getter
	private Whirlpool whirlpool;
	private boolean potionCasted = false;

	@Provides
	LKrakenTimerConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(LKrakenTimerConfig.class);
	}

	@Override
	protected void startUp() {
		if (this.client.getGameState() == GameState.LOGGED_IN)
			overlayManager.add(timerOverlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(timerOverlay);
	}

	@Subscribe
	private void onGameTick(final GameTick event)
	{
		if (whirlpool == null)
			return;

		if (potionCasted)
			handleTimerKraken();

	}

	@Subscribe
	private void onProjectileSpawned(final ProjectileSpawned event) {
		if (whirlpool != null)
			return;

		final Projectile projectile = event.getProjectile();
		final int id = projectile.getId();

		if (id != 49)
		{
			return;
		}

		List<NPC> npcs = client.getNpcs();
		for(NPC n : npcs){
			if (n.getId() == 496) {
				potionCasted = true;
				whirlpool = new Whirlpool(n, config);
				return;
			}
		}
	}

	private void handleTimerKraken(){
		if (whirlpool.getTimer() > 0) {
			whirlpool.updateTimer();
		}else
			reset();
	}

	private void reset(){
		potionCasted = false;
		whirlpool = null;
	}

	private Point getLocation(NPC npc) {
		return new Point(npc.getLocalLocation().getSceneX(),npc.getLocalLocation().getSceneY());
	}


}
