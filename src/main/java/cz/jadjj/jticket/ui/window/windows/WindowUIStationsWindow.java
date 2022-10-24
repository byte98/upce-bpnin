package cz.jadjj.jticket.ui.window.windows;

import cz.jadjj.jticket.data.Station;
import cz.jadjj.jticket.data.Stations;
import cz.jadjj.jticket.ui.window.WindowUI;
import cz.jadjj.jticket.ui.window.tables.WindowUIStationsTableModel;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIButtonType;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialog;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialogType;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Class representing window with stations
 * @author jadjj
 */
public class WindowUIStationsWindow extends WindowUIWindow
{
    /**
     * Viewer of data
     */
    private JTable dataView;
    
    /**
     * Container for make table scrollable
     */
    private JScrollPane scrollPane;
    
    /**
     * Item which opens data directory in file explorer
     */
    private JMenuItem openExplorer;
        
    /**
     * Menu with data options in top menu
     */
    private JMenu dataMenu;
    
    /**
     * Menu item which represents adding station to system
     */
    private JMenuItem addStationItem;
    
    /**
     * Menu item which represents deleting station from system
     */
    private JMenuItem deleteStationItem;
 
    /**
     * Menu item which represents saving changes in stations
     */
    private JMenuItem saveChangesItem;
    
    /**
     * List of identifiers of stations which should be deleted
     */
    private List<Integer> toDelete;
    
    /**
     * Flag, whether changes has been saved
     */
    private boolean changesSaved = true;
    
