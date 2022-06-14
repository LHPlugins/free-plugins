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

package net.runelite.client.plugins.lthralls;

import com.google.inject.Provides;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.MouseListener;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.plugins.iutils.*;
import org.pf4j.Extension;
import javax.inject.Inject;
import java.awt.event.MouseEvent;

@Extension
@PluginDependency(iUtils.class)
@PluginDescriptor(
	name = "<html><font size=\"\" color=\"green\"<b>LThralls</font></b></html>",
	enabledByDefault = false,
	description = "Auto cast thralls for more dps",
	tags = {"lh", "LH"}
)

public class LThrallPlugin extends Plugin implements MouseListener
{
	@Inject
	private Client client;

	@Inject
	private CalculationUtils calc;

	@Inject
	private iUtils utils;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private LThrallOverlay overlay;

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private LThrallConfig config;


	private int recastTimer;
	@Getter
	private int timer;
	@Getter
	private boolean pluginEnabled;

	private Thralls selectedThrall;
	private LegacyMenuEntry targetMenu;

	@Provides
	LThrallConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LThrallConfig.class);
	}

	@Override
	protected void startUp() {
		recastTimer = (int) calcTimer(config.getRecastTimer());
		initVals();
		if (!config.useMouse())
			pluginEnabled = !pluginEnabled;
		overlayManager.add(overlay);
		if (pluginEnabled){
			castThrall();
		}
	}

	@Override
	protected void shutDown() {
		overlayManager.remove(overlay);
		resetVals();
	}

	private void initVals(){
		pluginEnabled = false;
		selectedThrall = config.getThrall();
	}

	private void resetVals(){
		selectedThrall = null;
		timer = recastTimer;
		pluginEnabled = false;
	}

	private void castThrall(){
		Widget widget = client.getWidget(selectedThrall.getSpell());
		if (widget == null)
			return;
		targetMenu = new LegacyMenuEntry("Cast", "", 1 , MenuAction.CC_OP, -1, widget.getId(), false);
		utils.oneClickCastSpell(selectedThrall.getSpell(),targetMenu, widget.getBounds(), sleepDelay());
	}

	private double calcTimer(int configTimer){
		return configTimer * 1.7;
	}

	@Subscribe
	private void onGameTick(final GameTick event)
	{
		if (!pluginEnabled)
			return;
		if (this.client.getVarbitValue(4070) != 3) {
			sendGameMessage("You are on the wrong spellbook");
			return;
		}
		timer--;
		if (timer <= 0){
			timer = recastTimer;
			castThrall();
		}
	}

	private void sendGameMessage(String message) {
		String chatMessage = new ChatMessageBuilder()
				.append(ChatColorType.HIGHLIGHT)
				.append(message)
				.build();

		chatMessageManager
				.queue(QueuedMessage.builder()
						.type(ChatMessageType.CONSOLE)
						.runeLiteFormattedMessage(chatMessage)
						.build());
	}

	private long sleepDelay() {
		long sleepLength = calc.randomDelay(config.sleepWeightedDistribution(), config.sleepMin(), config.sleepMax(), config.sleepDeviation(), config.sleepTarget());
		return sleepLength;
	}
	//	NOT BEING USED ATM
	private int tickDelay() {
		int tickLength = (int) calc.randomDelay(config.tickDelayWeightedDistribution(), config.tickDelayMin(), config.tickDelayMax(), config.tickDelayDeviation(), config.tickDelayTarget());
		return tickLength;
	}

	@Override
	public MouseEvent mouseClicked(MouseEvent e) {
		return e;
	}

	@Override
	public MouseEvent mousePressed(MouseEvent e) {
		if (!config.useMouse())
			return e;

		if (config.mouseButton() == e.getButton())
			pluginEnabled = !pluginEnabled;

		return e;
	}

	@Override
	public MouseEvent mouseReleased(MouseEvent e) {
		return e;
	}

	@Override
	public MouseEvent mouseEntered(MouseEvent e) {
		return e;
	}

	@Override
	public MouseEvent mouseExited(MouseEvent e) {
		return e;
	}

	@Override
	public MouseEvent mouseDragged(MouseEvent e) {
		return e;
	}

	@Override
	public MouseEvent mouseMoved(MouseEvent e) {
		return e;
	}
}
