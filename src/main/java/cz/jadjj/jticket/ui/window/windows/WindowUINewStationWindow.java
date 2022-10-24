package cz.jadjj.jticket.ui.window.windows;

import cz.jadjj.jticket.ui.window.WindowUI;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Window with form to create new station
 * @author jadjj
 */
public class WindowUINewStationWindow extends JDialog
{
    /**
     * Textfield with abbreavation of station
     */
    private JTextField abbreavationTF;
    
    /**
     * Name of station
     */
    private JTextField nameTF;
    
    /**
     * Content of window
     */
    private JPanel content;
    
    /**
     * Panel with dialog buttons
     */
    private JPanel buttonPanel;
    
    /**
     * OK button
     */
    private JButton buttonOK;
    
    /**
     * Cancel button
     */
    private JButton buttonCancel;
    
    /**
     * Flag, whether OK button has been clicked
     */
    private boolean okClicked;
    
    
    /**
     * Creates new window for creating new station
     */
    public WindowUINewStationWindow()
    {
        super();
        super.setIconImage(new ImageIcon(WindowUI.UI_PATH + "/newitem.png").getImage());
        super.setTitle("jTicket - Nová stanice");
        super.setModal(true);
        this.initializeComponents();
        super.setPreferredSize(new Dimension(400, 200));
        super.pack();
        this.okClicked = false;
        super.setResizable(false);
        super.setLocation(
                ((Toolkit.getDefaultToolkit().getScreenSize().width - this.getPreferredSize().width) / 2),
                ((Toolkit.getDefaultToolkit().getScreenSize().height - this.getPreferredSize().height) / 2)

        );
    }
    
    /**
     * Initialises components in window
     */
    private void initializeComponents()
    {
        // Content of window
        this.content = new JPanel();
        this.setContentPane(this.content);
        this.content.setLayout(new BoxLayout(this.content, BoxLayout.Y_AXIS));
        
        // Label for abbreavation of station
        JPanel wrapper1 = new JPanel();
        wrapper1.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.content.add(wrapper1);
        wrapper1.add(new JLabel("Zkratka stanice"));
        
        // Textfield for abbreavation of station
        this.abbreavationTF = new JTextField();
        this.abbreavationTF.setFont(this.abbreavationTF.getFont().deriveFont(16.0f));
        this.content.add(this.abbreavationTF);
        
        // Label for name of station
        JPanel wrapper2 = new JPanel();
        wrapper2.setLayout(new FlowLayout(FlowLayout.LEADING));
        this.content.add(wrapper2);
        wrapper2.add(new JLabel("Název stanice"));
        
        // Textfield for name of station
        this.nameTF = new JTextField();
        this.nameTF.setFont(this.nameTF.getFont().deriveFont(16.0f));
        this.content.add(this.nameTF);
        
        // Panel with buttons
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        this.content.add(this.buttonPanel);
        
        // OK button
        this.buttonOK = new JButton();
        this.buttonOK.setIcon(new ImageIcon(WindowUI.UI_PATH + "/ok.png"));
        this.buttonOK.setText("Budiž");
        this.buttonOK.addActionListener((ActionEvent e) -> {
            if (nameTF.getText().trim().length() > 0 &&
                    abbreavationTF.getText().trim().length() > 0)
            {
                okClicked = true;
                closeDialog();
            }
        });
        this.buttonPanel.add(this.buttonOK);
        
        // Cancel button
        this.buttonCancel = new JButton();
        this.buttonCancel.setIcon(new ImageIcon(WindowUI.UI_PATH + "/close.png"));
        this.buttonCancel.setText("Zrušit");
        this.buttonCancel.addActionListener((ActionEvent e) -> {
            okClicked = false;
            closeDialog();
        });
        this.buttonPanel.add(this.buttonCancel);
        
    }
    
    /**
     * Closes dialog
     */
    private void closeDialog()
    {
        this.dispose();
    }
    
    /**
     * Gets name of station from dialog
     * @return Name of station from dialog
     */
    public String getStationName()
    {
        return this.nameTF.getText().trim();
    }
    
    /**
     * Gets abbreavation of station from dialog
     * @return Abbreavation of station from dialog
     */
    public String getStationAbbr()
    {
        return this.abbreavationTF.getText().trim().toUpperCase();
    }
    
    /**
     * Checks, whether button OK has been clicked
     * @return TRUE if button OK has been clicked, FALSE otherwise
     */
    public boolean clickedOK()
    {
        return this.okClicked;
    }
    
}
