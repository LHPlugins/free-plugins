/*
 * Copyright (c) 2022 LH <https://github.com/LHPlugins>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *	list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *	this list of conditions and the following disclaimer in the documentation
 *	and/or other materials provided with the distribution.
 *
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
package net.runelite.client.plugins.lnpchop;

import net.runelite.client.config.*;

@ConfigGroup("LNPCHopConfig")

public interface LNPCHopConfig extends Config {

	@ConfigItem(
			name = "Start/Stop",
			description = "This starts the plugin or stops it",
			position = 0,
			keyName = "startButton"
	)
	default Button startButton(){return new Button();}

	@ConfigItem(
			keyName = "enableUI",
			name = "Enable UI",
			description = "Displays UI for the plugin",
			position = 1
	)
	default boolean enableUI(){return true;}

	@ConfigSection(
			name = "Custom NPC",
			description = "",
			position = 0,
			keyName = "customSection"
	)
	String customSection = "custom NPC";

	@ConfigItem(
			name = "Enable Custom NPC",
			description = "Enable custom searching npc",
			keyName = "enableCustom",
			position = 0,
			section = customSection
	)
	default boolean enableCustom(){return false;}

	@ConfigItem(
			name = "ID of NPC #1",
			description = "Choose the id of the npc #1",
			position = 1,
			keyName = "idnpc1",
			section = customSection,
			hidden = true,
			unhide = "enableCustom"
	)
	default int getNPCId(){
		return 0;
	}

	@ConfigItem(
			name = "ID of NPC #2",
			description = "Choose the id of the npc #2",
			position = 1,
			keyName = "idnpc2",
			section = customSection,
			hidden = true,
			unhide = "enableCustom"
	)
	default int getNPCId2(){
		return 0;
	}

	@ConfigItem(
			position = 2,
			name = "Implings",
			description = "Checks for implings around you",
			keyName = "impling"
	)
	default Impling getImp(){return Impling.NONE;}

	@ConfigItem(
			keyName = "minDelay",
			name = "Minimum Delay",
			description = "Choose the minimum delay to takes to hop worlds",
			position = 3
	)
	default int getMinDelay(){return 15;}

	@ConfigItem(
			name = "Members Worlds",
			keyName = "membersWorlds",
			description = "Hop to members worlds",
			position = 5
	)
	default boolean memberWorlds(){
		return true;
	}

	enum Impling{
		NONE,
		NINJA,
		CRYSTAL_AND_ABOVE
	}

}