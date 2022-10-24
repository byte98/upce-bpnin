package cz.jadjj.jticket.ui.window.tables;

import javax.swing.table.DefaultTableModel;

/**
 * Class representing table model for stations table
 * @author jadjj
 */
public class WindowUIStationsTableModel extends DefaultTableModel
{
    /**
     * Creates new table model for stations table
     * @param data Array with rows which will be displayed in table
     * @param columns Columns of table
     */
    public WindowUIStationsTableModel (Object[][] data, Object[] columns)
    {
        super(data, columns);
    }
    
    /**
     * Creates new table model for stations table
     */
    public WindowUIStationsTableModel()
    {
        super();
    }
    
    @Override
    public boolean isCellEditable(int row, int column)
    {
        return true;
    }
}
