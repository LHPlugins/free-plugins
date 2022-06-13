package net.runelite.client.plugins.lshamanspawnstimer;

import lombok.Getter;
import net.runelite.api.NPC;

public class Spawn {

    private static final int DESPAWNER_TIMER = 9;

    @Getter
    private int timer;

    @Getter
    private final NPC npc;

    public Spawn(final NPC npc){
        this.npc = npc;
        this.timer = DESPAWNER_TIMER;
    }

    public void updateTimer(){

        if (--timer <= 0){
            //Do nothing
        }

    }
}
