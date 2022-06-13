package net.runelite.client.plugins.lnpchop;

import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;
import net.runelite.api.Client;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.ColorUtil;
import org.apache.commons.lang3.time.DurationFormatUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

@Singleton
public class LNPCHopOverlay extends OverlayPanel {

    private final LNPCHopPlugin plugin;
    private final LNPCHopConfig config;

    private String timeFormat;
    private String infoStatus = "Starting...";

    @Inject
    private LNPCHopOverlay(final LNPCHopPlugin plugin, final LNPCHopConfig config){
        super(plugin);
        setPosition(OverlayPosition.BOTTOM_LEFT);
        this.plugin = plugin;
        this.config = config;
        getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "airs overlay"));
    }

    public Dimension render(Graphics2D graphics) {
        if (this.plugin.getRunningTimer() == null || !this.config.enableUI()) {
            return null;
        }
        TableComponent tableComponent = new TableComponent();
        tableComponent.setColumnAlignments(new TableAlignment[] { TableAlignment.LEFT, TableAlignment.RIGHT });
        Duration duration = Duration.between(this.plugin.getRunningTimer(), Instant.now());
        this.timeFormat = (duration.toHours() < 1L) ? "mm:ss" : "HH:mm:ss";
        tableComponent.addRow(new String[] { "Time running:", DurationFormatUtils.formatDuration(duration.toMillis(), this.timeFormat) });
        if (plugin.state != null) {
            if (!plugin.state.name().equals("TIMEOUT")) {
                infoStatus = plugin.state.name();
            }
        }
        tableComponent.addRow("Status:", infoStatus);
        if (!tableComponent.isEmpty()) {
            this.panelComponent.setBackgroundColor(ColorUtil.fromHex("#121212"));
            this.panelComponent.setPreferredSize(new Dimension(200, 200));
            this.panelComponent.setBorder(new Rectangle(5, 5, 5, 5));
            this.panelComponent.getChildren().add(TitleComponent.builder()
                    .text("LNPC Hopping")
                    .color(ColorUtil.fromHex("#40C4FF"))
                    .build());
            this.panelComponent.getChildren().add(tableComponent);
        }
        return super.render(graphics);
    }

}
