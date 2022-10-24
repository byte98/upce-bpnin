package cz.jadjj.jticket.ui.window.windows;

import cz.jadjj.jticket.data.Configuration;
import cz.jadjj.jticket.data.DistanceTariff;
import cz.jadjj.jticket.data.Stations;
import cz.jadjj.jticket.data.Tariff;
import cz.jadjj.jticket.data.TariffType;
import cz.jadjj.jticket.data.ZoneTariff;
import cz.jadjj.jticket.jTicket;
import cz.jadjj.jticket.ui.window.WindowUI;
import cz.jadjj.jticket.ui.window.WindowUITheme;
import cz.jadjj.jticket.ui.window.tables.WindowUIPricesTableModel;
import cz.jadjj.jticket.ui.window.tables.WindowUIZonesTableModel;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIButtonType;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialog;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialogType;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 * Class representing new tariff window
 * @author Jiri Skoda <skodaji1@uhk.cz.cz>
 */
public class WindowUINewTariffWindow extends JDialog
{
    /**
     * Panel with first stage of wizard
     */
    private JPanel stage1;
    
    /**
     * Panel with second stage of wizard
     */
    private JPanel stage2;
    
    /**
     * Text field containing tariffs name
     */
    private JTextField tariffName;
    
    /**
     * Text field containing tariffs abbreavation
     */
    private JTextField tariffAbbr;
    
    /**
     * Type of tariff
     */
    private TariffType tariffType = TariffType.ZONE;
    
    /**
     * Table holding price list
     */
    private JTable priceTable;
    
    /**
     * Table holding zones assignments
     */
    private JTable zoneTable;
    
    /**
     * Flag, whether wizard has been finished using 'OK' button
     */
    private boolean okClicked = false;
    
    /**
     * Content of window
     */
    private JPanel content;
    
