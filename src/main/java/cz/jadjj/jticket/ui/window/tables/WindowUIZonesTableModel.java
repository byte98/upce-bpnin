package cz.jadjj.jticket.ui.window.tables;

import cz.jadjj.jticket.data.Station;
import cz.jadjj.jticket.data.Stations;
import javax.swing.table.DefaultTableModel;

/**
 * Class representing table model for zones table
 * @author jadjj
 */
public class WindowUIZonesTableModel extends DefaultTableModel
{
    /**
     * Creates new table model for zones table
     * @param data Array with rows which will be displayed in table
     * @param columns Columns of table
     */
    public WindowUIZonesTableModel (Object[][] data, Object[] columns)
    {
        super(data, columns);
    }
    
    /**
     * Creates new table model for zones table
     */
    public WindowUIZonesTableModel()
    {
        super();
    }
    
    @Override
    public boolean isCellEditable(int row, int column)
    {
        return (column > 1);
    }
    
    /**
     * Gets prepared empty data vector
     * @return Empty data vector
     */
    public static Object[][] getEmptyDataVector()
    {
        Station[] stations = Stations.GetInstance().GetAllStations();
        Object[][] reti = new Object[stations.length][];
        for (int i = 0; i < stations.length; i++)
        {
            reti[i] = new Object[3];
            reti[i][0] = stations[i].getAbbrevation();
            reti[i][1] = stations[i].getName();
            reti[i][2] = 0;
        }
        return reti;
    }
}
