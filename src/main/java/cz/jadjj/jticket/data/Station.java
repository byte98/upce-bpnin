package cz.jadjj.jticket.data;

/**
 * Class representing station
 * @author jadjj
 */
public class Station {
    
    /**
     * Name of the station
     */
    private String name;
    
    /**
     * Abbrevation for the station
     */
    private String abbrevation;
    
    /**
     * Identifier of the station
     */
    private int identifier;
    
    
    /**
     * Creates new station
     * @param abbrevation Abbrevation for the station
     * @param name Name of the station
     */
    public Station(String abbrevation, String name)
    {
        this.abbrevation = abbrevation;
        this.name = name;
    }
    
    /**
     * Creates new station
     * @param abbrevation Abbrevation for the station
     * @param name Name of the station
     * @param identifier Identifier of station in system
     */
    public Station (String abbrevation, String name, int identifier)
    {
        this.abbrevation = abbrevation;
        this.name = name;
        this.identifier = identifier;
    }
    
    /**
     * Gets name of the station
     * @return Name of the station
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Gets abbrevation for the station
     * @return Abbrevation for the station
     */
    public String getAbbrevation()
    {
        return this.abbrevation;
    }
    
    /**
     * Sets identifier of the station
     * @param id New identifier of the station
     */
    public void setIdentifier(int id)
    {
        this.identifier = id;
    }
    
    /**
     * Gets identifier of the station
     * @return Identifier of the station
     */
    public int getIdentifier()
    {
        return this.identifier;
    }
    
    /**
     * Sets new name to station
     * @param name New name of station
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * Sets new abbrevation to station
     * @param abbr New abbreavation of station
     */
    public void setAbbr(String abbr)
    {
        this.abbrevation = abbr;
    }
}
