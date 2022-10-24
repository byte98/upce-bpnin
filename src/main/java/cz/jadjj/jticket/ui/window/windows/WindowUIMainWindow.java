package cz.jadjj.jticket.ui.window.windows;

import cz.jadjj.jticket.data.Configuration;
import cz.jadjj.jticket.data.Station;
import cz.jadjj.jticket.data.Stations;
import cz.jadjj.jticket.data.Tariff;
import cz.jadjj.jticket.data.Tariffs;
import cz.jadjj.jticket.data.Ticket;
import cz.jadjj.jticket.jTicket;
import cz.jadjj.jticket.ui.window.WindowUI;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIButtonType;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialog;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialogType;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

/**
 * Class representing main window of program
 * @author jadjj
 */
public class WindowUIMainWindow extends WindowUIWindow
{    
    /**
     * Margin between components
     */
    private static final int MARGIN = 5;
    
    /**
     * Padding inside components
     */
    private static final int PADDING = 10;
    
    
    /**
     * Item in menu which opens settings window
     */
    private JMenuItem itemSettings;
    
    /**
     * Data menu in top menu bar
     */
    private JMenu dataMenu;
        
    /**
     * Item in menu which opens stations settings window
     */
    private JMenuItem itemStations;
    
    /**
     * Item in menu which opens distances settings window
     */
    private JMenuItem itemDistances;
    
    /**
     * Item in menu which opens tariffs settings window
     */
    private JMenuItem itemTariffs;
    
    /**
     * Item which opens data directory in file explorer
     */
    private JMenuItem openExplorer;
    
    /**
     * Item which opens 'about' window
     */
    private JMenuItem aboutItem;
    
    /**
     * Window with stations
     */
    private final WindowUIStationsWindow stationsWindow;
    
    /**
     * Label holding identifier of ticket
     */
    private JLabel textFieldTicketID;
    
    /**
     * ComboBox with tickets tariff
     */
    private JComboBox comboBoxTicketTariff;
    
    /**
     * ComboBox with tickets origin station
     */
    private JComboBox comboBoxTicketOrigin;
    
    /**
     * ComboBox with tickets destination station
     */
    private JComboBox comboBoxTicketDestination;
    
    /**
     * Label holding validity of ticket
     */
    private JLabel textFieldTicketValidity;
    
    /**
     * Label holding distance between origin and destination
     */
    private JLabel textFieldTicketDistance;
    
    /**
     * Label holding price without VAT
     */
    private JLabel textFieldTicketPriceWoVAT;
    
    /**
     * Label holding VAT rate
     */
    private JLabel textFieldTicketVAT;
    
    /**
     * Label holding VAT value
     */
    private JLabel textFieldTicketVATValue;
    
    /**
     * Label holding price of ticket
     */
    private JLabel textFieldTicketPrice;
    
    /**
     * Actually processed ticket
     */
    private Ticket TICKET;
    
    /**
     * Path to file with ticket
     */
    private String TICKET_PATH = null;
    
    /**
     * Button which cancels actually processed ticket
     */
    private JButton buttonCancel;
    
