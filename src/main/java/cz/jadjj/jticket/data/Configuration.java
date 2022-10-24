package cz.jadjj.jticket.data;

import cz.jadjj.jticket.ui.IUserInterface;
import cz.jadjj.jticket.ui.text.TextUI;
import cz.jadjj.jticket.ui.window.WindowUI;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing system configuration
 * @author jadjj
 */
public class Configuration
{
    /**
     * Instance of configuration class which will be instanced only once
     */
    private static Configuration instance = null;
    
    /**
     * Path to file which contains configuration
     */
    private final String filePath;
    
    /**
     * Path to file which contains background of tickets
     */
    public String ticketBackground;
    
    /**
     * Path to directory to which will be all tickets printed
     */
    public String outputDirectory;
    
    /**
     * Path to file which contains template of ticket
     */
    public String ticketTemplate;
    
    /**
     * Mode of user interface
     */
    public UIMode uiMode;
    
    /**
     * Name of used user interface theme
     */
    public String uiTheme;
    
    /**
     * Count of tickets issued
     */
    public int ticketNr;
    
    /**
     * Validity of ticket in hours
     */
    public int ticketValidity;
    
    /**
     * VAT rate in percents
     */
    public double VAT;
    
    /**
     * Creates new system configuration
     * @param filePath Path to file with configuration
     */
    private Configuration(String filePath)
    {
        this.filePath = filePath;
        this.loadFromFile();
    }
    
