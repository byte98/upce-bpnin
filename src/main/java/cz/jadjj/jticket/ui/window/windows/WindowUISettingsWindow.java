package cz.jadjj.jticket.ui.window.windows;

import cz.jadjj.jticket.data.Configuration;
import cz.jadjj.jticket.data.UIMode;
import cz.jadjj.jticket.jTicket;
import cz.jadjj.jticket.ui.window.WindowUI;
import cz.jadjj.jticket.ui.window.WindowUITheme;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIButtonType;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialog;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialogType;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class representing window with settings
 * @author Jiri Skoda <skodaji1@uhk.cz.cz>
 */
public class WindowUISettingsWindow extends JDialog
{
    /**
     * Reference to configuration of program
     */
    private final Configuration config;
    
    /**
     * Padding inside components
     */
    private static final int PADDING = 5;
    
    /**
     * Margin between components
     */
    private static final int MARGIN = 10;
    
    /**
     * Colour used for warning texts
     */
    private static final Color WARNING = new Color(200, 0, 0);
    
    /**
     * Spinner holding ticket validity
     */
    private JSpinner ticketValidity;
    
    /**
     * Text field holding path to directory where all tickets will be printed into
     */
    private JTextField ticketOutput;
    
    /**
     * Spinner holding VAT rate
     */
    private JSpinner ticketVAT;
    
    /**
     * Text field holding path to file with ticket template
     */
    private JTextField ticketTemplate;
    
    /**
     * Text field holding path to file with ticket background
     */
    private JTextField ticketBackground;
    
    /**
     * Spinner holding counter of issued tickets
     */
    private JSpinner ticketIssued;
    
    /**
     * Radio button for graphics application mode
     */
    private JRadioButton appearanceModeGraphics;
    
    /**
     * Radio button for text application mode
     */
    private JRadioButton appearanceModeText;
    
    /**
     * Combo box holding selected user interface theme
     */
    private JComboBox<String> appearanceTheme;
    
    /**
     * Map of themes names and displayNames
     */
    private Map<String, String> themeMap;
    
