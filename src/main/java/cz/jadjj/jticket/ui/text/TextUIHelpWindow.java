package cz.jadjj.jticket.ui.text;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

/**
 * Class representing helper window of textual user interface
 * @author jadjj
 */
public class TextUIHelpWindow extends JFrame
{
    /**
     * Content of help window
     */
    private final JTextPane content;
    
    /**
     * Scroll bar for content
     */
    private final JScrollPane scrollBar;
    
    /**
     * Screen containing list of all available stations
     */
    private final TextUIHTMLTemplateScreen stations; 
    
    /**
     * Screen containing list of all available tariffs
     */
    private final TextUIHTMLTemplateScreen tariffs;
    
    /**
     * HTML string which will be added before any HTML document
     */
    private final String defaultHTML = "<style type='text/css'>*{font-family: 'Lucida Console'; color: white; background-color: black;}</style>";
    
    /**
     * Creates new window with help needed when selling tickets
     */
    public TextUIHelpWindow()
    {
        super("JTicket - Help");
        this.setLayout(new GridLayout());
        this.setPreferredSize(new Dimension(400, 600));
        
        this.content = new JTextPane();
        this.content.setEditable(false);
        this.content.setContentType("text/html");
        this.content.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        this.getContentPane().add(this.content);
        
        this.scrollBar = new JScrollPane(this.content, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        this.getContentPane().add(this.scrollBar);
        
        this.stations = new TextUIHTMLTemplateScreen("help-stations", "help-stations.html");
        this.tariffs = new TextUIHTMLTemplateScreen("help-tariffs", "help-tariffs.html");
        
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
    
    /**
     * Shows tariffs in window
     */
    public void showTariffs()
    {
        Map<String, String> data = new HashMap<>();
        data.put("tariffs_tr", cz.jadjj.jticket.data.Tariffs.GetInstance().GenerateTariffsTableRows());
        this.tariffs.setContent(data);
        String toDisplay = this.tariffs.getContent().replace("<head>", "<head>" + this.defaultHTML).replace("null", "");
        this.content.setText(toDisplay);
        this.content.setSelectionStart(0);
        this.content.setSelectionEnd(0);
    }
    
    /**
     * Shows stations in window
     */
    public void showStations()
    {
        Map<String, String> data = new HashMap<>();
        data.put("stations_tr", cz.jadjj.jticket.data.Stations.GetInstance().GenerateTableRows());
        this.stations.setContent(data);
        String toDisplay = this.stations.getContent().replace("<head>", "<head>" + this.defaultHTML).replace("null", "");
        this.content.setText(toDisplay);
        this.content.setSelectionStart(0);
        this.content.setSelectionEnd(0);
    }
}
