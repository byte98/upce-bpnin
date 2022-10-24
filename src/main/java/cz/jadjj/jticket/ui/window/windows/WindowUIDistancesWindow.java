package cz.jadjj.jticket.ui.window.windows;

import cz.jadjj.jticket.data.Distances;
import cz.jadjj.jticket.data.Station;
import cz.jadjj.jticket.data.Stations;
import cz.jadjj.jticket.ui.window.WindowUI;
import cz.jadjj.jticket.ui.window.tables.WindowUIDistancesTableModel;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIButtonType;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialog;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialogType;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Class representing window with distances
 * @author jadjj
 */
public class WindowUIDistancesWindow extends WindowUIWindow
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
     * Menu item which displays detail of entered data
     */
    private JMenuItem detailItem;
    
    /**
     * Menu item which represents saving changes in stations
     */
    private JMenuItem saveChangesItem;
    
    /**
     * Flag, whether changes has been saved
     */
    private boolean changesSaved = true;
    
    /**
     * Creates new window with stations
     */
    public WindowUIDistancesWindow()
    {
        super("jTicket - Vzdálenosti", "dataitem.png");
        super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        super.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent ev)
            {
                if (changesSaved == false)
                {
                    WindowUIDialog exitDialog = new WindowUIDialog("Ukončit práci se vzdálenostmi", "Opravdu chcete ukončit práci s tabulkou vzdáleností?" + System.lineSeparator() + "Veškeré neuložené změny budou zahozeny.", WindowUIDialogType.WARNING, WindowUIButtonType.YES, WindowUIButtonType.NO);
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
        this.prepareTable();
    }
        
    /**
     * Prepares data to be shown in JTable
     * @return Array of rows with distances data
     */
    private Object[][] prepareData()
    {
        Station[] stations = Stations.GetInstance().GetAllStations();
        Object[][] reti = new Object[stations.length][];
        for (int i = 0; i < stations.length; i++)
        {
            reti[i] = new Object[stations.length + 1];
            reti[i][0] = stations[i].getAbbrevation();
            for (int j = 0; j < stations.length; j++)
            {
                reti[i][j + 1] = Distances.GetInstance().GetDistance(
                        stations[i],
                        stations[j]
                );
            }
        }
        return reti;
    }
    
    /**
     * Prepares table with distances
     */
    private void prepareTable()
    {
        if (this.dataView == null)
              this.dataView = new JTable();
        if (this.scrollPane != null)
            this.getContentPane().remove(this.scrollPane);
        Station[] stations = Stations.GetInstance().GetAllStations();
        String[] cols = new String[stations.length + 1];
        cols[0] = " ";
        for(int i = 0; i < stations.length; i++)
        {
            cols[i + 1] = stations[i].getAbbrevation();
        }
        WindowUIDistancesTableModel tableModel = new WindowUIDistancesTableModel();
        tableModel.setDataVector(this.prepareData(), cols);
        this.dataView.setModel(tableModel);
        this.dataView.setAutoCreateRowSorter(true);
        this.dataView.getTableHeader().setReorderingAllowed(false);
        this.dataView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.scrollPane = new JScrollPane(this.dataView);
        this.getContentPane().add(this.scrollPane);
        this.setState(String.format("Načtena tabulka pro %d stanic", stations.length));
        this.dataView.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (dataView.getSelectedRowCount() > 0)
            {
                Station from = Stations.GetInstance().GetStation(this.dataView.getModel().getColumnName(this.dataView.getSelectedColumn()));
                Station to = Stations.GetInstance().GetStation((String) this.dataView.getModel().getValueAt(this.dataView.getSelectedRow(), 0));
                if (from != null && to != null)
                {
                    int distance = Distances.GetInstance().GetDistance(from, to);
                    this.setState(String.format("Vzdálenost z %s do %s: %d km", from.getName(), to.getName(), distance));
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
                WindowUIDialog err = new WindowUIDialog(
                            "Chyba zadávání dat",
                            "Zadaná vzdálenost mezi stanicemi je neplatná." + System.lineSeparator() +
                            "Vzdálenost musí být celé nezáporné číslo.",
                            WindowUIDialogType.ERROR,
                            WindowUIButtonType.OK                            
                    );
                changesSaved = false;
                int distance = -1;
                try
                {
                    distance = Integer.parseInt(dataView.getValueAt(dataView.getSelectedRow(), dataView.getSelectedColumn()).toString());
                }
                catch (Exception ex)
                {                    
                }
                if ((distance >= 0) == false)
                {
                    err.showDialog();
                    dataView.setValueAt(0, dataView.getSelectedRow(), dataView.getSelectedColumn());
                }
                else
                {
                    int valueTransposed = -1;
                    try
                    {
                        valueTransposed = Integer.parseInt(dataView.getValueAt(dataView.getSelectedColumn() - 1, dataView.getSelectedRow() + 1).toString());
                    }
                    catch (Exception ex)
                    {}
                    if (valueTransposed != distance || valueTransposed < 0)
                    {
                        dataView.setValueAt(distance, dataView.getSelectedColumn() - 1, dataView.getSelectedRow() + 1);
                    }
                    
                }
            }
        });
        
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
        
        // Detail item
        this.detailItem = new JMenuItem();
        this.detailItem.setIcon(new ImageIcon(WindowUI.UI_PATH + "/detail-s.png"));
        this.detailItem.setText("Detail");
        this.detailItem.addActionListener((e) -> {
            if (this.dataView.getSelectedRowCount() > 0)
            {
                Station from = Stations.GetInstance().GetStation(this.dataView.getModel().getColumnName(this.dataView.getSelectedColumn()));
                Station to = Stations.GetInstance().GetStation((String) this.dataView.getModel().getValueAt(this.dataView.getSelectedRow(), 0));
                if (from != null && to != null)
                {
                    int distance = -1;
                    try
                    {
                        distance = Integer.parseInt((String)this.dataView.getModel().getValueAt(dataView.getSelectedRow(), dataView.getSelectedColumn()).toString());
                    }
                    catch(Exception ex)
                    {
                        WindowUIDialog err = new WindowUIDialog(
                                "Zobrazení detailu vzdálenosti",
                                "Nelze zobrazit detail vzdálenosti mezi stanicemi." + System.lineSeparator() +
                                "Hodnota vzdálenosti v tabulce je neplatná." + System.lineSeparator() +
                                "Pro odstranění této chyby restartujte program bez ukládání dat.",
                                WindowUIDialogType.ERROR,
                                WindowUIButtonType.OK
                        );
                        err.showDialog();
                    }
                    if (distance >= 0)
                    {
                        WindowUIDistanceDetailWindow detail = new WindowUIDistanceDetailWindow(from, to, distance);
                        detail.setVisible(true);
                        detail.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e){
                                if(detail.getOKClicked() == true)
                                {
                                    dataView.getModel().setValueAt(detail.getDistance(), dataView.getSelectedRow(), dataView.getSelectedColumn());
                                }
                            }
                        });
                    }
                }
                else
                {
                    WindowUIDialog dialog = new WindowUIDialog(
                        "CHYBA: Detail vzdálenosti",
                        "Nelze zobrazit detail vzdálenosti!" + System.lineSeparator() + "Vybrány neznámé stanice.",
                        WindowUIDialogType.ERROR,
                        WindowUIButtonType.CANCEL
                );
                dialog.showDialog();
                }
            }
            else
            {
                WindowUIDialog dialog = new WindowUIDialog(
                        "CHYBA: Detail vzdálenosti",
                        "Nelze zobrazit detail vzdálenosti!" + System.lineSeparator() + "Nebyl vybrán žádný údaj.",
                        WindowUIDialogType.ERROR,
                        WindowUIButtonType.CANCEL
                );
                dialog.showDialog();
            }
        });
        
        this.dataMenu.add(this.detailItem);
        
        // Save changes item
        this.saveChangesItem = new JMenuItem();
        this.saveChangesItem.setIcon(new ImageIcon(WindowUI.UI_PATH + "/saveitem.png"));
        this.saveChangesItem.setText("Uložit změny");
        this.saveChangesItem.addActionListener((e) -> {
            Map<String, Map<String, Integer>> data = this.getTableData();
            for (String from: data.keySet())
            {
                for (String to: data.get(from).keySet())
                {
                    Distances.GetInstance().SetDistance(
                            Stations.GetInstance().GetStation(from),
                            Stations.GetInstance().GetStation(to),
                            data.get(from).get(to));
                }
            }
            this.setState("Změny úspěšně uloženy");
            changesSaved = true;
        });
        this.dataMenu.add(this.saveChangesItem);
        
        super.setSize(800, 600);
        
    }
    
    /**
     * Gets data stored in table
     * @return Map with stations abbreavations and distances
     */
    private Map<String, Map<String, Integer>> getTableData()
    {
        Map<String, Map<String, Integer>> reti = new HashMap<>();
        for (int r = 0; r < this.dataView.getRowCount(); r++)
        {
            reti.put(this.dataView.getModel().getValueAt(r, 0).toString(), new HashMap<>());
            Map<String, Integer> line = new HashMap<>();
            for (int c = 1; c < this.dataView.getColumnCount(); c++)
            {
                int distance = -1;
                try
                {
                    distance = Integer.parseInt(this.dataView.getModel().getValueAt(r, c).toString());
                }
                catch (Exception ex){
                    distance = 0;
                    System.out.println(String.format("[!] ERROR: from %s to %s = %s",
                            this.dataView.getModel().getValueAt(r, 0),
                            this.dataView.getModel().getColumnName(c),
                            this.dataView.getModel().getValueAt(r, c)));
                }
                System.out.println(String.format("[r: %d; c: %d] -> %d", r, c, distance));
                line.put(this.dataView.getColumnName(c), distance);
            }
            reti.put(this.dataView.getModel().getValueAt(r, 0).toString(), line);
        }
        return reti;
    }
}