    /**
     * Creates new window with settings
     * @param c Actual configuration of program
     */
    public WindowUISettingsWindow(Configuration c)
    {
        super(null, java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        this.config = c;
        this.themeMap = new HashMap<>();
        for (WindowUITheme t: WindowUITheme.THEMES)
        {
            this.themeMap.put(t.getDisplayName(), t.getName());
        }
        super.setTitle("jTicket - Nastavení programu");
        super.setIconImage(new ImageIcon(WindowUI.UI_PATH + "/settings.png").getImage());
        super.setSize(new Dimension(WindowUISettingsWindow.WIDTH, WindowUISettingsWindow.HEIGHT));
        this.initializeComponents();
        super.setSize(750, 800);
        super.setModal(true);
        super.setResizable(false);
        super.setLocation(
                ((Toolkit.getDefaultToolkit().getScreenSize().width - super.getSize().width) / 2),
                ((Toolkit.getDefaultToolkit().getScreenSize().height - super.getSize().height) / 2)

        );
    }
    
    /**
     * Initializes components in window
     */
    private void initializeComponents()
    {
        //<editor-fold defaultstate="collapsed" desc="Content of window">
        super.getContentPane().setLayout(new BorderLayout(
                WindowUISettingsWindow.MARGIN,
                WindowUISettingsWindow.MARGIN));
        final Font HEADER = super.getContentPane().getFont().deriveFont(Font.BOLD).deriveFont((float)18);
        final Font ITEM = super.getContentPane().getFont().deriveFont(Font.BOLD);
        JPanel content = new JPanel(new CardLayout());
        super.getContentPane().add(content, BorderLayout.CENTER);
            //<editor-fold defaultstate="collapsed" desc="Ticket settings">
            JPanel ticketsSettings = new JPanel();
            ticketsSettings.setLayout(new BoxLayout(ticketsSettings, BoxLayout.Y_AXIS));
            ticketsSettings.setBorder(BorderFactory.createEmptyBorder(
                WindowUISettingsWindow.PADDING,
                WindowUISettingsWindow.PADDING,
                WindowUISettingsWindow.PADDING,
                WindowUISettingsWindow.PADDING));
                //<editor-fold defaultstate="collapsed" desc="Header">
                JPanel ticketHeader = new JPanel(new FlowLayout(
                        FlowLayout.LEADING,
                        WindowUISettingsWindow.MARGIN,
                        WindowUISettingsWindow.MARGIN
                ));
                ticketHeader.add(new JLabel(new ImageIcon(WindowUI.UI_PATH + "/settings-tickets.png")));
                JLabel ticketHeaderLabel = new JLabel("Nastavení jízdenek");
                ticketHeaderLabel.setFont(HEADER);
                ticketHeader.add(ticketHeaderLabel);
                ticketsSettings.add(ticketHeader);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Validity">
                JLabel ticketValidityLabel = new JLabel("Platnost jízdenek");
                ticketValidityLabel.setFont(ITEM);
                ticketValidityLabel.setBorder(BorderFactory.createEmptyBorder(
                        WindowUISettingsWindow.MARGIN,
                        0, 0, 0
                ));
                ticketsSettings.add(ticketValidityLabel);
                ticketsSettings.add(new JLabel("Nastaví platnost jízdenek jako počet hodin od jejich vydání."));
                JPanel ticketValidityPanel = new JPanel(new FlowLayout(
                        FlowLayout.LEADING
                ));
                this.ticketValidity = new JSpinner(new SpinnerNumberModel(this.config.ticketValidity, 0, Integer.MAX_VALUE, 1));
                ticketValidityPanel.add(this.ticketValidity);
                ticketValidityPanel.add(new JLabel("hodin"));
                ticketsSettings.add(ticketValidityPanel);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Output">
                JLabel ticketOutputLabel = new JLabel("Úložiště jízdenek");
                ticketOutputLabel.setFont(ITEM);
                ticketOutputLabel.setBorder(BorderFactory.createEmptyBorder(
                        WindowUISettingsWindow.MARGIN,
                        0, 0, 0
                ));
                ticketsSettings.add(ticketOutputLabel);
                ticketsSettings.add(new JLabel("Nastaví cestu k adresáři, do kterého se budou ukládat všechny jízdenky."));
                JPanel ticketOutputPanel = new JPanel(new FlowLayout(
                        FlowLayout.LEADING
                ));
                
                File ticketOutputFile = new File(this.config.outputDirectory);
                this.ticketOutput = new JTextField();
                this.ticketOutput.setText(ticketOutputFile.getAbsolutePath());
                ticketOutputPanel.add(this.ticketOutput);
                JButton ticketOutputButton = new JButton();
                ticketOutputButton.setIcon(new ImageIcon(WindowUI.UI_PATH + "/settings-search.png"));
                ticketOutputButton.addActionListener((e) -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("Vyberte adresář pro ukládání jízdenek");
                    chooser.setCurrentDirectory(ticketOutputFile);
                    chooser.setAcceptAllFileFilterUsed(false);
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
                    {
                        File chosen = chooser.getSelectedFile();
                        this.ticketOutput.setText(chosen.getAbsolutePath());
                    }
                });
                ticketOutputPanel.add(ticketOutputButton);
                ticketsSettings.add(ticketOutputPanel);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="VAT">
                JLabel ticketVATLabel = new JLabel("Daň z přidané hodnoty");
                ticketVATLabel.setFont(ITEM);
                ticketVATLabel.setBorder(BorderFactory.createEmptyBorder(
                        WindowUISettingsWindow.MARGIN,
                        0, 0, 0
                ));
                ticketsSettings.add(ticketVATLabel);
                ticketsSettings.add(new JLabel("Nastaví aktuálně platnou sazbu daně z přidné hodnoty v procentech."));
                JPanel ticketVATPanel = new JPanel(new FlowLayout(
                        FlowLayout.LEADING
                ));
                this.ticketVAT = new JSpinner(new SpinnerNumberModel(this.config.VAT, 0, Integer.MAX_VALUE, 0.01));
                ticketVATPanel.add(this.ticketVAT);
                ticketVATPanel.add(new JLabel("%"));
                ticketsSettings.add(ticketVATPanel);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Template">
                JLabel ticketTemplateLabel = new JLabel("Šablona");
                ticketTemplateLabel.setFont(ITEM);
                ticketTemplateLabel.setBorder(BorderFactory.createEmptyBorder(
                        WindowUISettingsWindow.MARGIN,
                        0, 0, 0
                ));
                ticketsSettings.add(ticketTemplateLabel);
                ticketsSettings.add(new JLabel("Nastaví cestu k souboru šabloně jízdenek, který popisuje rozložení jízdenek."));
                JPanel ticketTemplatePanel = new JPanel(new FlowLayout(
                        FlowLayout.LEADING
                ));
                
                File ticketTemplateFile = new File(this.config.ticketTemplate);
                this.ticketTemplate = new JTextField();
                this.ticketTemplate.setText(ticketTemplateFile.getAbsolutePath());
                ticketTemplatePanel.add(this.ticketTemplate);
                JButton ticketTemplateButton = new JButton();
                ticketTemplateButton.setIcon(new ImageIcon(WindowUI.UI_PATH + "/settings-search.png"));
                ticketTemplateButton.addActionListener((e) -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("Vyberte šablonu jízdenek");
                    chooser.setCurrentDirectory(ticketTemplateFile);
                    chooser.setAcceptAllFileFilterUsed(false);
                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("Šablona jízdenky (soubor XML)", "XML"));
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                    {
                        File chosen = chooser.getSelectedFile();
                        this.ticketTemplate.setText(chosen.getAbsolutePath());
                    }
                });
                ticketTemplatePanel.add(ticketTemplateButton);
                ticketsSettings.add(ticketTemplatePanel);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Background">
                JLabel ticketBackgroundLabel = new JLabel("Pozadí jízdenek");
                ticketBackgroundLabel.setFont(ITEM);
                ticketBackgroundLabel.setBorder(BorderFactory.createEmptyBorder(
                        WindowUISettingsWindow.MARGIN,
                        0, 0, 0
                ));
                ticketsSettings.add(ticketBackgroundLabel);
                ticketsSettings.add(new JLabel("Nastaví cestu k souboru s obrázkem, který bude použit jako obrázek na pozadí každé jízdenky."));
                JPanel ticketBackgroundPanel = new JPanel(new FlowLayout(
                        FlowLayout.LEADING
                ));
                
                File ticketBackgroundFile = new File(this.config.ticketBackground);
                this.ticketBackground = new JTextField();
                this.ticketBackground.setText(ticketBackgroundFile.getAbsolutePath());
                ticketBackgroundPanel.add(this.ticketBackground);
                JButton ticketBackgroundButton = new JButton();
                ticketBackgroundButton.setIcon(new ImageIcon(WindowUI.UI_PATH + "/settings-search.png"));
                ticketBackgroundButton.addActionListener((e) -> {
                    JFileChooser chooser = new JFileChooser();
                    chooser.setDialogTitle("Vyberte šablonu jízdenek");
                    chooser.setCurrentDirectory(ticketBackgroundFile);
                    chooser.setAcceptAllFileFilterUsed(false);
                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("Obrázek BMP", "BMP"));
                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("Obrázek GIF", "GIF"));
                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("Obrázek JPG", "JPG"));
                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("Obrázek JPEG", "JPEG"));
                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("Obrázek PNG", "PNG"));
                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("Obrázek TIF", "TIF"));
                    chooser.addChoosableFileFilter(new FileNameExtensionFilter("Obrázek TIFF", "TIFF"));
                    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                    {
                        File chosen = chooser.getSelectedFile();
                        this.ticketBackground.setText(chosen.getAbsolutePath());
                    }
                });
                ticketBackgroundPanel.add(ticketBackgroundButton);
                ticketsSettings.add(ticketBackgroundPanel);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Issued">
                JLabel ticketIssuedLabel = new JLabel("Počítadlo jízdenek");
                ticketIssuedLabel.setFont(ITEM);
                ticketIssuedLabel.setBorder(BorderFactory.createEmptyBorder(
                        WindowUISettingsWindow.MARGIN,
                        0, 0, 0
                ));
                ticketsSettings.add(ticketIssuedLabel);
                ticketsSettings.add(new JLabel("Zobrazuje počet již vydaných jízdenek."));
                JPanel ticketIssuedWarning = new JPanel(new FlowLayout(FlowLayout.LEADING));
                JLabel ticketIssuedWarningIcon = new JLabel();
                ticketIssuedWarningIcon.setIcon(new ImageIcon(WindowUI.UI_PATH +"/warning-s.png"));
                ticketIssuedWarning.add(ticketIssuedWarningIcon);
                JLabel ticketIssuedWarningText = new JLabel("<html>UPOZORNĚNÍ: Změna tohoto údaje může mít nepředvídatelné důsledky.<br>Změnu provádějte pouze pokud opravdu víte, co děláte.</html>");
                ticketIssuedWarningText.setFont(super.getContentPane().getFont().deriveFont(Font.BOLD));
                ticketIssuedWarningText.setForeground(WindowUISettingsWindow.WARNING);
                ticketIssuedWarning.add(ticketIssuedWarningText);
                ticketsSettings.add(ticketIssuedWarning);
                JPanel ticketIssuedPanel = new JPanel(new FlowLayout(
                        FlowLayout.LEADING
                ));
                this.ticketIssued = new JSpinner(new SpinnerNumberModel(this.config.ticketNr, 0, Integer.MAX_VALUE, 1));
                ticketIssuedPanel.add(this.ticketIssued);
                ticketIssuedPanel.add(new JLabel("jízdenek"));
                ticketsSettings.add(ticketIssuedPanel);
                //</editor-fold>
            for(Component c: ticketsSettings.getComponents())
            {
                JComponent j = (JComponent)c;
                j.setAlignmentX(Component.LEFT_ALIGNMENT);
                j.setAlignmentY(Component.TOP_ALIGNMENT);
            }
            //</editor-fold>
            content.add(ticketsSettings, "Jízdenky");
            //<editor-fold defaultstate="collapsed" desc="Appearance settings">
            JPanel appearanceSettings = new JPanel();
            appearanceSettings.setLayout(new BoxLayout(appearanceSettings, BoxLayout.Y_AXIS));
            appearanceSettings.setBorder(BorderFactory.createEmptyBorder(
                WindowUISettingsWindow.PADDING,
                WindowUISettingsWindow.PADDING,
                WindowUISettingsWindow.PADDING,
                WindowUISettingsWindow.PADDING));
                //<editor-fold defaultstate="collapsed" desc="Header">
                JPanel appearanceHeader = new JPanel(new FlowLayout(
                        FlowLayout.LEADING,
                        WindowUISettingsWindow.MARGIN,
                        WindowUISettingsWindow.MARGIN
                ));
                appearanceHeader.add(new JLabel(new ImageIcon(WindowUI.UI_PATH + "/settings-appearance.png")));
                JLabel appearanceHeaderLabel = new JLabel("Nastavení vzhledu");
                appearanceHeaderLabel.setFont(HEADER);
                appearanceHeader.add(appearanceHeaderLabel);
                appearanceSettings.add(appearanceHeader);
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Mode">
                JLabel appearanceModeLabel = new JLabel("Režim aplikace");
                appearanceModeLabel.setFont(ITEM);
                appearanceModeLabel.setBorder(BorderFactory.createEmptyBorder(
                        WindowUISettingsWindow.MARGIN,
                        0, 0, 0
                ));
                appearanceSettings.add(appearanceModeLabel);
                appearanceSettings.add(new JLabel("Nastaví režim zobrazení aplikace."));
                ButtonGroup appearanceModeGroup = new ButtonGroup();
                this.appearanceModeGraphics = new JRadioButton("GRAFICKÝ", true);
                appearanceModeGroup.add(this.appearanceModeGraphics);
                appearanceSettings.add(this.appearanceModeGraphics);
                appearanceSettings.add(new JLabel("<html>Bude použito úplné grafické rozhraní se všemi prvky<br>jako jsou tlačítka, přepínače a podobně.</html>"));
                appearanceSettings.add(new JLabel(System.lineSeparator()));
                this.appearanceModeText= new JRadioButton("TEXTOVÝ");
                appearanceModeGroup.add(this.appearanceModeText);
                appearanceSettings.add(this.appearanceModeText);
                appearanceSettings.add(new JLabel("<html>Bude použito zjednodušení grafické rozhraní připomínající příkazovou řádku.<br>Některé funkce aplikace mohou být omezené.</html>"));
                appearanceSettings.add(new JLabel(System.lineSeparator()));
                //</editor-fold>
                //<editor-fold defaultstate="collapsed" desc="Theme">
                JLabel appearanceThemeLabel = new JLabel("Motiv aplikace");
                appearanceThemeLabel.setFont(ITEM);
                appearanceThemeLabel.setBorder(BorderFactory.createEmptyBorder(
                        WindowUISettingsWindow.MARGIN,
                        0, 0, 0
                ));
                appearanceSettings.add(appearanceThemeLabel);
                appearanceSettings.add(new JLabel("Nastaví motiv grafického uživatelského rozhraní."));
                this.appearanceTheme = new JComboBox(this.themeMap.keySet().toArray());
                this.appearanceTheme.setSelectedItem(WindowUITheme.getTheme(Configuration.getInstance(jTicket.CONFIG_FILE).uiTheme).getDisplayName());
                appearanceSettings.add(this.appearanceTheme);
                //</editor-fold>
                for(int i = 0; i < 24; i++) // Fill remaining space
                {
                    appearanceSettings.add(new JLabel(System.lineSeparator()));
                }
            for(Component c: appearanceSettings.getComponents())
            {
                JComponent j = (JComponent)c;
                j.setAlignmentX(Component.LEFT_ALIGNMENT);
            }
            content.add(appearanceSettings, "Vzhled");
            //</editor-fold>
            content.add(new WindowUIAboutWindow(), "O programu");
            //<editor-fold defaultstate="collapsed" desc="Settings menu">
            String[] options = new String[]{"Jízdenky", "Vzhled", "O programu"};
            JList menu = new JList(options);
            menu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            Map<String, ImageIcon> imageMap = new HashMap<>();
            imageMap.put("Jízdenky", new ImageIcon(WindowUI.UI_PATH + "/settings-tickets.png"));
            imageMap.put("Vzhled", new ImageIcon(WindowUI.UI_PATH + "/settings-appearance.png"));
            imageMap.put("O programu", new ImageIcon(WindowUI.UI_PATH + "/about.png"));
            menu.setCellRenderer(new WindowUIImageListCellRenderer(imageMap));
            menu.setSelectedValue("Jízdenky", true);
            menu.setBorder(BorderFactory.createLoweredBevelBorder());
            menu.addListSelectionListener((e) -> {
                CardLayout cl = (CardLayout) content.getLayout();
                cl.show(content, (String)menu.getSelectedValue());
            });
            super.getContentPane().add(menu, BorderLayout.WEST);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Buttons">
                JPanel buttonsPanel = new JPanel(new FlowLayout(
                        FlowLayout.TRAILING,
                        WindowUISettingsWindow.MARGIN,
                        WindowUISettingsWindow.MARGIN
                ));
                super.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
                JButton buttonCancel = new JButton("Zrušit");
                buttonCancel.addActionListener((e) -> {
                    this.dispose();
                });
                buttonsPanel.add(buttonCancel);
                JButton buttonSave = new JButton("Uložit změny");
                buttonSave.addActionListener((e) -> {
                    this.config.ticketValidity = (Integer)this.ticketValidity.getValue();
                    this.config.outputDirectory = this.ticketOutput.getText();
                    this.config.VAT = (Double)this.ticketVAT.getValue();
                    this.config.ticketTemplate = this.ticketTemplate.getText();
                    this.config.ticketBackground = this.ticketBackground.getText();
                    this.config.ticketNr = (Integer)this.ticketIssued.getValue();
                    if (this.appearanceModeGraphics.isSelected())
                        this.config.uiMode = UIMode.GRAPHICS;
                    if (this.appearanceModeText.isSelected())
                        this.config.uiMode = UIMode.TEXT;
                    this.config.uiTheme = this.themeMap.get((String)this.appearanceTheme.getSelectedItem());
                    this.config.saveToFile();
                    WindowUIDialog dialog = new WindowUIDialog(
                            "Uložit nastavení",
                            "Nastavení bylo úspěšně uloženo." + System.lineSeparator() +
                            "Některé změny se projeví až při opětovném spuštění aplikace." + System.lineSeparator() + 
                            "Pokud je to možné, ukončete program a znovu jej zapněte.",
                            WindowUIDialogType.INFO,
                            WindowUIButtonType.OK
                    );
                    dialog.showDialog();
                    this.dispose();
                });
                buttonsPanel.add(buttonSave);
            //</editor-fold>
        //</editor-fold>
    }
}
