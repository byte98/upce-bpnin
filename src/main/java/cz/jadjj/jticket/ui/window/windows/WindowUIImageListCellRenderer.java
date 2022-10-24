package cz.jadjj.jticket.ui.window.windows;

import java.awt.Component;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;

/**
 * Renderer of list items with images
 * @author Jiri Skoda <skodaji1@uhk.cz.cz>
 */
public class WindowUIImageListCellRenderer extends DefaultListCellRenderer
{
    /**
     * Map of images assigned to items
     */
    private final Map<String, ImageIcon> imageMap;
    
    /**
     * Creates new renderer of list items with images
     * @param m Map of images assigned to items
     */
    public WindowUIImageListCellRenderer(Map<String, ImageIcon> m)
    {
        this.imageMap = m;
    }
    
    @Override
    public Component getListCellRendererComponent(
            JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        JLabel reti = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);
        reti.setIcon(this.imageMap.get((String) value));
        reti.setHorizontalTextPosition(JLabel.RIGHT);
        reti.setFont(reti.getFont().deriveFont((float)16));
        reti.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 60));
        return reti;
    }
    
}