    /**
     * Creates new window for creating new tariff
     */
    public WindowUINewTariffWindow()
    {
        super.setTitle("Průvodce vytvořením nového tarifu");
        super.setIconImage(new ImageIcon(WindowUI.UI_PATH + "/newitem.png").getImage());
        super.setModal(true);
        this.initializeComponents();
        super.setSize(new Dimension(650, 450));
        super.getInsets().set(20, 20, 20, 20);
        super.setResizable(false);
        super.setLocation(
                ((Toolkit.getDefaultToolkit().getScreenSize().width - super.getWidth()) / 2),
                ((Toolkit.getDefaultToolkit().getScreenSize().height - super.getHeight()) / 2)

        );
        super.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent ev)
            {
                WindowUIDialog dialog = new WindowUIDialog(
                    "Ukončit průvodce",
                    "Opravdu chcete ukončit průvodce vytvořením nového tarifu?",
                    WindowUIDialogType.QUESTION,
                    WindowUIButtonType.YES,
                    WindowUIButtonType.NO
                );
                dialog.showDialog();
                if (dialog.getResult() == WindowUIButtonType.YES)
                {
                    okClicked = false;
                    dispose();
                }
            }
        });
    }
    
    /**
     * Initializes components of wizard
     */
    private void initializeComponents()
    {
        final int PADDING = 10;
        //<editor-fold defaultstate="collapsed" desc="Content of dialog">
        this.content = new JPanel(new CardLayout());
        super.getContentPane().add(this.content);
        //<editor-fold defaultstate="collapsed" desc="First stage">
        this.stage1 = new JPanel(new BorderLayout());
        this.stage1.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        //<editor-fold defaultstate="collapsed" desc="Image of stage">
        JLabel stage1Img = new JLabel();
        stage1Img.setIcon(new ImageIcon(WindowUI.UI_PATH + "/tariffwiz-1.png"));
        stage1Img.setSize(100, 300);
        stage1Img.setBackground(WindowUITheme.getTheme(Configuration.getInstance(jTicket.CONFIG_FILE).uiTheme).getTariffWizardColour());
        stage1Img.setOpaque(true);
        stage1Img.setBorder(BorderFactory.createLoweredBevelBorder());
        this.stage1.add(stage1Img, BorderLayout.WEST);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Content of stage">
        JPanel stage1Content = new JPanel();
        stage1Content.setLayout(new BoxLayout(stage1Content, BoxLayout.Y_AXIS));
        stage1Content.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        stage1Content.add(new JLabel("Vítejte v průvodci vytvořením nového tarifu."));
        stage1Content.add(new JLabel("Tento průvodce Vám umožní vytvořit krok za krokem nový tarif."));
        stage1Content.add(new JLabel(System.lineSeparator()));
        JLabel basicInfoLabel = new JLabel("1) Základní informace o tarifu");
        basicInfoLabel.setFont(basicInfoLabel.getFont().deriveFont(Font.BOLD));
        stage1Content.add(basicInfoLabel);
        stage1Content.add(new JLabel("V tomto kroku bude zapotřebí zadat základní informace týkající se nového tarifu."));
        stage1Content.add(new JLabel(System.lineSeparator()));
        stage1Content.add(new JLabel("Zkratka tarifu"));
        this.tariffAbbr = new JTextField();
        stage1Content.add(this.tariffAbbr);
        stage1Content.add(new JLabel(System.lineSeparator()));
        stage1Content.add(new JLabel("Název tarifu"));
        this.tariffName = new JTextField();
        stage1Content.add(this.tariffName);
        stage1Content.add(new JLabel(System.lineSeparator()));
        stage1Content.add(new JLabel("Druh tarifu"));
        ButtonGroup typeGroup = new ButtonGroup();
        JRadioButton zoneRadio = new JRadioButton("ZÓNOVÝ", true);
        zoneRadio.addActionListener((e) -> {
            if (zoneRadio.isSelected())
            {
                this.tariffType = TariffType.ZONE;
            }
        });
        typeGroup.add(zoneRadio);
        stage1Content.add(zoneRadio);
        stage1Content.add(new JLabel("Finální cena jízdenky je určena počtem zón mezi výchozí a cílovou stanicí."));
        stage1Content.add(new JLabel(System.lineSeparator()));
        JRadioButton distanceRadio = new JRadioButton("VZDÁLENOSTNÍ");
        distanceRadio.addActionListener((e) -> {
            if (distanceRadio.isSelected())
            {
                this.tariffType = TariffType.DISTANCE;
            }
        });
        typeGroup.add(distanceRadio);
        stage1Content.add(distanceRadio);
        stage1Content.add(new JLabel("Celková cena jízdenky je vypočtena ze vzdálenosti mezi výchozí a cílovou stanící."));
        this.stage1.add(stage1Content, BorderLayout.CENTER);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Buttons of stage">
        JPanel stage1Btns = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        //<editor-fold defaultstate="collapsed" desc="Next button">
        JButton nextBtn = new JButton("Další");
        nextBtn.addActionListener((e) -> {
            this.initializeStage2();
            CardLayout cl = (CardLayout)(this.content.getLayout());
            cl.show(this.content, "Dokončit");
        });
        stage1Btns.add(nextBtn);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Cancel button">
        JButton cancelBtn = new JButton("Zrušit");
        cancelBtn.addActionListener((e) -> {
            WindowUIDialog dialog = new WindowUIDialog(
                    "Ukončit průvodce",
                    "Opravdu chcete ukončit průvodce vytvořením nového tarifu?",
                    WindowUIDialogType.QUESTION,
                    WindowUIButtonType.YES,
                    WindowUIButtonType.NO
            );
            dialog.showDialog();
            if (dialog.getResult() == WindowUIButtonType.YES)
            {
                this.okClicked = false;
                this.dispose();
            }
        });
        stage1Btns.add(cancelBtn);
        //</editor-fold>
        this.stage1.add(stage1Btns, BorderLayout.SOUTH);
        //</editor-fold>
        this.content.add(this.stage1, "Základní informace");
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="Second stage">
        this.stage2 = new JPanel(new BorderLayout());
        this.stage2.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        this.content.add(this.stage2, "Dokončit");
        this.initializeStage2();
        //</editor-fold>
        //</editor-fold>
    }
    
    /**
     * Initializes components for second stage
     */
    private void initializeStage2()
    {
        final int PADDING = 10;
        this.stage2.removeAll();
        
        //<editor-fold defaultstate="collapsed" desc="Image of stage">
        JLabel stage2Img = new JLabel();
        stage2Img.setIcon(new ImageIcon(WindowUI.UI_PATH + "/tariffwiz-2.png"));
        stage2Img.setSize(100, 300);
        stage2Img.setBackground(WindowUITheme.getTheme(Configuration.getInstance(jTicket.CONFIG_FILE).uiTheme).getTariffWizardColour());
        stage2Img.setOpaque(true);
        stage2Img.setBorder(BorderFactory.createLoweredBevelBorder());
        this.stage2.add(stage2Img, BorderLayout.WEST);
        //</editor-fold>
        //<editor-fold defaultststate="collapsed" desc="Content of stage">
        JPanel stage2Content = new JPanel();
        stage2Content.setLayout(new BoxLayout(stage2Content, BoxLayout.Y_AXIS));
        stage2Content.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        String[] cols = new String[]{
            (this.tariffType == TariffType.ZONE ? "POČET ZÓN" : "VZDÁLENOST"),
            "CENA (v Kč)"
        };
        //<editor-fold defaultstate="collapsed" desc="Table with zones">
        if (this.tariffType == TariffType.ZONE)
        {
            JLabel zonesLabel = new JLabel("2) Přiřazení zón");
            zonesLabel.setFont(zonesLabel.getFont().deriveFont(Font.BOLD));
            stage2Content.add(zonesLabel);
            stage2Content.add(new JLabel("V následujícím kroku je nezbytné přiřadit každou stanici do zóny."));
            this.zoneTable = new JTable();
            this.zoneTable.setModel(new WindowUIZonesTableModel(
                    WindowUIZonesTableModel.getEmptyDataVector(),
                    new String[]{"ZKRATKA", "STANICE", "ZÓNA"}
            ));
            this.zoneTable.setAutoCreateRowSorter(false);
            this.zoneTable.getTableHeader().setReorderingAllowed(false);
            this.zoneTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            this.zoneTable.getModel().addTableModelListener((e) -> {
                this.priceTable.setModel(new WindowUIPricesTableModel(
                (this.tariffType == TariffType.DISTANCE
                        ? WindowUIPricesTableModel.getEmptyDataVector()
                        : WindowUIPricesTableModel.getEmptyDataVector(this.getMaximalZone())
                        ),
                cols));
            });
            JScrollPane zoneScroll = new JScrollPane(this.zoneTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            stage2Content.add(zoneScroll);
        }
        //</editor-fold>
        JLabel priceLabel = new JLabel((this.tariffType == TariffType.DISTANCE ? "2" : "3") + ") Ceník tarifu");
        priceLabel.setFont(priceLabel.getFont().deriveFont(Font.BOLD));
        stage2Content.add(priceLabel);
        stage2Content.add(new JLabel("Ve " + (this.tariffType == TariffType.DISTANCE ? "druhém" : "třetím") + " kroku je nutné nastavit ceník tarifu."));
        stage2Content.add(new JLabel("Tento ceník bude použit při výpočtu ceny jízdenky."));
        stage2Content.add(new JLabel(System.lineSeparator()));
        stage2Content.add(new JLabel("Ceník tarifu '" + this.tariffName.getText() + "'"));
        //<editor-fold defaultstate="collapsed" desc="Table with pricelist">
        this.priceTable = new JTable();
        
        this.priceTable.setModel(new WindowUIPricesTableModel(
                (this.tariffType == TariffType.DISTANCE
                        ? WindowUIPricesTableModel.getEmptyDataVector()
                        : WindowUIPricesTableModel.getEmptyDataVector(this.getMaximalZone())
                        ),
                cols));
        this.priceTable.setAutoCreateRowSorter(false);
        this.priceTable.getTableHeader().setReorderingAllowed(false);
        this.priceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(this.priceTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        stage2Content.add(tableScroll);
        //</editor-fold>
        this.stage2.add(stage2Content);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Buttons of stage">
        JPanel stage2Btns = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        //<editor-fold defaultstate="collapsed" desc="Previous button">
        JButton prevBtn = new JButton("Předchozí");
        prevBtn.addActionListener((e) -> {
            CardLayout cl = (CardLayout)(this.content.getLayout());
            cl.show(this.content, "Základní informace");
        });
        stage2Btns.add(prevBtn);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Finish button">
        JButton finishBtn = new JButton("Dokončit");
        finishBtn.addActionListener((e) -> {
            this.okClicked = true;
            this.dispose();
        });
        stage2Btns.add(finishBtn);
        //</editor-fold>        
        //<editor-fold defaultstate="collapsed" desc="Cancel button">
        JButton cancelBtn2 = new JButton("Zrušit");
        cancelBtn2.addActionListener((e) -> {
            WindowUIDialog dialog = new WindowUIDialog(
                    "Ukončit průvodce",
                    "Opravdu chcete ukončit průvodce vytvořením nového tarifu?",
                    WindowUIDialogType.QUESTION,
                    WindowUIButtonType.YES,
                    WindowUIButtonType.NO
            );
            dialog.showDialog();
            if (dialog.getResult() == WindowUIButtonType.YES)
            {
                this.okClicked = false;
                this.dispose();
            }
        });
        stage2Btns.add(cancelBtn2);
        //</editor-fold>
        this.stage2.add(stage2Btns, BorderLayout.SOUTH);
        //</editor-fold>
    }
    
    /**
     * Gets maximal number of zone
     * @return Maximal number of zone
     */
    private int getMaximalZone()
    {
        int reti = Integer.MIN_VALUE;
        if (this.zoneTable != null)
        {
            for (int r = 0; r < this.zoneTable.getModel().getRowCount(); r++)
            {
                int val = Integer.MIN_VALUE;
                try
                {
                    val = Integer.parseInt(this.zoneTable.getModel().getValueAt(r, 2).toString());
                }
                catch (Exception ex){}
                if (val > reti)
                {
                    reti = val;
                }
            }
        }
        else
        {
            reti = 0;
        }
        return reti;
    }
    
    /**
     * Checks, whether dialog has been closed by clicking on 'OK' button
     * @return <code>TRUE</code>, if dialog has been closed by clicking on 'OK'
     *         button, <code>FALSE</code> otherwise
     */
    public boolean getOKClicked()
    {
        return this.okClicked;
    }
    
    /**
     * Gets tariff created by dialog
     * @return New tariff created by dialog
     */
    public Tariff getTariff()
    {
        Tariff reti = null;
        if (this.tariffType == TariffType.DISTANCE)
        {
            DistanceTariff ret = new DistanceTariff(
                    this.tariffName.getText(),
                    this.tariffAbbr.getText()
            );
            for (int r = 0; r < this.priceTable.getModel().getRowCount(); r++)
            {
                int price = 0;
                try
                {
                    price = Integer.parseInt(
                            this.priceTable.getModel().getValueAt(r, 
                                    this.priceTable.getModel().getColumnCount() - 1)
                            .toString()
                    );
                }
                catch(Exception ex){}
                ret.SetPrice(r, price);
                
            }
            reti = ret;
        }
        else if (this.tariffType == TariffType.ZONE)
        {
            ZoneTariff ret = new ZoneTariff(
                    this.tariffName.getText(),
                    this.tariffAbbr.getText()
            );
            // Set zones
            for (int r = 0; r < this.zoneTable.getModel().getRowCount(); r++)
            {
                int zone = 0;
                try
                {
                    zone = Integer.parseInt(
                            this.zoneTable.getModel().getValueAt(
                                    r,
                                    this.zoneTable.getColumnCount() - 1
                            ).toString()
                    );
                            
                }
                catch (Exception ex){}
                ret.setZone(Stations.GetInstance().GetStation(this.zoneTable.getModel().getValueAt(r, 0).toString()), zone);
            }
            // Set prices
            for (int r = 0; r < this.priceTable.getModel().getRowCount(); r++)
            {
                int price = 0;
                try
                {
                    price = Integer.parseInt(
                            this.priceTable.getModel().getValueAt(r, 
                                    this.priceTable.getModel().getColumnCount() - 1)
                            .toString()
                    );
                }
                catch(Exception ex){}
                ret.SetPrice(r, price);                
            }
            reti = ret;
        }
        return reti;
    }
}
