/*
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

package net.runelite.client.plugins.lshamanspawnstimer;

import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

import java.util.ArrayList;
import java.util.List;

@Extension
@PluginDescriptor(
	name = "<html><font size=\"\" color=\"green\"<b>LShamanSpawnsTimer</font></b></html>",
	enabledByDefault = false,
	description = "Adds a timer on the purple spawns from lizardman shaman",
	tags = {"lh"}
)
@Singleton
public class LShamanSpawnsTimerPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private TimerOverlay timerOverlay;

	@Getter
	private List<Spawn> npcs;

	@Override
	protected void startUp() {
		overlayManager.add(timerOverlay);
		npcs = new ArrayList<>();
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(timerOverlay);
	}

	@Subscribe
	private void onGameTick(final GameTick event)
	{
		if (npcs == null)
			return;

		handleSpawns();
	}

	@Subscribe
	public void onNpcSpawned(final NpcSpawned event){
		final NPC npc = event.getNpc();
		final int id = npc.getId();

		if (id == 6768)
			npcs.add(new Spawn(npc));
	}

	private void handleSpawns(){
		for (Spawn s : npcs){
			if (s.getTimer() >= 0)
				s.updateTimer();
		}
	}

	@Subscribe
	private void onNpcDespawned(final NpcDespawned event)
	{
		if (npcs != null)
			return;

		for (Spawn s : npcs){
			if (event.getNpc() == s.getNpc()){
				npcs.remove(s);
				overlayManager.remove(timerOverlay);
			}
		}
	}

}