    /**
     * Creates new main window of program
     */
    public WindowUIMainWindow()
    {
        super("jTicket", "default.png");
        super.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.stationsWindow = new WindowUIStationsWindow();
        this.stationsWindow.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e)
            {
                setEnabled(true);
            }
        });
        super.addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent ev)
            {
                WindowUIDialog exitDialog = new WindowUIDialog("Ukončit aplikaci", "Opravdu chcete ukončit aplikaci?", WindowUIDialogType.QUESTION, WindowUIButtonType.YES, WindowUIButtonType.NO);
                exitDialog.showDialog();
                if (exitDialog.getResult() == WindowUIButtonType.YES)
                {
                    dispose();
                    System.exit(0);
                }
            }
        });
        
        this.prepareContent();
        this.prepareData();
        this.updateTicketData();
        super.setPreferredSize(new Dimension(1024, 720));
    }
    
    @Override
    protected void prepareTopMenu()
    {        
        super.prepareTopMenu();
        
        // Settings of program
        this.itemSettings = new JMenuItem();
        this.itemSettings.setText("Nastavení");
        this.itemSettings.setIcon(new ImageIcon(WindowUI.UI_PATH + "/settings.png"));
        this.itemSettings.addActionListener((e) -> {
            WindowUISettingsWindow settingsWindow = new WindowUISettingsWindow(Configuration.getInstance(jTicket.CONFIG_FILE));
            settingsWindow.setVisible(true);
        });
        this.fileMenu.add(this.itemSettings, 0);
                
        // Data menu
        this.dataMenu = new JMenu();
        this.dataMenu.setText("Data");
        this.dataMenu.setIcon(new ImageIcon(WindowUI.UI_PATH + "/data.png"));
        
        // Stations menu item
        this.itemStations = new JMenuItem();
        this.itemStations.setText("Stanice");
        this.itemStations.setIcon(new ImageIcon(WindowUI.UI_PATH + "/dataitem.png"));
        this.itemStations.addActionListener((ActionEvent e) -> {
            stationsWindow.setData(Stations.GetInstance().GetAllStations());
            stationsWindow.setVisible(true);
            stationsWindow.setPreferredSize(getPreferredSize());
            setEnabled(false);
            stationsWindow.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosed(WindowEvent e)
                {
                    toFront();
                    prepareData();
                }
                
            });
        });
        this.dataMenu.add(this.itemStations);
        
        // Distances menu item
        this.itemDistances = new JMenuItem();
        this.itemDistances.setText("Vzdálenosti");
        this.itemDistances.setIcon(new ImageIcon(WindowUI.UI_PATH + "/dataitem.png"));
        this.itemDistances.addActionListener((e) -> {
            WindowUIDistancesWindow distancesWindow = new WindowUIDistancesWindow();
            distancesWindow.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosed(WindowEvent e)
                {
                    toFront();
                    prepareData();
                }
            });
            distancesWindow.setVisible(true);
        });
        this.dataMenu.add(this.itemDistances);
        
        // Tariffs menu item
        this.itemTariffs = new JMenuItem();
        this.itemTariffs.setText("Tarify");
        this.itemTariffs.setIcon(new ImageIcon(WindowUI.UI_PATH + "/dataitem.png"));
        this.itemTariffs.addActionListener((e) -> {
            WindowUITariffsWindow tariffsWindow = new WindowUITariffsWindow();
            tariffsWindow.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosed(WindowEvent e)
                {
                    toFront();
                    prepareData();
                }
            });
            tariffsWindow.setVisible(true);            
        });
        this.dataMenu.add(this.itemTariffs);
        
        // Open explorer item
        this.openExplorer = new JMenuItem();
        this.openExplorer.setText("Otevřít v prohlížeči souborů");
        this.openExplorer.setIcon(new ImageIcon(WindowUI.UI_PATH + "/explorer.png"));
        this.openExplorer.addActionListener((ActionEvent e) -> {
            Configuration cfg = Configuration.getInstance(jTicket.CONFIG_FILE);
            try {        
                Desktop.getDesktop().open(new File(cfg.outputDirectory));
            } catch (IOException ex) {
                WindowUI.handleException(ex);
            }
        });
        this.fileMenu.add(this.openExplorer, 0);
        
        // "About" item
        this.aboutItem = new JMenuItem();
        this.aboutItem.setText("O programu jTicket");
        this.aboutItem.setIcon(new ImageIcon(WindowUI.UI_PATH + "/about-s.png"));
        this.aboutItem.addActionListener((e) -> {
            JDialog aboutWindow = WindowUIAboutWindow.getWindow();
            aboutWindow.setVisible(true);
        });
        this.fileMenu.add(this.aboutItem, this.fileMenu.getItemCount() - 1);
        
        this.topMenu.add(this.dataMenu);
    }
    
    /**
     * Prepares main part of window
     */
    private void prepareContent()
    {
        Font valueFont = super.getContentPane().getFont().deriveFont((float)24);
        //<editor-fold defaultstate="collapsed" desc="Content of window">
        JPanel root = new JPanel(new GridLayout(1, 1));
        super.getContentPane().add(root, BorderLayout.CENTER);
        JPanel content = new JPanel(new GridBagLayout());
        root.add(content);
        //<editor-fold defaultstate="collapsed" desc="Date panel">
        JPanel datePanel = new JPanel();
        datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
        JLabel dateTextLabel = new JLabel("Datum vystavení");
        dateTextLabel.setBorder(BorderFactory.createEmptyBorder(WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING));
        datePanel.add(new JLabel("Datum vystavení"));
        LocalDateTime now = LocalDateTime.now();
        JLabel dateLabel = new JLabel(String.format(
                "%02d.%02d.%04d",
                now.getDayOfMonth(),
                now.getMonthValue(),
                now.getYear()
        ));
        dateLabel.setFont(valueFont);
        dateLabel.setBorder(BorderFactory.createEmptyBorder(WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING));
        datePanel.add(dateLabel);
        datePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        GridBagConstraints datePanelConstraints = new GridBagConstraints();
        datePanelConstraints.gridx = 0;
        datePanelConstraints.gridy = 0;
        datePanelConstraints.gridheight = 1;
        datePanelConstraints.gridwidth = 2;
        datePanelConstraints.insets = new Insets(WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN);
        datePanelConstraints.fill = GridBagConstraints.BOTH;
        content.add(datePanel, datePanelConstraints);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Ticket identifier panel">
        JPanel ticketIdPanel = new JPanel();
        ticketIdPanel.setLayout(new BoxLayout(ticketIdPanel, BoxLayout.Y_AXIS));
        ticketIdPanel.add(new JLabel("Číslo dokladu"));
        this.textFieldTicketID = new JLabel(String.format("JT%012d", Configuration.getInstance(jTicket.CONFIG_FILE).ticketNr + 1));
        this.textFieldTicketID.setFont(valueFont);
        this.textFieldTicketID.setBorder(BorderFactory.createEmptyBorder(WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING));
        ticketIdPanel.add(this.textFieldTicketID);
        ticketIdPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        GridBagConstraints ticketIdPanelConstraints = new GridBagConstraints();
        ticketIdPanelConstraints.gridx = 2;
        ticketIdPanelConstraints.gridy = 0;
        ticketIdPanelConstraints.gridwidth = 2;
        ticketIdPanelConstraints.gridheight = 1;
        ticketIdPanelConstraints.insets = new Insets(WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN);
        ticketIdPanelConstraints.fill = GridBagConstraints.BOTH;
        content.add(ticketIdPanel, ticketIdPanelConstraints);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Tariff panel">
        JPanel tariffPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 10));
        JLabel tariffText = new JLabel("Tarif: ");
        tariffText.setFont(valueFont);
        tariffPanel.add(tariffText);
        GridBagConstraints tariffPanelConstraints = new GridBagConstraints();
        tariffPanelConstraints.gridx = 0;
        tariffPanelConstraints.gridy = 1;
        tariffPanelConstraints.gridwidth = 4;
        tariffPanelConstraints.gridheight = 1;
        tariffPanelConstraints.insets = new Insets(WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN);
        tariffPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        tariffPanelConstraints.fill = GridBagConstraints.BOTH;
        content.add(tariffPanel, tariffPanelConstraints);
        this.comboBoxTicketTariff = new JComboBox();
        this.comboBoxTicketTariff.setFont(valueFont);
        this.comboBoxTicketTariff.setEditable(true);
        tariffPanel.add(this.comboBoxTicketTariff);
        this.comboBoxTicketTariff.addActionListener((e) -> {
            if (Objects.nonNull(this.TICKET) && this.comboBoxTicketTariff.getSelectedItem() != this.TICKET.getTariffText())
            {
                this.updateTicketData();
            }
        });
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Stations panel">
        JPanel stationsPanel = new JPanel();
        stationsPanel.setLayout(new BoxLayout(stationsPanel, BoxLayout.Y_AXIS));
        GridBagConstraints stationsPanelConstraints = new GridBagConstraints();
        stationsPanelConstraints.gridx = 0;
        stationsPanelConstraints.gridy = 2;
        stationsPanelConstraints.gridwidth = 4;
        stationsPanelConstraints.gridheight = 2;
        stationsPanelConstraints.insets = new Insets(WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN);
        stationsPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        stationsPanelConstraints.fill = GridBagConstraints.BOTH;
        content.add(stationsPanel, stationsPanelConstraints);
        JLabel fromText = new JLabel("Z:   ");
        fromText.setFont(valueFont);
        JPanel fromPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING));
        fromPanel.add(fromText);
        this.comboBoxTicketOrigin = new JComboBox();
        this.comboBoxTicketOrigin.setFont(valueFont);
        this.comboBoxTicketOrigin.setEditable(true);
        this.comboBoxTicketOrigin.addActionListener((e) -> {
            if (Objects.nonNull(this.TICKET) && this.comboBoxTicketOrigin.getSelectedItem() != this.TICKET.getFromText())
            {
                this.updateTicketData();
            }            
        });
        fromPanel.add(this.comboBoxTicketOrigin);
        stationsPanel.add(fromPanel);
        JPanel toPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING));
        JLabel toText = new JLabel("Do: ");
        toText.setFont(valueFont);
        toPanel.add(toText);
        this.comboBoxTicketDestination = new JComboBox();
        this.comboBoxTicketDestination.setFont(valueFont);
        this.comboBoxTicketDestination.setEditable(true);
        this.comboBoxTicketDestination.addActionListener((e) -> {
            if (Objects.nonNull(this.TICKET) && this.comboBoxTicketDestination.getSelectedItem() != this.TICKET.getToText())
            {
                this.updateTicketData();
            } 
        });
        toPanel.add(this.comboBoxTicketDestination);
        stationsPanel.add(toPanel);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Validity panel">
        JPanel validityPanel = new JPanel();
        validityPanel.setLayout(new BoxLayout(validityPanel, BoxLayout.Y_AXIS));
        validityPanel.add(new JLabel("Platnost"));
        this.textFieldTicketValidity = new JLabel();
        this.textFieldTicketValidity.setFont(valueFont);
        this.textFieldTicketValidity.setBorder(BorderFactory.createEmptyBorder(WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING));
        validityPanel.add(this.textFieldTicketValidity);
        GridBagConstraints validityPanelConstraints = new GridBagConstraints();
        validityPanelConstraints.gridx = 0;
        validityPanelConstraints.gridy = 4;
        validityPanelConstraints.gridwidth = 2;
        validityPanelConstraints.gridheight = 1;
        validityPanelConstraints.insets = new Insets(WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN);
        validityPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        validityPanelConstraints.fill = GridBagConstraints.BOTH;
        content.add(validityPanel, validityPanelConstraints);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Distance panel">
        JPanel distancePanel = new JPanel();
        distancePanel.setLayout(new BoxLayout(distancePanel, BoxLayout.Y_AXIS));
        distancePanel.add(new JLabel("Vzdálenost"));
        this.textFieldTicketDistance = new JLabel();
        this.textFieldTicketDistance.setFont(valueFont);
        this.textFieldTicketDistance.setBorder(BorderFactory.createEmptyBorder(WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING));
        distancePanel.add(this.textFieldTicketDistance);
        GridBagConstraints distancePanelConstraints = new GridBagConstraints();
        distancePanelConstraints.gridx = 2;
        distancePanelConstraints.gridy = 4;
        distancePanelConstraints.gridwidth = 2;
        distancePanelConstraints.gridheight = 1;
        distancePanelConstraints.insets = new Insets(WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN);
        distancePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        distancePanelConstraints.fill = GridBagConstraints.BOTH;
        content.add(distancePanel, distancePanelConstraints);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Price panel">
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new BoxLayout(pricePanel, BoxLayout.X_AXIS));
        GridBagConstraints pricePanelConstraints = new GridBagConstraints();
        pricePanelConstraints.gridx = 0;
        pricePanelConstraints.gridy = 5;
        pricePanelConstraints.gridwidth = 4;
        pricePanelConstraints.gridheight = 2;
        pricePanelConstraints.insets = new Insets(WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN);
        pricePanel.setBorder(BorderFactory.createLoweredBevelBorder());
        pricePanelConstraints.fill = GridBagConstraints.BOTH;
        content.add(pricePanel, pricePanelConstraints);
        JPanel VATPanel = new JPanel();
        VATPanel.setLayout(new BoxLayout(VATPanel, BoxLayout.Y_AXIS));
        VATPanel.setBorder(BorderFactory.createEmptyBorder(WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING));
        VATPanel.add(new JLabel("Cena bez DPH"));
        pricePanel.add(VATPanel);
        this.textFieldTicketPriceWoVAT = new JLabel();
        this.textFieldTicketPriceWoVAT.setFont(valueFont);
        VATPanel.add(this.textFieldTicketPriceWoVAT);
        JPanel VATSubPanel  = new JPanel();
        VATSubPanel.setLayout(new BoxLayout(VATSubPanel, BoxLayout.X_AXIS));
        VATPanel.add(new JLabel("Daň z přidané hodnoty"));
        this.textFieldTicketVAT = new JLabel();
        this.textFieldTicketVAT.setFont(valueFont);
        VATPanel.add(this.textFieldTicketVAT);
        this.textFieldTicketVATValue = new JLabel();
        VATPanel.add(this.textFieldTicketVATValue);
        this.textFieldTicketVATValue.setFont(valueFont);
        JPanel priceSubPanel = new JPanel();
        priceSubPanel.setLayout(new BoxLayout(priceSubPanel, BoxLayout.Y_AXIS));
        priceSubPanel.add(new JLabel("Cena"));
        this.textFieldTicketPrice = new JLabel();
        this.textFieldTicketPrice.setFont(valueFont.deriveFont(Font.BOLD).deriveFont((float)48));
        priceSubPanel.add(this.textFieldTicketPrice);
        pricePanel.add(priceSubPanel);
        priceSubPanel.setBorder(BorderFactory.createEmptyBorder(WindowUIMainWindow.PADDING, 4 * WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING, WindowUIMainWindow.PADDING));
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Buttons">
            Font buttonFont = super.getContentPane().getFont().deriveFont((float) 18);
            //<editor-fold defaultstate="collapsed" desc="Cancel button">
        this.buttonCancel = new JButton();
        this.buttonCancel.setFont(buttonFont);
        this.buttonCancel.setIcon(new ImageIcon(WindowUI.UI_PATH + "/cancel.png"));
        this.buttonCancel.setText("Zrušit");
        this.buttonCancel.addActionListener((e) -> {
            this.TICKET_PATH = null;
            this.comboBoxTicketTariff.setSelectedIndex(0);
            this.comboBoxTicketOrigin.setSelectedIndex(0);
            this.comboBoxTicketDestination.setSelectedIndex(0);
            this.updateTicketData();
            this.checkIfDone();
            this.setState("Připraven");
        });
        GridBagConstraints buttonCancelConstraints = new GridBagConstraints();
        buttonCancelConstraints.gridx = 0;
        buttonCancelConstraints.gridy = 7;
        buttonCancelConstraints.gridwidth = 1;
        buttonCancelConstraints.gridheight = 1;
        buttonCancelConstraints.fill = GridBagConstraints.BOTH;
        buttonCancelConstraints.insets = new Insets(WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN);
        content.add(this.buttonCancel, buttonCancelConstraints);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Print button">
        JButton buttonPrint = new JButton();
        buttonPrint.setFont(buttonFont);
        buttonPrint.setIcon(new ImageIcon(WindowUI.UI_PATH + "/print.png"));
        buttonPrint.setText("Tisk");
        buttonPrint.addActionListener((e) -> {
            this.createTicketFile();
            try
            {
                PDDocument doc = PDDocument.load(new File(this.TICKET_PATH));
                PrinterJob pj = PrinterJob.getPrinterJob();
                pj.setPageable(new PDFPageable(doc));
                if (pj.printDialog())
                {
                    pj.print();
                }
            }
            catch (IOException | PrinterException ex)
            {
                Logger.getLogger(WindowUIMainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        GridBagConstraints buttonPrintConstraints = new GridBagConstraints();
        buttonPrintConstraints.gridx = 1;
        buttonPrintConstraints.gridy = 7;
        buttonPrintConstraints.gridwidth = 1;
        buttonPrintConstraints.gridheight = 1;
        buttonPrintConstraints.fill = GridBagConstraints.BOTH;
        buttonPrintConstraints.insets = new Insets(WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN);
        content.add(buttonPrint, buttonPrintConstraints);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Open file button">
        JButton buttonOpen = new JButton();
        buttonOpen.setFont(buttonFont);
        buttonOpen.setIcon(new ImageIcon(WindowUI.UI_PATH + "/open.png"));
        buttonOpen.setText("Otevřít soubor");
        buttonOpen.addActionListener((e) -> {
            this.createTicketFile();
            File f = new File(this.TICKET_PATH);
            if (Desktop.isDesktopSupported())
            {
                try
                {
                    Desktop.getDesktop().open(f);
                }
                catch (IOException ex)
                {
                    WindowUIDialog dialog = new WindowUIDialog(
                        "CHYBA: Otevřít soubor",
                        "Nelze otevřít soubor s jízdenkou." + System.lineSeparator() + 
                        "Soubor nebyl nalezen.",
                        WindowUIDialogType.ERROR,
                        WindowUIButtonType.CANCEL
                );
                dialog.showDialog();
                }
            }
            else
            {
                WindowUIDialog dialog = new WindowUIDialog(
                        "CHYBA: Otevřít soubor",
                        "Nelze otevřít soubor s jízdenkou." + System.lineSeparator() + 
                        "Tato funkce není na tomto systému podporována.",
                        WindowUIDialogType.ERROR,
                        WindowUIButtonType.CANCEL
                );
                dialog.showDialog();
            }
        });
        GridBagConstraints buttonOpenConstraints = new GridBagConstraints();
        buttonOpenConstraints.gridx = 2;
        buttonOpenConstraints.gridy = 7;
        buttonOpenConstraints.gridwidth = 1;
        buttonOpenConstraints.gridheight = 1;
        buttonOpenConstraints.fill = GridBagConstraints.BOTH;
        buttonOpenConstraints.insets = new Insets(WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN);
        content.add(buttonOpen, buttonOpenConstraints);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Finish button">
        JButton buttonFinish = new JButton();
        buttonFinish.setFont(buttonFont);
        buttonFinish.setIcon(new ImageIcon(WindowUI.UI_PATH + "/finish.png"));
        buttonFinish.setText("Dokončit a pokračovat");
        buttonFinish.addActionListener((e) -> {
            this.createTicketFile();
            this.TICKET_PATH = null;
            this.TICKET = null;
            this.comboBoxTicketTariff.setSelectedIndex(0);
            this.comboBoxTicketOrigin.setSelectedIndex(0);
            this.comboBoxTicketDestination.setSelectedIndex(0);
            this.updateTicketData();
            this.checkIfDone();
            this.setState("Připraven");
        });
        GridBagConstraints buttonFinishConstraints = new GridBagConstraints();
        buttonFinishConstraints.gridx = 3;
        buttonFinishConstraints.gridy = 7;
        buttonFinishConstraints.gridwidth = 1;
        buttonFinishConstraints.gridheight = 1;
        buttonFinishConstraints.fill = GridBagConstraints.BOTH;
        buttonFinishConstraints.insets = new Insets(WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN, WindowUIMainWindow.MARGIN);
        content.add(buttonFinish, buttonFinishConstraints);
            //</editor-fold>
        //</editor-fold>
        //</editor-fold>
    }
    
    /**
     * Prepares data in ticket form
     */
    private void prepareData()
    {
        //<editor-fold defaultstate="collapsed" desc="Tariffs">
        this.comboBoxTicketTariff.removeAllItems();
        for (Tariff t: Tariffs.GetInstance().GetAllTariffs())
        {
            this.comboBoxTicketTariff.addItem(t.getName());
        }
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Origin stations">
        this.comboBoxTicketOrigin.removeAllItems();
        for (Station s: Stations.GetInstance().GetAllStations())
        {
            this.comboBoxTicketOrigin.addItem(s.getName());
        }                
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Destination stations">
        this.comboBoxTicketDestination.removeAllItems();
        for (Station s: Stations.GetInstance().GetAllStations())
        {
            this.comboBoxTicketDestination.addItem(s.getName());
        }                
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Set default values">
        this.comboBoxTicketTariff.setSelectedIndex(0);
        this.comboBoxTicketOrigin.setSelectedIndex(0);
        this.comboBoxTicketDestination.setSelectedIndex(0);
        //</editor-fold>
    }
    
    /**
     * Updates ticket data
     */
    private void updateTicketData()
    {
        if (this.TICKET == null)
        {
            this.TICKET = new Ticket();
            this.TICKET.initializeTicket();
        }
        
        // Check tariff
        if (this.comboBoxTicketTariff.getSelectedItem() != null)
        {
            Tariff t = Tariffs.GetInstance().GetTariff(this.comboBoxTicketTariff.getSelectedItem().toString());
            if (t != null)
            {
                this.TICKET.setTariff(t);
            }
        }
        
        // Check origin
        if (this.comboBoxTicketOrigin.getSelectedItem() != null)
        {
            Station o = Stations.GetInstance().GetStation(this.comboBoxTicketOrigin.getSelectedItem().toString());
            if (o != null)
            {
                this.TICKET.setOrigin(o);
            }
        }
        
        // Check destination
        if (this.comboBoxTicketDestination.getSelectedItem() != null)
        {
            Station d = Stations.GetInstance().GetStation(this.comboBoxTicketDestination.getSelectedItem().toString());
            if (d != null)
            {
                this.TICKET.setDestination(d);
            }
        }
        
        // Show full ticket data
        this.textFieldTicketID.setText(this.TICKET.getId());
        this.comboBoxTicketTariff.setSelectedItem(this.TICKET.getTariffText());
        this.comboBoxTicketOrigin.setSelectedItem(this.TICKET.getFromText());
        this.comboBoxTicketDestination.setSelectedItem(this.TICKET.getToText());
        this.textFieldTicketValidity.setText(this.TICKET.getValidityText());
        this.textFieldTicketDistance.setText(this.TICKET.getDistanceText());
        this.textFieldTicketPriceWoVAT.setText(String.format("%.2f CZK", this.TICKET.getPriceWoVAT()));
        this.textFieldTicketVAT.setText(String.format("%.2f %%", this.TICKET.getVATRate()));
        this.textFieldTicketVATValue.setText(String.format("%.2f CZK", this.TICKET.getVATValue()));
        this.textFieldTicketPrice.setText(String.format("%d CZK", this.TICKET.getPrice()));
    }
    
    /**
     * Creates ticket file for actual ticket
     */
    private void createTicketFile()
    {
        if (this.TICKET_PATH == null)
        {
            File f = new File(Configuration.getInstance(jTicket.CONFIG_FILE).outputDirectory + File.separator +  this.TICKET.getId() + ".pdf");
            this.TICKET_PATH = f.getAbsolutePath();
            this.TICKET.printToPdf(this.TICKET_PATH);
            this.setState("Jízdenka byla úspěšně uložena");
            this.checkIfDone();
        }
    }
    
    /**
     * Checks, whether ticket has been finished or not
     */
    private void checkIfDone()
    {
        this.comboBoxTicketTariff.setEnabled(this.TICKET_PATH == null);
        this.comboBoxTicketOrigin.setEnabled(this.TICKET_PATH == null);
        this.comboBoxTicketDestination.setEnabled(this.TICKET_PATH == null);
        this.buttonCancel.setEnabled(this.TICKET_PATH == null);
    }
    
}
