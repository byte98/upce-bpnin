package cz.jadjj.jticket.data;

/**
 * Enumeration of available tariff types
 * @author jadjj
 */
public enum TariffType
{
    /**
     * Tariff which computes price between station based on its distance
     */
    DISTANCE,
    
    /**
     * Tariff which computes price between station based on its tariff zones
     */
    ZONE
}