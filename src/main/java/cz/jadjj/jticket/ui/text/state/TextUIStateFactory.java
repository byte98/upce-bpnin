package cz.jadjj.jticket.ui.text.state;

import cz.jadjj.jticket.ui.text.TextUIController;

/**
 * Class which can create every state of program
 * @author jadjj
 */
public class TextUIStateFactory
{
    /**
     * Creates new state
     * @param controller Controller of program
     * @param name Name of state
     * @return New state of program
     */
    public static TextUIState createState(TextUIController controller, String name)
    {
        TextUIState reti = null;
        switch (name.toLowerCase())
        {
            case                "welcome": reti = new TextUIWelcome(controller);              break;
            case                   "exit": reti = new TextUIExit(controller);                 break; 
            case                   "data": reti = new TextUIData(controller);                 break; 
            case               "stations": reti = new TextUIStations(controller);             break;
            case           "stations-add": reti = new TextUIStationsAdd(controller);          break;
            case      "stations-add-name": reti = new TextUIStationsAddName(controller);      break;
            case      "stations-add-abbr": reti = new TextUIStationsAddAbbr(controller);      break;
            case     "stations-edit-name": reti = new TextUIStationsEditName(controller);     break;
            case     "stations-edit-abbr": reti = new TextUIStationsEditAbbr(controller);     break;
            case          "stations-edit": reti = new TextUIStationsEdit(controller);         break;
            case        "stations-delete": reti = new TextUIStationsDelete(controller);       break;
            case              "distances": reti = new TextUIDistances(controller);            break;
            case       "distances-create": reti = new TextUIDistancesCreate(controller);      break;
            case         "distances-view": reti = new TextUIDistancesView(controller);        break;
            case "distances-view-station": reti = new TextUIDistancesViewStation(controller); break;
            case     "distances-set-from": reti = new TextUIDistancesSetFrom(controller);     break;
            case       "distances-set-to": reti = new TextUIDistancesSetTo(controller);       break;
            case "distances-set-distance": reti = new TextUIDistancesSetDistance(controller); break;
            case          "distances-set": reti = new TextUIDistancesSet(controller);         break;
            case                "tariffs": reti = new TextUITariffs(controller);              break;
            case      "tariffs-zone-name": reti = new TextUITariffsZoneName(controller);      break;
            case      "tariffs-zone-abbr": reti = new TextUITariffsZoneAbbr(controller);      break;
            case           "tariffs-zone": reti = new TextUITariffsZone(controller);          break;
            case     "tariffs-zone-zones": reti = new TextUITariffsZoneZones(controller);     break;
            case    "tariffs-zone-prices": reti = new TextUITariffsZonePrices(controller);    break;
            case      "tariffs-zone-view": reti = new TextUITariffsZoneView(controller);      break;
            case    "tariffs-zone-delete": reti = new TextUITariffsZoneDelete(controller);    break;
            case      "tariffs-dist-name": reti = new TextUITariffsDistName(controller);      break;
            case      "tariffs-dist-abbr": reti = new TextUITariffsDistAbbr(controller);      break;
            case           "tariffs-dist": reti = new TextUITariffsDist(controller);          break;
            case    "tariffs-dist-prices": reti = new TextUITariffsDistPrices(controller);    break;
            case      "tariffs-dist-view": reti = new TextUITariffsDistView(controller);      break;
            case    "tariffs-dist-delete": reti = new TextUITariffsDistDelete(controller);    break;
            case                 "ticket": reti = new TextUITicket(controller);               break;
        }
        return reti;
    }
}
