package cz.jadjj.jticket.ui.text.state;
import cz.jadjj.jticket.data.Station;
import cz.jadjj.jticket.ui.text.ITextUIHelp;
import cz.jadjj.jticket.ui.text.ITextUIScreen;
import cz.jadjj.jticket.ui.text.TextUIController;
import cz.jadjj.jticket.ui.text.TextUIHTMLTemplateScreen;
import cz.jadjj.jticket.ui.text.TextUIHelpFactory;
import java.awt.Color;
import java.util.Map;

/**
 * Class representing state of program which displays dialog for setting distance between station (with confirming dialog)
 * @author jadjj
 */
public class TextUIDistancesSet extends TextUIState
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
     * Distance between stations
     */
    private int distance;
    
    /**
     * Creates new state of program which displays dialog for setting distance between station (with confirming dialog)
     * @param controller Controller of program
     */
    public TextUIDistancesSet(TextUIController controller)
    {
        super(controller);
        this.commandPrefix = "/data/distances/set?";
        this.screen = new TextUIHTMLTemplateScreen("distances-set", "distances-set.html");
        this.name = "distances-set";
        this.strict = true;
        
        this.helps = new ITextUIHelp[2];
        this.helps[0] = TextUIHelpFactory.createSimpleHelp("yes", Color.GREEN, "Udaje jsou v poradku, zmenit vzdalenost mezi stanicemi");
        this.helps[1] = TextUIHelpFactory.createSimpleHelp("no", Color.RED, "Zrusit");
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
        this.distance = Integer.parseInt(data.get("distance"));
        ((TextUIHTMLTemplateScreen)this.screen).setContent(data);
        return this.screen;
    }

    @Override
    public void handleInput(String input)
    {
        switch(input.toLowerCase())
        {
            case "yes":
                cz.jadjj.jticket.data.Distances.GetInstance().SetDistance(this.origin, this.destination, this.distance);
                this.controller.showSucess("Vzdalenost mezi stanicemi byla uspesne zmenena.");
                this.controller.changeState("distances");
                break;
            case "no":
                this.controller.changeState("distances");
                break;
        }
    }
    
}
