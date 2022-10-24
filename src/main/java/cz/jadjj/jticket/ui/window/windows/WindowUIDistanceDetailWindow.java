package cz.jadjj.jticket.ui.window.windows;

import cz.jadjj.jticket.data.Station;
import cz.jadjj.jticket.ui.window.WindowUI;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIButtonType;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialog;
import cz.jadjj.jticket.ui.window.windows.dialogs.WindowUIDialogType;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Window with detail about distance between stations
 * @author jadjj
 */
public class WindowUIDistanceDetailWindow extends JDialog
{
    /**
     * Origin station of distance
     */
    private Station origin;
    
    /**
     * Destination station of distance
     */
    private Station destination;
    
    /**
     * Distance between stations
     */
    private int distance;
    
    /**
     * Flag, whether OK button has been clicked
     */
    private boolean okClicked = false;
    
    /**
     * Creates new window with detail about distance between stations
     * @param origin Origin station of distance
     * @param destination Destination station of distance
     * @param distance Distance between stations
     */
    public WindowUIDistanceDetailWindow(Station origin, Station destination, int distance)
    {
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
        this.initializeComponents();
        super.setResizable(false);
        super.setLocation(
                ((Toolkit.getDefaultToolkit().getScreenSize().width - super.getWidth()) / 2),
                ((Toolkit.getDefaultToolkit().getScreenSize().height - super.getHeight()) / 2)

        );
        super.setModal(true);
        super.setIconImage(new ImageIcon(WindowUI.UI_PATH + "/detail-s.png").getImage());
        super.setTitle("Detail vzdálenosti mezi stanicemi");
    }
    
