package cz.jadjj.jticket.ui.window.windows;

import cz.jadjj.jticket.data.Tariff;
import cz.jadjj.jticket.data.TariffType;
import cz.jadjj.jticket.data.Tariffs;
import cz.jadjj.jticket.ui.window.WindowUI;
import cz.jadjj.jticket.ui.window.tables.WindowUIReadOnlyTableModel;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIButtonType;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialog;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialogType;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
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
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/**
 * Class representing window with tariffs
 * @author jadjj
 */
public class WindowUITariffsWindow extends WindowUIWindow
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
     * Menu item which removes selected tariffs
     */
    private JMenuItem removeItem;
    
    /**
     * Menu item which displays new tariff wizard
     */
    private JMenuItem newTariffItem;
    
    /**
     * Menu item which represents saving changes in stations
     */
    private JMenuItem saveChangesItem;
    
    /**
     * Flag, whether changes has been saved
     */
    private boolean changesSaved = true;
    
    /**
     * List of tariffs to be added
     */
    private List<Tariff> toAdd;
    
    /**
     * List of tariffs to be removed
     */
    private List<Tariff> toRemove;
    
    /**
     * Creates new window with stations
     */
    public WindowUITariffsWindow()
    {
        super("jTicket - Tarify", "dataitem.png");
        super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        super.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent ev)
            {
                if (changesSaved == false)
                {
                    WindowUIDialog exitDialog = new WindowUIDialog("Ukončit práci s tarify", "Opravdu chcete ukončit práci s tarify?" + System.lineSeparator() + "Veškeré neuložené změny budou zahozeny.", WindowUIDialogType.WARNING, WindowUIButtonType.YES, WindowUIButtonType.NO);
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
        this.toAdd = new ArrayList<>();
        this.toRemove = new ArrayList<>();
    }
        
    /**
     * Prepares data to be shown in JTable
     * @return Array of rows with distances data
     */
    private Object[][] prepareData()
    {        
        Tariff[] tariffs = Tariffs.GetInstance().GetAllTariffs();
        Object[][] reti = new Object[tariffs.length][];
        for (int i = 0; i < tariffs.length; i++)
        {
            reti[i] = new Object[3];
            reti[i][0] = tariffs[i].getAbbr();
            reti[i][1] = tariffs[i].getName();
            reti[i][2] = 
                    (tariffs[i].getType() == TariffType.ZONE 
                        ? "ZÓNOVÝ"
                        : "VZDÁLENOSTNÍ");
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
        String[] cols = new String[]{"ZKRATKA", "NÁZEV", "DRUH"};        
        WindowUIReadOnlyTableModel tableModel = new WindowUIReadOnlyTableModel();
        tableModel.setDataVector(this.prepareData(), cols);
        this.dataView.setModel(tableModel);
        this.dataView.setAutoCreateRowSorter(true);
        this.dataView.getTableHeader().setReorderingAllowed(true);
        this.dataView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.scrollPane = new JScrollPane(this.dataView);
        this.getContentPane().add(this.scrollPane);
        this.setState(String.format("Načteno %d tarifů", this.prepareData().length));
        this.dataView.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            if (dataView.getSelectedRowCount() > 0)
            {
                int row = dataView.convertRowIndexToModel(dataView.getSelectedRow());
                setState((String) dataView.getModel().getValueAt(row, 1));
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
        this.detailItem.setText("Detail tarifu");
        this.detailItem.addActionListener((e) -> {
            if (this.dataView.getSelectedRowCount() > 0 && this.getSelectedTariff() != null)
            {
                WindowUITariffDetailWindow window = new WindowUITariffDetailWindow(this.getSelectedTariff());
                window.setVisible(true);
            }
            else
            {
                WindowUIDialog dialog = new WindowUIDialog(
                        "CHYBA: Detail tarifu",
                        "Nelze zobrazit detail tarifu!" + System.lineSeparator() + "Nebyl vybrán žádný tarif.",
                        WindowUIDialogType.ERROR,
                        WindowUIButtonType.CANCEL
                );
                dialog.showDialog();
            }
        });
        
        this.dataMenu.add(this.detailItem);
        
        // New tariff item
        this.newTariffItem = new JMenuItem();
        this.newTariffItem.setIcon(new ImageIcon(WindowUI.UI_PATH + "/newitem.png"));
        this.newTariffItem.setText("Vytvořit nový tarif");
        this.newTariffItem.addActionListener((e) -> {
            WindowUINewTariffWindow newTariffWindow = new WindowUINewTariffWindow();
            newTariffWindow.setVisible(true);
            if (newTariffWindow.getOKClicked() == true)
            {
                Tariff t = newTariffWindow.getTariff();
                this.toAdd.add(t);
                WindowUIReadOnlyTableModel model = (WindowUIReadOnlyTableModel) this.dataView.getModel();
                model.addRow(new Object[]{t.getAbbr(), t.getName(), (t.getType() == TariffType.ZONE ? "ZÓNOVÝ": "VZDÁLENOSTNÍ")});
            }
        });
        this.dataMenu.add(this.newTariffItem);
        
        // Remove item
        this.removeItem = new JMenuItem();
        this.removeItem.setIcon(new ImageIcon(WindowUI.UI_PATH + "/deleteitem.png"));
        this.removeItem.setText("Odebrat vybraný tarif");
        this.removeItem.addActionListener((e) -> {
            if (this.dataView.getSelectedRowCount() > 0)
            {
                this.changesSaved = false;
                Tariff selected = this.getSelectedTariff();
                if (selected != null)
                {
                    if (this.toAdd.contains(selected))
                    {
                        this.toAdd.remove(selected);
                    }
                    else
                    {
                        this.toRemove.add(selected);
                    }
                }
                WindowUIReadOnlyTableModel model = (WindowUIReadOnlyTableModel) this.dataView.getModel();
                model.removeRow(this.dataView.convertRowIndexToModel(this.dataView.getSelectedRow()));
            }
            else
            {
                WindowUIDialog dialog = new WindowUIDialog(
                        "Odebrat vybraný tarif",
                        "Nelze odebrat vybraný tarif." + System.lineSeparator() + "Žádný tarif nebyl vybrán.",
                        WindowUIDialogType.INFO,
                        WindowUIButtonType.CANCEL
                );
                dialog.showDialog();
            }
        });
        this.dataMenu.add(this.removeItem);
        
        // Save changes item
        this.saveChangesItem = new JMenuItem();
        this.saveChangesItem.setIcon(new ImageIcon(WindowUI.UI_PATH + "/saveitem.png"));
        this.saveChangesItem.setText("Uložit změny");
        this.saveChangesItem.addActionListener((e) -> {
            for (Tariff t: this.toAdd)
            {
                Tariffs.GetInstance().AddTariff(t);
            }
            for (Tariff t: this.toRemove)
            {
                Tariffs.GetInstance().RemoveTariff(t);
            }
            this.setState("Změny úspěšně uloženy");
            changesSaved = true;
        });
        this.dataMenu.add(this.saveChangesItem);
        
        super.setSize(800, 600);
    }
    
    /**
     * Gets tariff selected in table
     * @return Selected tariff or <code>NULL</code> if no tariff is selected
     */
    private Tariff getSelectedTariff()
    {
        Tariff reti = null;
        if (this.dataView.getSelectedRowCount() == 1)
        {
            int row = this.dataView.convertRowIndexToModel(this.dataView.getSelectedRow());
            String abbr = this.dataView.getValueAt(row, 0).toString();
            // Check, if tariff is in items to be deleted
            reti = this.tariffIsInList(this.toRemove, abbr);
            if (reti == null)
            {
                // Check items to be added
                reti = this.tariffIsInList(this.toAdd, abbr);
                if (reti == null)
                {
                    // Check data
                    reti = Tariffs.GetInstance().GetTariff(abbr);
                }
            }
        }
        return reti;
    }
    
    /**
     * Checks, whether tariff is in list
     * @param l Iterable container which will be checked
     * @param abbr Abbreavation which will be searched for
     * @return <code>Tariff</code> if list is in iterable container,
     *          <code>NULL</code> otherwise
     */
    private Tariff tariffIsInList(Iterable<Tariff> l, String abbr)
    {
        Tariff reti = null;
        for (Tariff t: l)
        {
            if (t.getAbbr().trim().toUpperCase().equals(
                    abbr.trim().toUpperCase()
            ))
            {
                reti = t;
                break;
            }
        }
        return reti;
    }
}