    /**
     * Saves configuration to file
     */
    public void saveToFile()
    {
        FileWriter writer = null;
        try
        {
            // First, read old lines
            List<String> oldLines = new ArrayList<String>();
            FileInputStream fstream = null;
            try
            {
                fstream = new FileInputStream(this.filePath);
                BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
                String strLine;
                while ((strLine = br.readLine()) != null)
                {
                    oldLines.add(strLine);
                }
                fstream.close();
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            }
            finally
            {
                try
                {
                    fstream.close();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            // Then, change content to new one
            List<String> newLines = new ArrayList<>();
            List<String> toSave = new ArrayList<>();
            toSave.add("TICKET_BACKGROUND");
            toSave.add("TICKET_DIRECTORY");
            toSave.add("UI_MODE");
            toSave.add("TICKET_TEMPLATE");
            toSave.add("TICKET_ISSUED");
            toSave.add("TICKET_VALIDITY");
            toSave.add("VAT");
            toSave.add("UI_THEME");
            for(String line: oldLines)
            {
                boolean added = false;
                if (line.startsWith("# ||                Last modification")) // Set last modification of file
                {
                    LocalDateTime now = LocalDateTime.now();
                    newLines.add(String.format(
                            "# ||                Last modification: %04d/%02d/%02d %02d:%02d             ||",
                            now.getYear(),
                            now.getMonthValue(),
                            now.getDayOfMonth(),
                            now.getHour(),
                            now.getMinute()
                            ));
                    added = true;
                }
                for (String val: toSave) // Check, if is not already saved
                {
                    
                    if (line.toUpperCase().startsWith(val))
                    {
                        StringBuilder sb = new StringBuilder();
                        sb.append(val);
                        sb.append("=");
                        switch(val)
                        {
                            case "TICKET_BACKGROUND": sb.append(this.ticketBackground); break;
                            case "TICKET_DIRECTORY" : sb.append(this.outputDirectory);  break;
                            case "TICKET_TEMPLATE"  : sb.append(this.ticketTemplate);   break;
                            case "TICKET_ISSUED"    : sb.append(this.ticketNr);         break;
                            case "TICKET_VALIDITY"  : sb.append(this.ticketValidity);   break;
                            case "VAT"              : sb.append(this.VAT);              break;
                            case "UI_THEME"         : sb.append(this.uiTheme);          break;
                            case "UI_MODE"          : switch(this.uiMode)
                            {
                                case TEXT: sb.append("TEXT");         break;
                                case GRAPHICS: sb.append("GRAPHICS"); break;
                            }
                            
                        }
                        newLines.add(sb.toString());
                        added = true;
                        toSave.remove(val);
                        break;
                    }
                }
                if (added == false)
                {
                    newLines.add(line);
                }
            }
            if (toSave.size() > 0)
            {
                for(String val: toSave)
                {
                    StringBuilder sb = new StringBuilder();
                    sb.append(val);
                    sb.append("=");
                    switch(val)
                    {
                        case "TICKET_BACKGROUND": sb.append(this.ticketBackground); break;
                        case "TICKET_DIRECTORY" : sb.append(this.outputDirectory);  break;
                        case "TICKET_TEMPLATE"  : sb.append(this.ticketTemplate);   break;
                        case "TICKET_ISSUED"    : sb.append(this.ticketNr);         break;
                        case "TICKET_VALIDITY"  : sb.append(this.ticketValidity);   break;
                        case "VAT"              : sb.append(this.VAT);              break;
                        case "UI_THEME"         : sb.append(this.uiTheme);          break;
                        case "UI_MODE"          : switch(this.uiMode)
                        {
                            case TEXT: sb.append("TEXT");         break;
                            case GRAPHICS: sb.append("GRAPHICS"); break;
                        }
                    }
                    newLines.add(sb.toString());
                }
            }
            // At the end, write output to file
            writer = new FileWriter(this.filePath);
            for(String str: newLines)
            {
                writer.write(str + System.lineSeparator());
            }
            writer.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                writer.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Loads configuration from file
     */
    private void loadFromFile()
    {
        // Read configuration from file
        Map<String, String> fileContent = this.readLines();
        this.outputDirectory = fileContent.get("TICKET_DIRECTORY");
        this.ticketBackground = fileContent.get("TICKET_BACKGROUND");
        this.ticketTemplate = fileContent.get("TICKET_TEMPLATE");
        this.ticketNr = Integer.parseInt(fileContent.get("TICKET_ISSUED"));
        this.ticketValidity = Integer.parseInt(fileContent.get("TICKET_VALIDITY"));
        this.VAT = Double.parseDouble(fileContent.get("VAT"));
        if (fileContent.get("UI_MODE") != null)
        {
            switch(fileContent.get("UI_MODE").toUpperCase())
            {
                case "TEXT": this.uiMode = UIMode.TEXT; break;
                case "GRAPHICS": this.uiMode = UIMode.GRAPHICS; break;
                default: this.uiMode = UIMode.TEXT; break;
            }
        }
        else
        {
            this.uiMode = UIMode.TEXT;
        }
        this.uiTheme = fileContent.get("UI_THEME");
    }
    
    /**
     * Gets instance of configuration class
     * @param filePath Path to file with configuration
     * @return Instance of configuration class
     */
    public static Configuration getInstance(String filePath)
    {
        if (Configuration.instance == null)
        {
            Configuration.instance = new Configuration(filePath);
        }
        return Configuration.instance;
    }
    
    /**
     * Reads configuration file
     * @return Lines with data from configuration file
     */
    private Map<String, String> readLines()
    {
        Map<String, String> reti = new HashMap<>();
        FileInputStream fstream = null;
        try
        {
            fstream = new FileInputStream(this.filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null)
            {
                String[] parts = strLine.split("=");
                if (parts.length == 2 && parts[0].startsWith("#") == false)
                {
                    reti.put(parts[0], parts[1]);
                }
            }
            fstream.close();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            try
            {
                fstream.close();
            }
            catch (IOException ex)
            {
                Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex, null);
            }
        }
        return reti;
    }
    
    /**
     * Gets handler of user interface defined in configuration
     * @return Handler of user interface defined in configuration
     */
    public IUserInterface getUI()
    {
        IUserInterface reti = null;
        switch (this.uiMode)
        {
            case TEXT: reti = new TextUI(); break;
            case GRAPHICS: reti = new WindowUI(); break;
        } 
        return reti;
    }
}
        