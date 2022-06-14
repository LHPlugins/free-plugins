package net.runelite.client.plugins.lthralls;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.widgets.WidgetInfo;

@Getter
@AllArgsConstructor
public enum Thralls {

        RESURRECT_LESSER_GHOST("Resurrect Lesser Ghost", WidgetInfo.SPELL_RESURRECT_LESSER_GHOST),
        RESURRECT_LESSER_SKELETON("Resurrect Lesser Skeleton", WidgetInfo.SPELL_RESURRECT_LESSER_SKELETON),
        RESURRECT_LESSER_ZOMBIE("Resurrect Lesser Zombie", WidgetInfo.SPELL_RESURRECT_LESSER_ZOMBIE),
        RESURRECT_SUPERIOR_GHOST("Resurrect Superior Ghost", WidgetInfo.SPELL_RESURRECT_SUPERIOR_GHOST),
        RESURRECT_SUPERIOR_SKELETON("Resurrect Superior Skeleton", WidgetInfo.SPELL_RESURRECT_SUPERIOR_SKELETON),
        RESURRECT_SUPERIOR_ZOMBIE("Resurrect Superior Zombie", WidgetInfo.SPELL_RESURRECT_SUPERIOR_ZOMBIE),
        RESURRECT_GREATER_GHOST("Resurrect Greater Ghost", WidgetInfo.SPELL_RESURRECT_GREATER_GHOST),
        RESURRECT_GREATER_SKELETON("Resurrect Greater Skeleton", WidgetInfo.SPELL_RESURRECT_GREATER_SKELETON),
        RESURRECT_GREATER_ZOMBIE("Resurrect Greater Zombie", WidgetInfo.SPELL_RESURRECT_GREATER_ZOMBIE);

        private String name;
        private WidgetInfo spell;

        @Override
        public String toString() {
            return getName();
        }
}
