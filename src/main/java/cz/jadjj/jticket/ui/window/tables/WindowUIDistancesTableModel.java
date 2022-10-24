package cz.jadjj.jticket.ui.window.tables;

import javax.swing.table.DefaultTableModel;

/**
 * Class representing table model for distances table
 * @author jadjj
 */
public class WindowUIDistancesTableModel extends DefaultTableModel
{
    /**
     * Creates new table model for distances table
     * @param data Array with rows which will be displayed in table
     * @param columns Columns of table
     */
    public WindowUIDistancesTableModel (Object[][] data, Object[] columns)
    {
        super(data, columns);
    }
    
    /**
     * Creates new table model for stations table
     */
    public WindowUIDistancesTableModel()
    {
        super();
    }
    
    @Override
    public boolean isCellEditable(int row, int column)
    {
        return (column > 0 && (column - 1) != row);
    }
    
}
