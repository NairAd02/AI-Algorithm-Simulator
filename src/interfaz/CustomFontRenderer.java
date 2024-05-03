package interfaz;

import java.awt.Component;
import java.awt.Font;

import javax.swing.*;
import javax.swing.table.*;

public class CustomFontRenderer extends DefaultTableCellRenderer {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Font font;

    public CustomFontRenderer(Font font) {
        this.font = font;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        c.setFont(font);
        return c;
    }
}

