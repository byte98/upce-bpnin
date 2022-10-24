package cz.jadjj.jticket.ui.text.state;


import cz.jadjj.jticket.data.Station;
import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.ITextUIScreen;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHTMLTemplateScreen;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing state of program which displays creating of table of distances
 * @author jadjj
 */
public class TextUIDistancesCreate extends TextUIState
{

    /**
     * Array of all stations
     */
    private Station[] stations;
    
    /**
     * Index of stations array which selects actual origin
     */
    private int origin;
    
    /**
     * Index of stations array which selects actual destination
     */
    private int destination;
    
    /**
     * Creates new state of program with table of distances
     * @param controller 
     */
    public TextUIDistancesCreate(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/distances/create";
        this.screen = new TextUIHTMLTemplateScreen("distances-create", "distances-create.html");
        this.name = "distances-create";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<cele cislo>", Color.YELLOW, "Vzdalenost mezi stanicemi v kilometrech");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
    }
    
    
    /**
     * Checks, whether input contains only integer
     * @param input Input which will be checked
     * @return <code>TRUE</code> if input contains integer only, <code>FALSE</code> otherwise
     * @author Jonas K https://stackoverflow.com/questions/237159/whats-the-best-way-to-check-if-a-string-represents-an-integer-in-java
     */
    private boolean checkInt(String input)
    {
        if (input == null)
        {
            return false;
        }
        int length = input.length();
        if (length == 0)
        {
            return false;
        }
        int i = 0;
        if (input.charAt(0) == '-')
        {
            if (length == 1)
            {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++)
        {
            char c = input.charAt(i);
            if (c < '0' || c > '9')
            {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void load()
    {
        this.stations = cz.jadjj.jticket.data.Stations.GetInstance().GetAllStations();
        if (this.stations.length > 1)
        {
            cz.jadjj.jticket.data.Distances.GetInstance().SetAllZeroes();
            this.origin = 0;
            this.destination = 1;
        }
    }
    
    @Override
    public ITextUIScreen getScreen()
    {
        if (this.stations.length > 1)
        {
            Map<String, String> data = new HashMap<>();
            data.put("stations_distances_tr", cz.jadjj.jticket.data.Distances.GetInstance().GenerateDistancesRows(this.stations[this.origin]));
            data.put("station_from", this.stations[this.origin].getName());
            data.put("station_to", this.stations[this.destination].getName());
            ((TextUIHTMLTemplateScreen) this.screen).setContent(data);
        }
        return this.screen;
    }
    
    @Override
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        if (this.stations.length > 1)
        {
            data.put("stations_distances_tr", cz.jadjj.jticket.data.Distances.GetInstance().GenerateDistancesRows(this.stations[this.origin]));
            data.put("station_from", this.stations[this.origin].getName());
            data.put("station_to", this.stations[this.destination].getName());
            ((TextUIHTMLTemplateScreen) this.screen).setContent(data);
        }
        return this.screen;
    }
    
    
    @Override
    public void handleInput(String input)
    {
        if (input.toLowerCase().equals("cancel"))
        {
            this.controller.changeState("distances");
        }
        else if (this.checkInt(input) == true)
        {
            int value = Integer.parseInt(input);
            if (value < 0)
            {
                this.controller.showError("Zadane cislo nesmi byt mensi nez nula!");
            }
            else
            {
                cz.jadjj.jticket.data.Distances.GetInstance().SetDistance(
                        this.stations[this.origin],
                        this.stations[this.destination],
                        value
                );
                this.controller.showSucess("Vzdalenost mezi stanicemi uspesne nastavena.");                
                boolean nextToSet = this.nextStationsDistance();
                if (nextToSet == true)
                {
                    this.controller.reDraw();
                }
                else
                {
                    this.controller.showSucess("Tabulka vzdalenosti byla uspesne vytvorena.");
                    this.controller.changeState("distances");
                }
            }
        }
        else
        {
            this.controller.showError("Neznamy prikaz '" + input + "'!");
        }
    }
    
    @Override
    public void control()
    {
      if (this.stations.length < 2)   
      {
          this.controller.showError("V systemu je prilis malo stanic!");
          this.controller.changeState("distances");
      }
    }
    
    /**
     * Gets next stations to set distance between them
     * @return <code>TRUE</code> if there is next stations to set distance between, <code>FALSE</code> otherwise
     */
    private boolean nextStationsDistance()
    {
        boolean reti = true;
        this.destination ++;
        if (this.destination >= this.stations.length)
        {
            this.destination = 0;
            this.origin++;
        }
        if (this.origin >= this.stations.length)
        {
            reti = false;
        }
        else if (this.origin == this.destination)
        {
            reti = this.nextStationsDistance();
        }
        else if (cz.jadjj.jticket.data.Distances.GetInstance().GetDistance(this.stations[this.origin], this.stations[this.destination]) != 0)
        {
            reti = this.nextStationsDistance();
        }
        else
        {
            // pass
        }

        return reti;
    }
    
}
