package cz.jadjj.jticket.ui.window.tables;

import javax.swing.table.DefaultTableModel;

/**
 * Class representing table model for read only table
 * @author jadjj
 */
public class WindowUIReadOnlyTableModel extends DefaultTableModel
{
    /**
     * Creates new table model for read only table
     * @param data Array with rows which will be displayed in table
     * @param columns Columns of table
     */
    public WindowUIReadOnlyTableModel (Object[][] data, Object[] columns)
    {
        super(data, columns);
    }
    
    /**
     * Creates new table model for read only table
     */
    public WindowUIReadOnlyTableModel()
    {
        super();
    }
    
    @Override
    public boolean isCellEditable(int row, int column)
    {
        return false;
    }
}
