package net.runelite.client.plugins.lshamanspawnstimer;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import net.runelite.api.Point;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TimerOverlay extends Overlay {

    private final LShamanSpawnsTimerPlugin plugin;
    private final Client client;

    @Inject
    TimerOverlay(final LShamanSpawnsTimerPlugin plugin, final Client client)
    {
        this.plugin = plugin;
        this.client = client;

        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGHEST);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        for (Spawn s : plugin.getNpcs())
        {
            if (s.getNpc() == null)
            {
                continue;
            }

            int ticksLeft = s.getTimer();

            if (ticksLeft <= 0)
            {
                continue;
            }

            final String ticksLeftStr = String.valueOf(ticksLeft);
            Color color = (ticksLeft <= 3 ? Color.RED : Color.GREEN);


            final Point canvasPoint = s.getNpc().getCanvasTextLocation(graphics, ticksLeftStr, 0);
            OverlayUtil.renderTextLocation(graphics, ticksLeftStr, 13, 10, color, canvasPoint, 0);
        }

        return null;
    }
}
