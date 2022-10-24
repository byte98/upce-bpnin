package cz.jadjj.jticket.ui.window.windows.dialogs;

import cz.jadjj.jticket.ui.window.WindowUI;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author jadjj
 */
public class WindowUIDialog extends JDialog
{
    /**
     * Text displayed in OK button
     */
    private static final String BUTTON_OK = "OK";
    
    /**
     * Text displayed in YES button
     */
    private static final String BUTTON_YES = "Ano";
    
    /**
     * Text displayed in NO button
     */
    private static final String BUTTON_NO = "Ne";
    
    /**
     * Text displayed in CANCEL button
     */
    private static final String BUTTON_CANCEL = "Zrušit";
    
    /**
     * List with all buttons which will be displayed
     */
    private final List<WindowUIButtonType> buttons;
    
    /**
     * Type of dialog
     */
    private final WindowUIDialogType type;
    
    /**
     * Path to audio file
     */
    private String audioFile;
    
    /**
     * Path to file with icon
     */
    private String iconFile;
    
    /**
     * Path to file with displayed image
     */
    private String imageFile;
    
    /**
     * Text which will be displayed in dialog
     */
    private final String text;
    
    /**
     * Result of dialog
     */
    private WindowUIButtonType result;
    
    /**
     * Creates new dialog
     * @param title Title of dialog
     * @param body Body text of dialog
     * @param type Type of dialog
     * @param buttons Buttons which will be displayed in button
     */
    public WindowUIDialog(String title, String body, WindowUIDialogType type, WindowUIButtonType... buttons)
    {
        super.setTitle(title);
        this.text = body;
        this.type = type;
        this.buttons = new ArrayList<>();
        this.buttons.addAll(Arrays.asList(buttons));
        this.recognizeType();
        super.setModal(true);
        this.initializeComponents();
        super.setResizable(false);
        super.pack();
        super.setLocation(
                ((Toolkit.getDefaultToolkit().getScreenSize().width - super.getPreferredSize().width) / 2),
                ((Toolkit.getDefaultToolkit().getScreenSize().height - super.getPreferredSize().height) / 2)
        );
    }
    
    /**
     * Recognizes type of dialog and sets proper values to attributes
     */
    private void recognizeType()
    {
        switch(this.type)
        {
            case INFO:
                this.audioFile = WindowUI.UI_PATH + "/DING.WAV";
                this.iconFile = WindowUI.UI_PATH + "/info-s.png";
                this.imageFile = WindowUI.UI_PATH + "/info.png";
                break;
            case QUESTION:
                    this.audioFile = WindowUI.UI_PATH + "/DING.WAV";
                    this.iconFile = WindowUI.UI_PATH + "/question-s.png";
                    this.imageFile = WindowUI.UI_PATH + "/question.png";
                    break;
            case WARNING:
                    this.audioFile = WindowUI.UI_PATH + "/CHORD.WAV";
                    this.iconFile = WindowUI.UI_PATH + "/warning-s.png";
                    this.imageFile = WindowUI.UI_PATH + "/warning.png";
                    break;
            case ERROR:
                    this.audioFile = WindowUI.UI_PATH + "/CHORD.WAV";
                    this.iconFile = WindowUI.UI_PATH + "/error-s.png";
                    this.imageFile = WindowUI.UI_PATH + "/error.png";
                    break;
        }
    }
    
    /**
     * Initializes components
     */
    private void initializeComponents()
    {
        this.getContentPane().setLayout(new BorderLayout());
        this.setIconImage(new ImageIcon(this.iconFile).getImage());
        
        // Body of dialog        
        this.getContentPane().add(this.initializeBody(), BorderLayout.CENTER);
        
        // Dialog buttons
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));
        for(WindowUIButtonType btn: this.buttons)
        {
            buttons.add(this.createButton(btn));
        }
        this.getContentPane().add(buttons, BorderLayout.SOUTH);
        
    }
    
    /**
     * Initializes body of dialog
     * @return Panel containing body of dialog
     */
    protected JPanel initializeBody()
    {
        JPanel reti = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        reti.add(this.initializeIcon());
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        for (String line: this.text.split(System.lineSeparator()))
        {
            JLabel lineLabel = new JLabel(line);
            textPanel.add(lineLabel);
        }
        reti.add(textPanel);
        return reti;
    }
    
    /**
     * Initializes icon of dialog
     * @return Label with icon of dialog
     */
    protected JLabel initializeIcon()
    {
        JLabel reti = new JLabel();
        reti.setIcon(new ImageIcon(this.imageFile));
        return reti;
    }
    
    /**
     * Creates new button according to its type
     * @param type Type of button
     * @return New button with defined type
     */
    private JButton createButton(WindowUIButtonType type)
    {
        JButton reti = null;
        switch(type)
        {
            case OK:
                reti = new JButton(WindowUIDialog.BUTTON_OK);
                reti.addActionListener((e) -> {
                    this.result = WindowUIButtonType.OK;
                    this.dispose();
                });
                reti.setMnemonic(KeyEvent.VK_O);
                break;
            case CANCEL:
                reti = new JButton(WindowUIDialog.BUTTON_CANCEL);
                reti.setMnemonic(KeyEvent.VK_Z);
                reti.addActionListener((e) -> {
                    this.result = WindowUIButtonType.CANCEL;
                    this.dispose();
                });
                break;
            case YES:
                reti = new JButton(WindowUIDialog.BUTTON_YES);
                reti.setMnemonic(KeyEvent.VK_A);
                reti.addActionListener((e) -> {
                    this.result = WindowUIButtonType.YES;
                    this.dispose();
                });
                break;
            case NO:
                reti = new JButton(WindowUIDialog.BUTTON_NO);
                reti.setMnemonic(KeyEvent.VK_N);
                reti.addActionListener((e) -> {
                    this.result = WindowUIButtonType.NO;
                    this.dispose();
                });
                break;
        }
        return reti;
    }
    
    /**
     * Shows dialog
     */
    public void showDialog()
    {
        try
        {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(this.audioFile).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        }
        catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex)
        {
            Logger.getLogger(WindowUIDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setVisible(true);        
    }
    
    /**
     * Gets result of dialog
     * @return Button pressed as result of dialog
     */
    public WindowUIButtonType getResult()
    {
        return this.result;
    }
}