    /**
     * Creates new window with stations
     */
    public WindowUIStationsWindow()
    {
        super("jTicket - Stanice", "dataitem.png");
        super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        super.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent ev)
            {
                if (changesSaved == false)
                {
                    WindowUIDialog exitDialog = new WindowUIDialog("Ukončit práci se stanicemi", "Opravdu chcete ukončit práci se stanicemi?" + System.lineSeparator() + "Veškeré neuložené změny budou zahozeny.", WindowUIDialogType.WARNING, WindowUIButtonType.YES, WindowUIButtonType.NO);
                    exitDialog.showDialog();
                    if (exitDialog.getResult() == WindowUIButtonType.YES)
                    {
                        dispose();
                    }
                }
                else
                {
                    dispose();
                }
            }
        });
        this.toDelete = new ArrayList<>();
    }
    
    /**
     * Sets data which will be displayed in table
     * @param stations Array with stations which will be displayed
     */
    public void setData(Station[] stations)
    {
        if (this.dataView == null)
              this.dataView = new JTable();
        if (this.scrollPane != null)
            this.getContentPane().remove(this.scrollPane);
        String[] cols = new String[3];
        cols[0] = "ZKRATKA";
        cols[1] = "NÁZEV";
        cols[2] = "ID";
        WindowUIStationsTableModel tableModel = new WindowUIStationsTableModel();
        tableModel.setDataVector(this.prepareData(stations), cols);
        this.dataView.setModel(tableModel);
        this.dataView.setAutoCreateRowSorter(true);
        this.scrollPane = new JScrollPane(this.dataView);
        this.getContentPane().add(this.scrollPane);
        this.setPreferredSize(new Dimension(800, 600));
        this.setState(String.format("Načteno %d stanic", stations.length));
        this.dataView.removeColumn(this.dataView.getColumn("ID"));
        this.dataView.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (dataView.getSelectedRowCount() > 0)
            {
                int row = dataView.convertRowIndexToModel(dataView.getSelectedRow());
                if (dataView.getSelectedRowCount() == 1)
                {
                    setState("Stanice " + (String) dataView.getModel().getValueAt(row, 1));
                }
                else
                {
                    setState(String.format("Vybráno %d stanic", dataView.getSelectedRowCount()));
                }
            }
            else        
            {
                setState("Připraven");
            }
        });
        this.dataView.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                changesSaved = false;
            }
        });
    }
        
    /**
     * Prepares data to be shown in JTable
     * @param stations Stations which will be displayed in table
     * @return Array of rows with stations data
     */
    private Object[][] prepareData(Station[] stations)
    {
        Object[][] reti = new Object[stations.length][];
        for (int i = 0; i < stations.length; i++)
        {
            reti[i] = new Object[3];
            reti[i][0] = stations[i].getAbbrevation();
            reti[i][1] = stations[i].getName();
            reti[i][2] = stations[i].getIdentifier();
        }
        return reti;
    }
    
    @Override
    protected void prepareTopMenu()
    {
        super.prepareTopMenu();
        
        // Open explorer item
        this.openExplorer = new JMenuItem();
        this.openExplorer.setText("Otevřít v prohlížeči souborů");
        this.openExplorer.setIcon(new ImageIcon(WindowUI.UI_PATH + "/explorer.png"));
        this.openExplorer.addActionListener((ActionEvent e) -> {
            try {        
                Desktop.getDesktop().open(new File("./resources/data/"));
            } catch (IOException ex) {
                WindowUI.handleException(ex);
            }
        });
        this.fileMenu.add(this.openExplorer, 0);
        
        // Data menu
        this.dataMenu = new JMenu();
        this.dataMenu.setIcon(new ImageIcon(WindowUI.UI_PATH + "/data.png"));
        this.dataMenu.setText("Data");
        this.topMenu.add(this.dataMenu);
        
        // Add station item
        this.addStationItem = new JMenuItem();
        this.addStationItem.setIcon(new ImageIcon(WindowUI.UI_PATH + "/newitem.png"));
        this.addStationItem.setText("Přidat stanici");
        this.addStationItem.addActionListener((ActionEvent e) -> {
            WindowUINewStationWindow dialog = new WindowUINewStationWindow();
            dialog.setVisible(true);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e){
                    if (dialog.getStationAbbr().trim().length() * dialog.getStationName().trim().length() > 0)
                    {
                        changesSaved = false;
                        WindowUIStationsTableModel model = (WindowUIStationsTableModel) dataView.getModel();
                        model.addRow(new Object[]{dialog.getStationAbbr(), dialog.getStationName(), null});
                    }
                }
            });
        });
        this.dataMenu.add(this.addStationItem);
        
        // Delete station item
        this.deleteStationItem = new JMenuItem();
        this.deleteStationItem.setIcon(new ImageIcon(WindowUI.UI_PATH + "/deleteitem.png"));
        this.deleteStationItem.setText("Smazat vybrané stanice");
        this.dataMenu.add(this.deleteStationItem);
        this.deleteStationItem.addActionListener((ActionListener) (ActionEvent e) -> {
            WindowUIStationsTableModel model = (WindowUIStationsTableModel) dataView.getModel();
            if (dataView.getSelectedRows().length > 1)
            {
                changesSaved = false;
                int row;
                for (int i = 0; i < dataView.getSelectedRows().length; i++)
                {
                    row = dataView.convertRowIndexToModel(dataView.getSelectedRow());
                    this.toDelete.add((Integer)dataView.getModel().getValueAt(row, 2));
                    model.removeRow(dataView.convertRowIndexToModel(dataView.getSelectedRow()));
                }
                row = dataView.convertRowIndexToModel(dataView.getSelectedRow());
                this.toDelete.add((Integer)dataView.getModel().getValueAt(row, 2));
                model.removeRow(dataView.convertRowIndexToModel(dataView.getSelectedRow()));
            }
            else
            {
                if (dataView.getSelectedRows().length > 0)
                {
                    changesSaved = false;
                    int row = dataView.convertRowIndexToModel(dataView.getSelectedRow());
                    this.toDelete.add((Integer)dataView.getModel().getValueAt(row, 2));
                    model.removeRow(dataView.convertRowIndexToModel(dataView.getSelectedRow()));
                }
                else
                {
                    WindowUIDialog dialog = new WindowUIDialog("Smazání stanic", "Nelze smazat vybrané stanice." + System.lineSeparator() + "Nejsou vybrané žádné stanice.", WindowUIDialogType.INFO, WindowUIButtonType.OK);
                    dialog.showDialog();
                }
            }
        });
        
        // Save changes item
        this.saveChangesItem = new JMenuItem();
        this.saveChangesItem.setIcon(new ImageIcon(WindowUI.UI_PATH + "/saveitem.png"));
        this.saveChangesItem.setText("Uložit změny");
        this.saveChangesItem.addActionListener((e) -> {
            // First, delete stations
            for(Integer id: this.toDelete)
            {
                if (id != null)
                {
                    Stations.GetInstance().DeleteStation(Stations.GetInstance().GetStation(id));
                }
            }
            // Second, parse data from table
            for(int i = 0; i < this.dataView.getModel().getRowCount(); i++)
            {
                if (this.dataView.getModel().getValueAt(i, 2) != null)
                {
                    Station s = Stations.GetInstance().GetStation((Integer)this.dataView.getModel().getValueAt(i, 2));
                    Stations.GetInstance().EditStation(s, (String)this.dataView.getModel().getValueAt(i, 1), (String)this.dataView.getModel().getValueAt(i, 0));
                }
                else
                {
                    Stations.GetInstance().AddStation(new Station((String)this.dataView.getModel().getValueAt(i, 0), (String)this.dataView.getModel().getValueAt(i, 1)));
                }
                
            }
            this.setState("Změny úspěšně uloženy");
            changesSaved = true;
        });
        this.dataMenu.add(this.saveChangesItem);
        
        super.setSize(800, 600);
        
    }
}
