package net.runelite.client.plugins.lkrakentimer;

import lombok.Getter;
import net.runelite.api.NPC;

public class Whirlpool {

    @Getter
    private int timer;

    @Getter
    private final NPC npc;

    public Whirlpool(final NPC npc, final LKrakenTimerConfig config){
        this.npc = npc;
        this.timer = config.getTimer();
    }

    public void updateTimer(){

        if (--timer <= 0){
            //Do nothing
        }

    }
}
