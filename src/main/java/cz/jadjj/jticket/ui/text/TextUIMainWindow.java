package cz.jadjj.jticket.ui.text;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Class representing main window for text user interface
 * @author jadjj
 */
public class TextUIMainWindow extends JFrame
{
    
    /**
     * Constant of one second;
     */
    private final static int ONE_SECOND = 1000;
    
    /**
     * Text area representing output console
     */
    private final JEditorPane out;
    
    /**
     * Scroll bar for output text area
     */
    private final JScrollPane outScrollBar;
    
    /**
     * Text area representing input console
     */
    private final JTextPane in;
    
    /**
     * Panel containing input controls
     */
    private final JPanel input;
    
    /**
     * Text area containing help
     */
    private final JTextPane help;
    
    /**
     * Scroll bar for input text area
     */
    private final JScrollPane inScrollBar;
    
    /**
     * Label showing name of program
     */
    private final JLabel nameLabel;
    
    /**
     * Label showing date and time
     */
    private final JLabel dateLabel;
    
    /**
     * Header of program
     */
    private final JPanel header;
    
    /**
     * Default font of window
     */
    private final Font defaultFont;
    
    /**
     * Content of the window
     */
    private final JPanel content;
    
    /**
     * Timer for updating current date and time
     */
    private final Timer timer;
    
    /**
     * Mode of actually entered commands
     */
    private String commandMode;
    
    /**
     * Panel which can only display inputs
     */
    private final JPanel inputViews;
    
    /**
     * Text field which accepts commands
     */
    private final JTextField command;
    
    /**
     * Panel containing command line
     */
    private final JPanel commandLine;
    
    /**
     * Prefix of command
     */
    private final JLabel commandPrefix;
    
    /**
     * HTML string which will be added before any HTML document
     */
    private final String defaultHTML = "<style type='text/css'>*{font-family: 'Lucida Console'; color: white; background-color: black;}</style>";
    
    /**
     * Actually displayed help to the commands
     */
    private ITextUIHelp[] actualHelp;
    
    /**
     * Controller of window
     */
    private TextUIController controller = null;
    
    /**
     * Flag, whether window should check input or not
     */
    private boolean strict = false;

    
    public TextUIMainWindow()
    {
        super("JTicket");
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(800, 600));        
        this.commandMode = "";
        
        this.defaultFont = new Font("Lucida Console", Font.PLAIN, 18);
        
        this.content = new JPanel(new GridLayout(2, 1));
        this.getContentPane().add(this.content, BorderLayout.CENTER);
        
        this.header = new JPanel(new BorderLayout());
        this.header.setForeground(Color.BLACK);
        this.header.setBackground(Color.LIGHT_GRAY);
        this.header.setFont(this.defaultFont);
        this.getContentPane().add(this.header, BorderLayout.PAGE_START);
        
        this.nameLabel = new JLabel("JTicket");
        this.nameLabel.setForeground(Color.BLACK);
        this.nameLabel.setBackground(Color.LIGHT_GRAY);
        this.nameLabel.setFont(this.defaultFont);
        this.header.add(this.nameLabel, BorderLayout.LINE_START);
        
        this.dateLabel = new JLabel("{date placeholder}");
        this.dateLabel.setForeground(Color.BLACK);
        this.dateLabel.setBackground(Color.LIGHT_GRAY);
        this.dateLabel.setFont(this.defaultFont);
        this.header.add(this.dateLabel, BorderLayout.LINE_END);
        
        this.out = new JEditorPane();
        this.content.add(this.out);
        this.out.setBackground(Color.BLACK);
        this.out.setForeground(Color.LIGHT_GRAY);
        this.out.setFont(this.defaultFont);
        this.out.setEditable(false);
        this.out.setContentType("text/html");
        
