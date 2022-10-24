package cz.jadjj.jticket.ui.window.windows;

import cz.jadjj.jticket.jTicket;
import cz.jadjj.jticket.ui.window.WindowUI;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.event.HyperlinkEvent;

/**
 * Panel with content of "about" window
 * @author Jiri Skoda <skodaji1@uhk.cz.cz>
 */
public class WindowUIAboutWindow extends JPanel
{
    
    /**
     * Defines space between components
     */
    private static final int MARGIN = 5;
    
    /**
     * Defines space inside components
     */
    private static final int PADDING = 10;
    
    /**
     * Creates content of "about" window
     */
    public WindowUIAboutWindow()
    {
        super.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.initializeComponents();
    }
    
    /**
     * Initializes components of panel with content of "about" window
     */
    private void initializeComponents()
    {
        
        //<editor-fold defaultstate="collapsed" desc="Content of panel">
        super.setBorder(BorderFactory.createEmptyBorder(
                WindowUIAboutWindow.PADDING,
                WindowUIAboutWindow.PADDING,
                WindowUIAboutWindow.PADDING,
                WindowUIAboutWindow.PADDING
        ));
            //<editor-fold defaultstate="collapsed" desc="Header of panel">
            JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 
                    WindowUIAboutWindow.MARGIN,
                    WindowUIAboutWindow.MARGIN
            ));
            JLabel icon = new JLabel();
            icon.setIcon(new ImageIcon(WindowUI.UI_PATH + "/default.png"));
            header.add(icon);
            JLabel jticket = new JLabel("jTicket");
            jticket.setFont(jticket.getFont().deriveFont((float) 32));
            header.add(jticket);
            super.add(header);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Information about release">
            JPanel release = new JPanel(new GridLayout(3, 2,
                    WindowUIAboutWindow.MARGIN,
                    WindowUIAboutWindow.MARGIN
            ));
            release.setBorder(BorderFactory.createEmptyBorder(
                WindowUIAboutWindow.PADDING,
                WindowUIAboutWindow.PADDING,
                WindowUIAboutWindow.PADDING,
                WindowUIAboutWindow.PADDING
            ));
            JLabel versionText = new JLabel("Verze");
            versionText.setFont(versionText.getFont().deriveFont(Font.BOLD));
            release.add(versionText);
            release.add(new JLabel(jTicket.VERSION));
            JLabel dateText = new JLabel("Datum sestavení");
            dateText.setFont(dateText.getFont().deriveFont(Font.BOLD));
            release.add(dateText);
            release.add(new JLabel(jTicket.RELEASE_DATE));
            JLabel configText = new JLabel("Soubor konfigurace");
            configText.setFont(configText.getFont().deriveFont(Font.BOLD));
            release.add(configText);
            JButton configVal = new JButton(new File(jTicket.CONFIG_FILE).getAbsolutePath());
            configVal.setBorderPainted(false);
            configVal.setOpaque(false);
            configVal.setHorizontalAlignment(SwingConstants.LEFT);
            configVal.setForeground(new Color(33, 161, 255));
            configVal.setBorder(null);
            Font font = configVal.getFont();
            Map attributes = font.getAttributes();
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            configVal.setFont(font.deriveFont(attributes));
            configVal.addActionListener((e) -> {
                try
                {        
                    Desktop.getDesktop().open(new File(jTicket.CONFIG_FILE));
                }
                catch (IOException ex)
                {
                    WindowUI.handleException(ex);
                }
            });
            release.add(configVal);
            super.add(release);
            //</editor-fold>
            //<editor-fold defaultstate="collapsed" desc="Third party">
                JLabel thirdPartyLabel = new JLabel("Použité zdroje třetích stran");
                thirdPartyLabel.setFont(dateText.getFont().deriveFont(Font.BOLD));
                super.add(thirdPartyLabel);
                JPanel thirdParty = new JPanel(new GridLayout(1, 1));
                JTextArea thirdPartyText = new JTextArea();
                thirdPartyText.setText(
                "Icons by icons8.com" + System.lineSeparator() +
                "https://icons8.com"  + System.lineSeparator() +
                System.lineSeparator() +
                "Piano Chord.wav" + System.lineSeparator() +
                "kelsey_w" + System.lineSeparator() +
                "CC BY 3.0" + System.lineSeparator() +
                "https://freesound.org/people/kelsey_w/sounds/467049/" + System.lineSeparator() +
                System.lineSeparator() +
                "Dissonant Piano Stinger.mp3" + System.lineSeparator() +
                "XfiXy8" + System.lineSeparator() +
                "CC0 1.0" + System.lineSeparator() +
                "https://freesound.org/people/XfiXy8/sounds/467289/" + System.lineSeparator() +
                System.lineSeparator() +
                "05706 dinging interface sounds.wav" + System.lineSeparator() +
                "Robinhood76" + System.lineSeparator() +
                "CC BY-NC 4.0" + System.lineSeparator() +
                "https://freesound.org/people/Robinhood76/sounds/269259/" + System.lineSeparator() +
                System.lineSeparator() +
                "Windows 98 Icons" + System.lineSeparator() + 
                "Alex Meub" + System.lineSeparator() + 
                "https://win98icons.alexmeub.com" + System.lineSeparator() +
                System.lineSeparator()
                );
                thirdPartyText.setEditable(false);
                thirdPartyText.setFont(thirdPartyText.getFont().deriveFont((float)10));
                JScrollPane thirdPartyScroll = new JScrollPane(thirdPartyText);
                thirdParty.add(thirdPartyScroll);
                super.add(thirdParty);
            //</editor-fold>
        //</editor-fold>
    }
    
    /**
     * Gets window with information about program
     * @return Window with information about program
     */
    public static JDialog getWindow()
    {
        JDialog reti = new JDialog(null, "jTicket - O programu", java.awt.Dialog.ModalityType.TOOLKIT_MODAL);
        reti.setIconImage(new ImageIcon(WindowUI.UI_PATH + "/" + "about-s.png").getImage());
        reti.setModal(true);
        reti.setResizable(false);
        reti.setLayout(new GridLayout(1, 1));
        reti.getContentPane().add(new WindowUIAboutWindow());
        reti.setSize(400, 500);
        reti.setLocation(
                ((Toolkit.getDefaultToolkit().getScreenSize().width - reti.getSize().width) / 2),
                ((Toolkit.getDefaultToolkit().getScreenSize().height - reti.getSize().height) / 2)
        );
        return reti;
    }
}
