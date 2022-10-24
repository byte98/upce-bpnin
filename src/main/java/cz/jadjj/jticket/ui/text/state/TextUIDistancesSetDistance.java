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
 * Class representing state of program which displays dialog for setting distance between station (with distance selected)
 * @author jadjj
 */
public class TextUIDistancesSetDistance extends TextUIState
{

    /**
     * Origin station for setting distance
     */
    private Station origin;
    
    /**
     * Destination for setting distance
     */
    private Station destination;
    
    /**
     * Creates new state of program which displays dialog for setting distance between station (with distance selected)
     * @param controller Controller of program
     */
    public TextUIDistancesSetDistance(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/distances/set:distance";
        this.screen = new TextUIHTMLTemplateScreen("distances-set-distance", "distances-set-distance.html");
        this.name = "distances-set-distance";
        this.strict = false;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("<cele cislo>", Color.YELLOW, "Vzdalenost mezi vychozi a cilovou stanici");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("cancel", Color.MAGENTA, "Zrusit");
    }
    
    @Override
    public ITextUIScreen getScreen(Map<String, String> data)
    {
        this.origin = cz.jadjj.jticket.data.Stations.GetInstance().GetStation(data.get("station_from"));
        this.destination = cz.jadjj.jticket.data.Stations.GetInstance().GetStation(data.get("station_to"));
        data.put("stations_distances_tr", cz.jadjj.jticket.data.Distances.GetInstance().GenerateDistancesRows(
                this.origin
        ));
        data.put("station_from", this.origin.getName() + " (" + this.origin.getAbbrevation() + ")");
        data.put("station_to", this.destination.getName() + " (" + this.destination.getAbbrevation() + ")");
        int dist = cz.jadjj.jticket.data.Distances.GetInstance().GetDistance(this.origin, this.destination);
        data.put("distance", Integer.toString(dist));
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }

    @Override
    public void handleInput(String input)
    {
        if (input.toLowerCase().equals("cancel"))
        {
            this.controller.changeState("distances");
        }
        else
        {
            if (this.checkInt(input))
            {
                int value = Integer.parseInt(input);
                if (value < 0)
                {
                    this.controller.showError("Vzdalenost musi byt nezaporne cislo!");
                }
                else
                {
                    Map<String, String> data = new HashMap<>();
                    data.put("station_from", this.origin.getAbbrevation());
                    data.put("station_to",this.destination.getAbbrevation());
                    data.put("distance", input);
                    this.controller.changeState("distances-set", data);
                }
            }
            else
            {
                this.controller.showError("Zadany vstup '" + input + "' neni cislo!");
            }
        }
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
    
}
