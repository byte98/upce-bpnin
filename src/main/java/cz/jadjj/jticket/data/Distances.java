package cz.jadjj.jticket.data;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class managing distances between stations
 * @author jadjj
 */
public class Distances
{
    /**
     * Instance of manager of distances between stations
     */
    private static Distances instance = null;
    
    /**
     * List of distances from stations
     */
    private Map<Station, Distance> dist;
    
    /**
     * File with saved distances between stations
     */
    private final String filePath = "resources/data/distances.jtd";
    
    /**
     * Creates new manager of distances between stations
     */
    private Distances()
    {
        this.dist = new HashMap<>();
        this.LoadDistances();
    }
    
    /**
     * Gets instance of manager of distances between stations
     */
    public static Distances GetInstance()
    {
        if (Distances.instance == null)
        {
            Distances.instance = new Distances();
        }
        return Distances.instance;
    }
    
    /**
     * Saves distances to file
     */
    private void SaveDistances()
    {
        // First, get stations identifiers to array
        Station[] stations = Stations.GetInstance().GetAllStations();
        int[] stIds = new int[stations.length];
        for (int i = 0; i < stations.length; i++)
        {
            stIds[i] = stations[i].getIdentifier();
        }
        
        // Then, get arrays of distances from each station
        int[][] data = new int[stations.length][stations.length];
        for (int i = 0; i < stations.length; i++)   // Start with arrays containing only zeroes
        {
            Arrays.fill(data[i], 0);
        }
        for (Station s: stations)
        {
            Distance d = this.dist.get(s);
            int from = 0;
            for (int f = 0; f < stIds.length; f++)
            {
                if (stIds[f] == s.getIdentifier())
                {
                    from = f;
                    break;
                }
            }
            if (d != null)
            {
                for (int to = 0; to < stIds.length; to++)
                {
                    int distToSt = d.GetDistance(Stations.GetInstance().GetStation(stIds[to]));
                    data[from][to] = distToSt;
                }
            }
        }
        
        // Then, prepare all data to one gigantic array
        int[] toWrite = new int[(stations.length * stations.length) + stations.length + 1];
        int idx = 0;
        toWrite[idx] = stations.length;                    // First, write count of stations
        idx++;
        for (int id: stIds)                                // Write all stations identifiers
        {
            toWrite[idx] = id;
            idx++;
        }
        for (int from = 0; from < stations.length; from++) // Write distances
        {
            for (int to = 0; to < stations.length; to++)
            {
                toWrite[idx] = data[from][to];
                idx++;
            }
        }
        
        // And at the end, write all to file
        ByteArrayOutputStream baos = new ByteArrayOutputStream(toWrite.length * Integer.BYTES);
        DataOutputStream dos = new DataOutputStream(baos);
        for (int w: toWrite)
        {
            try
            {
                dos.writeInt(w);
            }
            catch (IOException ex)
            {
                Logger.getLogger(Distances.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try
        {
            FileOutputStream fos = new FileOutputStream(this.filePath);
            baos.writeTo(fos);
            fos.flush();
            fos.close();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(Distances.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(IOException ex)
        {
            Logger.getLogger(Distances.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    /**
     * Loads distances between stations from file
     */
    private void LoadDistances()
    {
        // First, load whole file into array
        File in = new File(this.filePath);
        int[] data;
        if (in.exists())
        {
            try
            {
                FileInputStream fis = new FileInputStream(in);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                int count = (int) (in.length() / Integer.BYTES);
                data = new int[count];
                for (int i = 0; i < count; i++)
                {
                    data[i] = dis.readInt();
                }
                // Prepare stations
                int stationsCount = data[0];
                Station stations[] = new Station[stationsCount];
                for (int i = 1; i < stationsCount + 1; i++)
                {
                    stations[i - 1] = Stations.GetInstance().GetStation(data[i]);
                }
                int idx = stationsCount + 1;
                // Load data
                for (int from = 0; from < stationsCount; from++)
                {
                    Distance d = new Distance(stations[from]);
                    for (int to = 0; to < stationsCount; to++)
                    {
                        int i = idx + ((from * stationsCount) + to);
                        d.SetDistance(stations[to], data[i]);
                    }
                    this.dist.put(stations[from], d);
                }
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(Distances.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                Logger.getLogger(Distances.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }
        
    }
    
    /**
     * Gets all defined distances from station
     * @param st Origin station from which distances will be returned
     * @return All defined distances from selected station
     */
    public Distance GetDistanceFromStation(Station st)
    {
        return this.dist.get(st);
    }
    
    
    /**
     * Gets distance between selected stations
     * @param from Origin station
     * @param to Final station
     * @return Distance between selected stations
     */
    public int GetDistance(Station from, Station to)
    {
        int reti = 0;
        Distance d = this.dist.get(from);
        if (d != null)
        {
            reti = d.GetDistance(to);
        }
        return reti;
    }
    
    /**
     * Sets distance between stations
     * @param from Origin station
     * @param to Destination station
     * @param distance Distance between origin and destination
     */
    public void SetDistance(Station from, Station to, int distance)
    {
        Distance d = this.dist.get(from);
        if (d == null)
        {
            d = new Distance(from);
            this.dist.put(from, d);
        }
        d.SetDistance(to, distance);
        
        Distance b = this.dist.get(to);
        if (b == null)
        {
            b = new Distance(to);
            this.dist.put(to, d);
        }
        b.SetDistance(from, distance);
        this.SaveDistances();
    }
    
    /**
     * Sets all distances between stations to zero
     */
    public void SetAllZeroes()
    {
        Station stations[] = Stations.GetInstance().GetAllStations();
        for (Station from : stations)
        {
            for (Station to : stations)
            {
                this.SetDistance(from, to, 0);
            }
        }
    }
    
    /**
     * Gets string containing HTML rows for table with distances from station
     * @param origin Origin station from which distances will be displayed
     * @return HTML rows of table with distances from station
     */
    public String GenerateDistancesRows(Station origin)
    {
        String reti = "";
        Distance d = this.GetDistanceFromStation(origin);
        if (d != null)
        {
            reti = d.GetAllDistances().entrySet().stream().filter(entry -> (entry.getKey().equals(origin) == false)).map(entry -> "<tr><td style='color: green;'>" + entry.getKey().getAbbrevation() + "</td>" +
                    "<td style='color: gray;'>" + entry.getKey().getName() + "</td>" +
                            "<td style='color: white;'>" + entry.getValue() + "&nbsp;km</td></tr>").reduce(reti, String::concat);
        }
        return reti;
    }
}
