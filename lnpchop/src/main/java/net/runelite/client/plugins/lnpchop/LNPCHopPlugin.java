package net.runelite.client.plugins.lnpchop;

import com.google.inject.Provides;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.WorldService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.WorldUtil;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldResult;
import org.pf4j.Extension;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Extension
@PluginDescriptor(
		name = "<html><font size=\"\" color=\"green\"<b>LNPC Hopping</font></b></html>",
		description = "Hops worlds to find npc for you",
		enabledByDefault = false,
		tags = {"lh"}
)

public class LNPCHopPlugin extends Plugin {
	private static final Logger log = LoggerFactory.getLogger(LNPCHopPlugin.class);

	@Inject
	private Client client;
	@Inject
	private LNPCHopConfig config;
	@Inject
	private WorldService worldService;
	@Inject
	private ExecutorService executor;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private LNPCHopOverlay overlay;

	private int[] implingCrystalAbove = {1644,1654,7302,7233, 8741, 8742, 8743,8744,8745,8746,
										8747,8748,8749,8750,8751,8752,8753,8754,8755,8756,8757};
	private boolean start = false;
	private List<Integer> worldList;
	private List<Integer> usedList;

	@Getter
	private Instant runningTimer;
	private int timer;

	State state;

	@Provides
	LNPCHopConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(LNPCHopConfig.class);
	}

	@Override
	protected void startUp(){
		worldList = new ArrayList<>();
		usedList = new ArrayList<>();
		getValidWorld();
		setupTimer();
	}

	@Override
	protected void shutDown(){
		resetVar();
	}

	private void resetVar(){
		runningTimer = null;
		state = null;
		this.overlayManager.remove(overlay);
	}

	private void setupTimer(){
		this.timer = config.getMinDelay() + ThreadLocalRandom.current().nextInt(10);
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equalsIgnoreCase("LNPCHopConfig"))
			return;

		switch (event.getKey())
		{
			case "membersWorlds":
				resetList();
				getValidWorld();
				break;
			case "minDelay":
				setupTimer();
				break;
			default:
				break;
		}
	}

	private void countTimer(){
		if (--timer <= 0){
			//Do nothing
		}
	}

	@Subscribe
	public void onGameTick(GameTick event) {
		if (!start)
			return;

		if (!nearNPC()){
			countTimer();
			if (timer == 0){
				handleAction();
				timer = config.getMinDelay();
			}
		}
	}

	@Subscribe
	public void onConfigButtonPressed(ConfigButtonClicked configButtonClicked){
		if (!configButtonClicked.getGroup().equalsIgnoreCase("LNPCHopConfig"))
			return;

		if (configButtonClicked.getKey().equals("startButton")){
			start = !start;
			if (start == true){
				runningTimer = Instant.now();
				state = State.SEARCHING_NPC;
				this.overlayManager.add(this.overlay);
			}else
				resetVar();
		}

	}

	private boolean whatImpling(int npcId)
	{
		if (this.config.getImp().equals(LNPCHopConfig.Impling.CRYSTAL_AND_ABOVE)){
			for (int i : implingCrystalAbove){
				if (i == npcId){
					start = false;
					state = State.FOUND_NPC;
					return true;
				}
			}
		}
		if (this.config.getImp().equals(LNPCHopConfig.Impling.NINJA)){
			if (npcId == 1643 || npcId == 1653){
				start = false;
				state = State.FOUND_NPC;
				return true;
			}
		}

		return false;
	}

	private boolean nearNPC(){
		List<NPC> npcs = client.getNpcs();
		state = State.SEARCHING_NPC;
		for(NPC n : npcs){
			int id = n.getId();
			if (whatImpling(id))
				return true;

			if (this.config.enableCustom() && (id == this.config.getNPCId() || id == this.config.getNPCId2())){
				start = false;
				state = State.FOUND_NPC;
				return true;
			}
		}
		return false;
	}

	private void resetList(){
		//	Resets the lists
		worldList.clear();
		usedList.clear();

		//	Remakes the worldList
		getValidWorld();
		handleAction(); //	Do method again
		log.info("resetList method called");
	}

	private void handleAction(){
		if (worldList != null){
			int id = worldList.get(0);
			hopToWorld(id); //Hop world
			usedList.add(id); //Adds the world id to used list
			worldList.remove(0); //Removes the world id from world list
			log.info("World id + " + id);
		}else
			resetList();
	}

	private void getValidWorld(){
		WorldResult result = worldService.getWorlds();
		if (result == null)
			return;

		List<World> worlds = result.getWorlds();
		//Collections.shuffle(worlds);
		for (World w : worlds){
			if (client.getWorld() == w.getId())
				continue;

			if (w.getTypes().contains(net.runelite.http.api.worlds.WorldType.HIGH_RISK) ||
					w.getTypes().contains(net.runelite.http.api.worlds.WorldType.DEADMAN) ||
					w.getTypes().contains(net.runelite.http.api.worlds.WorldType.PVP) ||
					w.getTypes().contains(net.runelite.http.api.worlds.WorldType.SKILL_TOTAL) ||
					w.getTypes().contains(net.runelite.http.api.worlds.WorldType.BOUNTY) ||
					w.getTypes().contains(net.runelite.http.api.worlds.WorldType.SEASONAL) ||
					config.memberWorlds() != w.getTypes().contains(net.runelite.http.api.worlds.WorldType.MEMBERS))
				continue;
			worldList.add(w.getId());
			log.info(worldList.toString());
		}
		return;
	}

	private void hopToWorld(int worldId){
		assert client.isClientThread();

		state = State.HOPPING_WORLD;

		WorldResult worldResult = worldService.getWorlds();
		//	Don't try to hop if the world doesn't exist
		World world = worldResult.findWorld(worldId);
		if (world == null)
			return;

		final net.runelite.api.World rsWorld = client.createWorld();
		rsWorld.setActivity(world.getActivity());
		rsWorld.setAddress(world.getAddress());
		rsWorld.setId(world.getId());
		rsWorld.setPlayerCount(world.getPlayers());
		rsWorld.setLocation(world.getLocation());
		rsWorld.setTypes(WorldUtil.toWorldTypes(world.getTypes()));

		if (client.getGameState() == GameState.LOGIN_SCREEN) {
			// on the login screen we can just change the world by ourselves
			client.changeWorld(rsWorld);
			return;
		}

		if (client.getWidget(WidgetInfo.WORLD_SWITCHER_LIST) == null) {
			client.openWorldHopper();

			executor.submit(() -> {
				try {
					Thread.sleep(25 + ThreadLocalRandom.current().nextInt(125));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				injector.getInstance(ClientThread.class).invokeLater(() -> {
					if (client.getWidget(WidgetInfo.WORLD_SWITCHER_LIST) != null)
						client.hopToWorld(rsWorld);
				});
			});
		} else {
			client.hopToWorld(rsWorld);
		}
	}
}