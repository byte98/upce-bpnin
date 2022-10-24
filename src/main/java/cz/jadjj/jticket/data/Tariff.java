package cz.jadjj.jticket.data;

/**
 * Class representing tariff in system
 * @author jadjj
 */
public abstract class Tariff {
    
    /**
     * Type of tariff
     */
    private TariffType type;
    
    /**
     * Name of tariff
     */
    private String name;
    
    /**
     * Abbreavation of tariff
     */
    private String abbr;
    
    /**
     * Creates new tariff
     * @param type Type of tariff
     * @param name Name of tariff
     * @param abbreavation Abbreavation of tariff
     */
    public Tariff(TariffType type, String name, String abbreavation)
    {
        this.type = type;
        this.name = name;
        this.abbr = abbreavation;
    }
    
    /**
     * Gets type of tariff
     * @return Type of tariff
     */
    public TariffType getType()
    {
        return this.type;
    }
    
    /**
     * Gets name of tariff
     * @return Name of tariff
     */
    public String getName()
    {
        return this.name;
    }
    
    /**
     * Gets abbreavation of tariff
     * @return Abbreavation of tariff
     */
    public String getAbbr()
    {
        return this.abbr;
    }
    
    /**
     * Gets price between stations
     * @param origin Origin station
     * @param destination Destination station
     * @return Price between selected stations
     */
    public abstract int GetPrice(Station origin, Station destination);
    
    /**
     * Deletes tariff
     */
    public abstract void Delete();
}
