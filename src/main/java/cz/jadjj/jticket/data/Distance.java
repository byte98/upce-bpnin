package cz.jadjj.jticket.data;

import java.util.HashMap;
import java.util.Map;

/**
 * Class representing distance between stations
 * @author jadjj
 */
public class Distance
{
    /**
     * Station from which distance will be meassurred
     */
    private Station station;
    
    /**
     * List of distances to other stations
     */
    private Map<Station, Integer> distances;
    
    /**
     * Creates new distance between stations
     * @param st Origin station
     */
    public Distance(Station st)
    {
        this.station = st;
        this.distances = new HashMap<>();
    }
    
    /**
     * Sets distance from origin station to selected one
     * @param to Selected station to which distance is set
     * @param distance Distance between stations (in kilometers)
     */
    public void SetDistance(Station to, int distance)
    {
        this.distances.put(to, distance);
    }
    
    /**
     * Gets distance from origin station to selected one
     * @param to Selected station to which distance will be returned
     * @return Distance between origin station and selected one
     */
    public int GetDistance(Station to)
    {
        int reti = 0;
        if (this.distances.get(to) != null)
        {
            reti = this.distances.get(to);
        }
        return reti;
    }
    
    /**
     * Gets all distances to other stations
     * @return All distances to other stations
     */
    public Map<Station, Integer> GetAllDistances()
    {
        return this.distances;
    }
}
