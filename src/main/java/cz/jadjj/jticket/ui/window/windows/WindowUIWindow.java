package cz.jadjj.jticket.ui.window.windows;

import cz.jadjj.jticket.ui.window.WindowUI;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

/**
 * Class abstracting all common attributes to all windows
 * @author jadjj
 */
public abstract class WindowUIWindow extends JFrame
{
    /**
     * Constant of one second (in milliseconds)
     */
    private final static int ONE_SECOND = 1000;
    
    /**
     * Top menu of window
     */
    protected JMenuBar topMenu;
    
    /**
     * File menu of top menu
     */
    protected JMenu fileMenu;
    
    /**
     * Close window menu item
     */
    protected JMenuItem closeItem;
    
    /**
     * Status bar of window
     */
    private JPanel statusBar;
    
    /**
     * Panel containing actual clock
     */
    private JPanel clockPanel;
    
    /**
     * Icon of clock
     */
    private JLabel clockIcon;
    
    /**
     * Text with actual time
     */
    private JLabel timeText;
    
    /**
     * Timer used to update clock
     */
    private Timer timer;
    
    /**
     * Panel with all components necessary to display actual date
     */
    private JPanel datePanel;
    
    /**
     * Label with icon of calendar
     */
    private JLabel calendarIcon;
    
    /**
     * Label with actual date
     */
    private JLabel dateText;
    
    /**
     * Default font used in status bar
     */
    private static final Font statusBarFont = new Font("Lucida Console", Font.PLAIN, 16);
    
    /**
     * Label with actual state
     */
    private JLabel stateText;
    
    /**
     * Action which will be performed on window exiting
     */
    private ActionListener exitAction = null;
    
    /**
     * Creates new window
     * @param title Title of window
     * @param icon Name of file with icon
     */
    public WindowUIWindow(String title, String icon)
    {
        super(title);
        super.setIconImage(new ImageIcon(WindowUI.UI_PATH + "/" + icon).getImage());
        super.setLayout(new BorderLayout());
        this.prepareTopMenu();
        this.prepareStatusBar();
        super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        super.setPreferredSize(new Dimension(800, 600));
        super.setLocation(
                ((Toolkit.getDefaultToolkit().getScreenSize().width - super.getPreferredSize().width) / 2),
                ((Toolkit.getDefaultToolkit().getScreenSize().height - super.getPreferredSize().height) / 2)

        );
    }
    
    /**
     * Sets handler for exit of window
     * @param al Handler for exit of window
     */
    public void setExitAction(ActionListener al)
    {
        this.exitAction = al;
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                al.actionPerformed(null);
            }
        });
    }
    
    /**
     * Prepares top menu of window
     */
    protected void prepareTopMenu()
    {
        this.topMenu = new JMenuBar();
        
        // File menu
        this.fileMenu = new JMenu();
        this.fileMenu.setText("Soubor");
        this.fileMenu.setIcon(new ImageIcon(WindowUI.UI_PATH + "/file.png"));
        this.fileMenu.setMnemonic(KeyEvent.VK_S);
        this.topMenu.add(this.fileMenu);
        
        // Close window
        this.closeItem = new JMenuItem();
        this.closeItem.setText("Zavřít");
        this.closeItem.setIcon(new ImageIcon(WindowUI.UI_PATH + "/close.png"));
        this.closeItem.setMnemonic(KeyEvent.VK_Z);
        if (this.exitAction != null)
        {
            this.closeItem.addActionListener(this.exitAction);
        }
        else
        {
            this.closeItem.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    closeWindow();
                }
            });
        }
        
        this.fileMenu.add(this.closeItem);
        
        this.getContentPane().add(this.topMenu, BorderLayout.NORTH);
    }
    
    /**
     * Prepares status bar of window
     */
    private void prepareStatusBar()
    {
        // Status bar
        this.statusBar = new JPanel();
        this.statusBar.setLayout(new GridLayout(1, 3, 5, 0));
        this.statusBar.setBorder(new BevelBorder(BevelBorder.LOWERED));
        this.getContentPane().add(this.statusBar, BorderLayout.SOUTH);
        
        // Date panel
        this.datePanel = new JPanel();
        this.datePanel.setLayout(new FlowLayout());
        this.statusBar.add(this.datePanel);
        
        // Calendar icon
        this.calendarIcon = new JLabel();
        this.calendarIcon.setIcon(new ImageIcon(WindowUI.UI_PATH + "/calendar.png"));
        this.datePanel.add(this.calendarIcon);
        
        // Date text
        this.dateText = new JLabel();
        this.datePanel.add(this.dateText);
        this.dateText.setFont(WindowUIWindow.statusBarFont);
        this.dateText.setText("--.--.----");
        
        // Clock panel
        this.clockPanel = new JPanel();
        this.clockPanel.setLayout(new FlowLayout());
        this.statusBar.add(this.clockPanel);
        
        // Clock icon
        this.clockIcon = new JLabel();
        this.clockIcon.setIcon(new ImageIcon(WindowUI.UI_PATH + "/clock.png"));
        this.clockPanel.add(this.clockIcon);   
        
        // Time text
        this.timeText = new JLabel();
        this.clockPanel.add(this.timeText);
        this.timeText.setFont(WindowUIWindow.statusBarFont);
        this.timeText.setText("--:--:--");
        
        // Timer
        this.timer = new Timer(ONE_SECOND, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                updateClock();
            }
        });
        this.timer.start();
        
        // State text
        this.stateText = new JLabel();
        this.stateText.setFont(WindowUIWindow.statusBarFont);
        this.stateText.setText("Připraven");
        this.stateText.setToolTipText(this.stateText.getText());
        this.statusBar.add(this.stateText, 0);
    }
    
    /**
     * Sets actual state
     * @param state New state
     */
    protected void setState(String state)
    {
        this.stateText.setText(state);
        this.stateText.setToolTipText(state);
    }
    
    /**
     * Closes window
     */
    private void closeWindow()
    {
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
    
    /**
     * Updates text of date and time
     */
    private void updateClock()
    {
        LocalDateTime now = LocalDateTime.now();
        String timeFormat = now.getSecond() % 2 == 0 ?
                "%02d:%02d:%02d" :
                "%02d %02d %02d";
        this.timeText.setText(String.format(timeFormat, now.getHour(), now.getMinute(), now.getSecond()));
        this.dateText.setText(String.format(
                "%02d.%02d.%04d",
                now.getDayOfMonth(),
                now.getMonthValue(),
                now.getYear()
        ));
    }
}