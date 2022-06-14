package net.runelite.client.plugins.lthralls;

import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;
import net.runelite.api.MenuAction;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.ColorUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class LThrallOverlay extends OverlayPanel {

    private final LThrallPlugin plugin;
    private final LThrallConfig config;

    @Inject
    private LThrallOverlay(final LThrallPlugin plugin, final LThrallConfig config){
        super(plugin);
        setPosition(OverlayPosition.BOTTOM_LEFT);
        this.plugin = plugin;
        this.config = config;
        getMenuEntries().add(new OverlayMenuEntry(MenuAction.RUNELITE_OVERLAY_CONFIG, "Configure", "airs overlay"));
    }

    public Dimension render(Graphics2D graphics) {
        if (!this.config.getEnabledUI()) {
            return null;
        }
        TableComponent tableComponent = new TableComponent();
        tableComponent.setColumnAlignments(new TableAlignment[] { TableAlignment.LEFT, TableAlignment.RIGHT });
        tableComponent.addRow("Status: ", plugin.isPluginEnabled()+"");
        if (plugin.isPluginEnabled())
            tableComponent.addRow("Auto casts in: ", plugin.getTimer()+"");
        if (!tableComponent.isEmpty()) {
            this.panelComponent.setBackgroundColor(ColorUtil.fromHex("#b5b3b3"));
            this.panelComponent.setPreferredSize(new Dimension(125, 125));
            this.panelComponent.setBorder(new Rectangle(5, 5, 5, 5));
            this.panelComponent.getChildren().add(TitleComponent.builder()
                    .text("LThrall")
                    .color(ColorUtil.fromHex("#40C4FF"))
                    .build());
            this.panelComponent.getChildren().add(tableComponent);
        }
        return super.render(graphics);
    }

}