    /**
     * Initializes components displayed in window
     */
    private void initializeComponents()
    {
        super.setLayout(new GridBagLayout());
        
        final int PADDING = 1;
        
        //<editor-fold defaultstate="collapsed" desc="Origin label">
        GridBagConstraints originLabelConstraints = new GridBagConstraints();
        originLabelConstraints.gridx = 0;
        originLabelConstraints.gridy = 0;
        originLabelConstraints.gridheight = 1;
        originLabelConstraints.gridwidth = 1;
        originLabelConstraints.insets = new Insets(10, 10, PADDING, 20);
        originLabelConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(new JLabel("Výchozí stanice"), originLabelConstraints);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Origin station">
        GridBagConstraints originStationConstraints = new GridBagConstraints();
        originStationConstraints.gridx = 1;
        originStationConstraints.gridy = 0;
        originStationConstraints.gridheight = 1;
        originStationConstraints.gridwidth = 2;
        originStationConstraints.insets = new Insets(10, PADDING, PADDING, 10);
        originStationConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(new JLabel(this.origin.getName()), originStationConstraints);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Destination label">
        GridBagConstraints destinationLabelConstraints = new GridBagConstraints();
        destinationLabelConstraints.gridx = 0;
        destinationLabelConstraints.gridy = 1;
        destinationLabelConstraints.gridheight = 1;
        destinationLabelConstraints.gridwidth = 1;
        destinationLabelConstraints.insets = new Insets(PADDING, 10, PADDING, 20);
        destinationLabelConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(new JLabel("Cílová stanice"), destinationLabelConstraints);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Destination station">
        GridBagConstraints destinationStationConstraints = new GridBagConstraints();
        destinationStationConstraints.gridx = 1;
        destinationStationConstraints.gridy = 1;
        destinationStationConstraints.gridheight = 1;
        destinationStationConstraints.gridwidth = 2;
        destinationStationConstraints.insets = new Insets(PADDING, PADDING, PADDING, 10);
        destinationStationConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(new JLabel(this.destination.getName()), destinationStationConstraints);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Distance label">
        GridBagConstraints distanceLabelConstraints = new GridBagConstraints();
        distanceLabelConstraints.gridx = 0;
        distanceLabelConstraints.gridy = 2;
        distanceLabelConstraints.gridheight = 1;
        distanceLabelConstraints.gridwidth = 1;
        distanceLabelConstraints.insets = new Insets(PADDING, 10, PADDING, 20);
        distanceLabelConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(new JLabel("Vzdálenost"), distanceLabelConstraints);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Distance spinner">
        GridBagConstraints distanceSpinnerConstraints = new GridBagConstraints();
        distanceSpinnerConstraints.gridx = 1;
        distanceSpinnerConstraints.gridy = 2;
        distanceSpinnerConstraints.gridheight = 1;
        distanceSpinnerConstraints.gridwidth = 1;
        distanceSpinnerConstraints.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
        distanceSpinnerConstraints.anchor = GridBagConstraints.WEST;
        JSpinner distanceSpinner = new JSpinner(new SpinnerNumberModel(this.distance, 0, Integer.MAX_VALUE, 1));
        super.getContentPane().add(distanceSpinner, distanceSpinnerConstraints);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="'KM' label">
        GridBagConstraints KMLabelConstraints = new GridBagConstraints();
        KMLabelConstraints.gridx = 2;
        KMLabelConstraints.gridy = 2;
        KMLabelConstraints.gridheight = 1;
        KMLabelConstraints.gridwidth = 1;
        KMLabelConstraints.insets = new Insets(PADDING, PADDING, PADDING, 10);
        KMLabelConstraints.anchor = GridBagConstraints.WEST;
        super.getContentPane().add(new JLabel("km"), KMLabelConstraints);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Buttons">
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 2));
        GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
        buttonPanelConstraints.gridx = 0;
        buttonPanelConstraints.gridy = 3;
        buttonPanelConstraints.gridheight = 1;
        buttonPanelConstraints.gridwidth = 3;
        buttonPanelConstraints.insets = new Insets(20, 10, 10, 10);
        buttonPanelConstraints.anchor = GridBagConstraints.WEST;
        buttonPanelConstraints.fill = GridBagConstraints.BOTH;
        //<editor-fold defaultstate="collapsed" desc="OK button">
        JButton okButton = new JButton();
        okButton.setIcon(new ImageIcon(WindowUI.UI_PATH + "/ok.png"));
        okButton.setText("Budiž");
        okButton.addActionListener((e) -> {
            int entered = -1;
            try
            {
                entered = Integer.parseInt(distanceSpinner.getValue().toString());
            }
            catch (NumberFormatException ex)
            {
                WindowUIDialog err = new WindowUIDialog(
                        "Neplatný vstup",
                        "Zadaná hodnota pro vzdálenost mezi stanicemi je neplatná." + System.lineSeparator() + 
                        "Vzdálenost musí být celé nezáporné číslo.",
                        WindowUIDialogType.ERROR,
                        WindowUIButtonType.OK
                );
                err.showDialog();
            }
            if (entered >= 0)
            {
                this.distance = entered;
                this.okClicked = true;
                this.dispose();
            }            
        });
        buttonPanel.add(okButton);
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Cancel button">
        JButton cancelButton = new JButton();
        cancelButton.setIcon(new ImageIcon(WindowUI.UI_PATH + "/close.png"));
        cancelButton.setText("Zrušit");
        cancelButton.addActionListener((e) -> {
            this.okClicked = false;
            this.dispose();
        });
        buttonPanel.add(cancelButton);
        //</editor-fold>
        super.getContentPane().add(buttonPanel, buttonPanelConstraints);
        //</editor-fold>
        super.pack();        
    }
    
    /**
     * Gets flag, whether user has clicked on 'OK' button or not
     * @return <code>TRUE</code> if user has clicked on 'OK' button
     *         <code>FALSE</code> otherwise
     */
    public boolean getOKClicked()
    {
        return this.okClicked;
    }
    
    /**
     * Gets distance entered into dialog
     * @return Distance entered into dialog
     */
    public int getDistance()
    {
        return this.distance;
    }
}