        this.outScrollBar = new JScrollPane(this.out, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.content.add(this.outScrollBar);
        this.outScrollBar.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        
        this.inputViews  = new JPanel(new GridLayout(2,1));
        
        this.input = new JPanel(new BorderLayout());
        this.content.add(this.input);
        
        this.help = new JTextPane();
        this.help.setBackground(Color.BLACK);
        this.help.setForeground(Color.LIGHT_GRAY);
        this.help.setFont(this.defaultFont);
        this.help.setEditable(false);        
        this.inputViews.add(this.help);
        
        this.in = new JTextPane();
        this.in.setBackground(Color.BLACK);
        this.in.setForeground(Color.LIGHT_GRAY);
        this.in.setFont(this.defaultFont);   
        this.in.setEditable(false);
        this.in.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        this.inputViews.add(this.in);        
        
        this.inScrollBar = new JScrollPane(this.in, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.inputViews.add(this.inScrollBar);
        this.inScrollBar.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        
        this.input.add(this.inputViews, BorderLayout.CENTER);
        
        this.commandLine = new JPanel();
        this.commandLine.setLayout(new BoxLayout(this.commandLine, BoxLayout.X_AXIS));
        this.commandLine.setBackground(Color.BLACK);
        this.commandLine.setForeground(Color.LIGHT_GRAY);
        this.commandLine.setFont(this.defaultFont);
        this.input.add(this.commandLine, BorderLayout.PAGE_END);
        
        this.commandPrefix = new JLabel();
        this.commandPrefix.setBackground(Color.BLACK);
        this.commandPrefix.setForeground(Color.LIGHT_GRAY);
        this.commandPrefix.setFont(this.defaultFont);
        this.commandPrefix.setText(this.commandMode + "> ");
        this.commandLine.add(this.commandPrefix);
        
        this.command = new JTextField("cmd");
        this.command.setText("");
        this.command.setBackground(Color.BLACK);
        this.command.setForeground(Color.LIGHT_GRAY);
        this.command.setFont(this.defaultFont);
        this.command.setBorder(BorderFactory.createEmptyBorder());
        this.command.setBorder(BorderFactory.createCompoundBorder(
        command.getBorder(), 
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        this.commandLine.add(this.command);
        this.command.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                handleInput();
            }
        });
        
        this.timer = new Timer(ONE_SECOND, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt)
            {
                String toDisplay = "";
                Date date = new Date();
                toDisplay += (date.getDate() < 10 ? "0" + date.getDate(): date.getDate());
                toDisplay += ".";
                toDisplay += (date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1): (date.getMonth() + 1));
                toDisplay += ".";
                toDisplay += date.getYear() + 1900;
                toDisplay += " ";
                toDisplay += (date.getHours() < 10 ? "0" + date.getHours(): date.getHours());
                toDisplay += (date.getSeconds() % 2 == 0 ? ":" : " ");
                toDisplay += (date.getMinutes() < 10 ? "0" + date.getMinutes(): date.getMinutes());
                toDisplay += (date.getSeconds() % 2 == 0 ? ":" : " ");
                toDisplay += (date.getSeconds() < 10 ? "0" + date.getSeconds(): date.getSeconds());
                dateLabel.setText(toDisplay);
            }
        });
        this.timer.start();
        this.addWindowListener(new WindowAdapter(){
            @Override
            public void windowOpened(WindowEvent evt)
            {
                command.requestFocus();
            }
        });
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    /**
     * Sets controller of window
     * @param controller Controller of window
     */
    public void setController(TextUIController controller)
    {
        this.controller = controller;
    }
    
    /**
     * Function which handle input from command line
     */
    private void handleInput()
    {
        String cmd = this.command.getText().trim();
        
        SimpleAttributeSet def = new SimpleAttributeSet();
        StyleConstants.setItalic(def, false);
        StyleConstants.setBold(def, false);
        StyleConstants.setBackground(def, Color.BLACK);
        StyleConstants.setForeground(def, Color.LIGHT_GRAY);
        StyleConstants.setFontFamily(def, this.defaultFont.getFamily());
        StyleConstants.setFontSize(def, this.defaultFont.getSize());
        
        SimpleAttributeSet err = new SimpleAttributeSet(def);
        StyleConstants.setForeground(err, Color.RED);
        
        Document doc = this.in.getStyledDocument();
        try
        {
            doc.insertString(doc.getLength(), "\n" + this.commandMode + "> " + cmd, def);
        }
        catch (BadLocationException ex)
        {
            Logger.getLogger(TextUIMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.command.setText("");
        
        // Check, if comand is allowed
        if (this.strict == true)
        {
            boolean allowed = false;
            for (ITextUIHelp h: this.actualHelp)
            {
                if (h.getCommand().toLowerCase() == null ? cmd.toLowerCase() == null : h.getCommand().toLowerCase().equals(cmd.toLowerCase()))
                {
                    allowed = true;
                    break;
                }
            }
            if (allowed == false && !"".equals(cmd))
            {
                try
                {
                    doc.insertString(doc.getLength(), "\n[!] Neznamy prikaz '" + cmd + "'!", err);
                }
                catch (BadLocationException ex)
                {
                    Logger.getLogger(TextUIMainWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if (allowed == true)
            {
                this.controller.handleCommand(cmd);
            }
        }
        else
        {
            this.controller.handleCommand(cmd);
        }
        this.inScrollBar.getVerticalScrollBar().setValue(this.inScrollBar.getVerticalScrollBar().getMaximum());
        
    }
    
    /**
     * Shows screen in output window
     * @param screen Screen which will be shown in output
     */
    public void showScreen(ITextUIScreen screen)
    {
        String C = screen.getContent().replace("<head>", "<head>" + this.defaultHTML).replace("null", "");
        this.out.setText(C); 
        this.out.setSelectionStart(0);
        this.out.setSelectionEnd(0);
    }
    
    /**
     * Shows available commands
     * @param help List of available commands and help to them
     */
    public void showHelp(ITextUIHelp[] help)
    {
        this.actualHelp = help;
        this.help.setText("");
        
        SimpleAttributeSet def = new SimpleAttributeSet();
        StyleConstants.setItalic(def, false);
        StyleConstants.setBold(def, false);
        StyleConstants.setBackground(def, Color.BLACK);
        StyleConstants.setForeground(def, Color.LIGHT_GRAY);
        StyleConstants.setFontFamily(def, this.defaultFont.getFamily());
        StyleConstants.setFontSize(def, this.defaultFont.getSize());
        
        
        // Display each help
        SimpleAttributeSet aS = new SimpleAttributeSet(def);
        Document doc = this.help.getStyledDocument();
        for (ITextUIHelp h: help)
        {
            StyleConstants.setForeground(aS, h.getColor());
            try
            {
                doc.insertString(doc.getLength(), "'", def);
                doc.insertString(doc.getLength(), h.getCommand(), aS);
                doc.insertString(doc.getLength(), "' - " + h.getHelp() + " \n", def);
                
            }
            catch (BadLocationException ex)
            {
                Logger.getLogger(TextUIMainWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Sets, whether window should check input or not
     * @param strict Flag, whether window should check input or not
     */
    public void setStrict(boolean strict)
    {
        this.strict = strict;
    }
    
    /**
     * Shows error input message
     * @param message Message which will be shown
     */
    public void showInputError(String message)
    {
        SimpleAttributeSet def = new SimpleAttributeSet();
        StyleConstants.setItalic(def, false);
        StyleConstants.setBold(def, false);
        StyleConstants.setBackground(def, Color.BLACK);
        StyleConstants.setForeground(def, Color.LIGHT_GRAY);
        StyleConstants.setFontFamily(def, this.defaultFont.getFamily());
        StyleConstants.setFontSize(def, this.defaultFont.getSize());
        
        SimpleAttributeSet err = new SimpleAttributeSet(def);
        StyleConstants.setForeground(err, Color.RED);
        
        Document doc = this.in.getStyledDocument();
        try
        {
            doc.insertString(doc.getLength(), "\n[!] " + message, err);
        }
        catch (BadLocationException ex)
        {
            Logger.getLogger(TextUIMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Shows success input message
     * @param message Message which will be shown
     */
    public void showInputSuccess(String message)
    {
        SimpleAttributeSet def = new SimpleAttributeSet();
        StyleConstants.setItalic(def, false);
        StyleConstants.setBold(def, false);
        StyleConstants.setBackground(def, Color.BLACK);
        StyleConstants.setForeground(def, Color.LIGHT_GRAY);
        StyleConstants.setFontFamily(def, this.defaultFont.getFamily());
        StyleConstants.setFontSize(def, this.defaultFont.getSize());
        
        SimpleAttributeSet success = new SimpleAttributeSet(def);
        StyleConstants.setForeground(success, Color.GREEN);
        
        Document doc = this.in.getStyledDocument();
        try
        {
            doc.insertString(doc.getLength(), "\n>>> " + message, success);
        }
        catch (BadLocationException ex)
        {
            Logger.getLogger(TextUIMainWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sets command prefix to command line
     * @param commandPrefix Prefix which will be displayed in command line
     */
    public void setCommandMode(String commandPrefix)
    {
        this.commandMode = commandPrefix;
        this.commandPrefix.setText(this.commandMode + "> ");
    }
    
    /**
     * Sets focus on command line
     */
    public void setFocusOnCommandLine()
    {
        this.command.requestFocus();
    }
}
