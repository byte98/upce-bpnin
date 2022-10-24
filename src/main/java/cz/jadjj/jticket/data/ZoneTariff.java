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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing tariff which calculates price based on zones of stations
 * @author jadjj
 */
public class ZoneTariff extends Tariff{
    
    /**
     * Map of zones of stations
     */
    private Map<Station, Integer> zones;
    
    /**
     * Price list of tariff
     */
    private Map<Integer, Integer> prices;
    
    /**
     * Path to file with zones of stations
     */
    private final String ZfilePath;
    
    /**
     * Path to file with prices of zones
     */
    private final String PfilePath;

    /**
     * Creates new tariff which calculates price based on zones of stations
     * @param name Name of tariff
     * @param abbreavation Abbreavation of tariff
     */
    public ZoneTariff(String name, String abbreavation)
    {
        super(TariffType.ZONE, name, abbreavation);
        this.zones = new HashMap<>();
        this.prices = new HashMap<>();
        for (Station s: cz.jadjj.jticket.data.Stations.GetInstance().GetAllStations())
        {
            this.zones.put(s, 0);
        }
        this.ZfilePath = "resources/data/" + abbreavation.toLowerCase() + ".jtz";
        this.PfilePath = "resources/data/" + abbreavation.toLowerCase() + ".jtp";
        this.LoadZones();
        this.LoadPrices();
    }
    
    /**
     * Saves zones to file
     */
    private void SaveZones()
    {
        // First, save all to one big array
        int idx = 0;
        int output[] = new int[(this.zones.size() * 2) + 1];
        output[idx] = this.zones.size();
        idx++;
        for (Station s: this.zones.keySet())
        {
            output[idx] = s.getIdentifier();
            idx++;
        }
        for (int i = 0; i < this.zones.size(); i++)
        {
            Station s = Stations.GetInstance().GetStation(output[i + 1]);
            output[idx] = this.zones.get(s);
            idx++;
        }
        
        // Than, save it to file
        ByteArrayOutputStream baos = new ByteArrayOutputStream(output.length * Integer.BYTES);
        DataOutputStream dos = new DataOutputStream(baos);
        for (int val: output)
        {
            try
            {
                dos.writeInt(val);
            }
            catch (IOException ex)
            {
                Logger.getLogger(ZoneTariff.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try
        {
            FileOutputStream fos = new FileOutputStream(this.ZfilePath);
            baos.writeTo(fos);
            fos.flush();
            fos.close();
            dos.close();
            baos.close();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ZoneTariff.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ZoneTariff.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    /**
     * Loads tones from file
     */
    private void LoadZones()
    {
        File in = new File (this.ZfilePath);
        int [] data;
        if (in.exists())
        {
            try
            {
                FileInputStream fis = new FileInputStream(in);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                // First, load file into array
                int count = (int) (in.length() / Integer.BYTES);
                data = new int[count];
                for (int i = 0; i < count; i++)
                {
                    data[i] = dis.readInt();
                }
                int dataCount = data[0];
                for (int i = 0; i < dataCount; i++)
                {
                    Station s = Stations.GetInstance().GetStation(data[i + 1]);
                    int zone = data[i + 1 + dataCount];
                    this.zones.put(s, zone);
                }
                fis.close();
                bis.close();
                dis.close();
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(ZoneTariff.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                Logger.getLogger(ZoneTariff.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    /**
     * Sets zone to station
     * @param st Station to which zone ill be assigned to
     * @param zone Zone which will be assigned to station
     */
    public void setZone(Station st, int zone)
    {
        this.zones.put(st, zone);
        this.SaveZones();
    }
    
    /**
     * Generates string with HTML table rows containing zone for each station
     * @return String with HTML table rows containing zone for each station
     */
    public String generateZonesTr()
    {
        String reti = new String();
        for (Station st: cz.jadjj.jticket.data.Stations.GetInstance().GetAllStations())
        {
            reti += "<tr><td style='color: green;'>" + st.getAbbrevation().toUpperCase() + "</td><td>" + st.getName() + "</td><td style='color: white;'>" + (this.zones.get(st) == null ? "0" : this.zones.get(st)) + "</td></tr>";
        }
        return reti;
    }

    @Override
    public int GetPrice(Station origin, Station destination)
    {
        int reti = 0;
        int z = Math.abs(this.zones.get(origin) - this.zones.get(destination));
        reti = (this.prices.get(z) == null ? 0 : this.prices.get(z));
        return reti;
    }
    
    /**
     * Sets price of tariff for zones
     * @param zones Zones passed by passenger to which price will be assigned to 
     * @param price Price for passed zones
     */
    public void SetPrice(int zones, int price)
    {
        this.prices.put(zones, price);
        this.SavePrices();
    }
    
    /**
     * Saves prices to file
     */
    private void SavePrices()
    {
        // First, save all to one big array
        int idx = 0;
        int output[] = new int[this.prices.size() * 2];
        for (Integer zones: this.prices.keySet())
        {
            output[idx] = zones;
            idx++;
            output[idx] = this.prices.get(zones);
            idx++;
        }
        
        // Than, save it to file
        ByteArrayOutputStream baos = new ByteArrayOutputStream(output.length * Integer.BYTES);
        DataOutputStream dos = new DataOutputStream(baos);
        for (int val: output)
        {
            try
            {
                dos.writeInt(val);
            }
            catch (IOException ex)
            {
                Logger.getLogger(ZoneTariff.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try
        {
            FileOutputStream fos = new FileOutputStream(this.PfilePath);
            baos.writeTo(fos);
            fos.flush();
            fos.close();
            dos.close();
            baos.close();
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(ZoneTariff.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(ZoneTariff.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Loads prices from file
     */
    private void LoadPrices()
    {
        File in = new File (this.PfilePath);
        int [] data;
        if (in.exists())
        {
            try
            {
                FileInputStream fis = new FileInputStream(in);
                BufferedInputStream bis = new BufferedInputStream(fis);
                DataInputStream dis = new DataInputStream(bis);
                // First, load file into array
                int count = (int) (in.length() / Integer.BYTES);
                data = new int[count];
                for (int i = 0; i < count; i++)
                {
                    data[i] = dis.readInt();
                }
                for (int i = 0; i < data.length; i++)
                {
                    this.prices.put(data[i], data[i + 1]);
                    i++;
                }
                dis.close();
                bis.close();
                fis.close();
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(ZoneTariff.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (IOException ex)
            {
                Logger.getLogger(ZoneTariff.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    /**
     * Gets all available zones of stations
     * @return All available zones of stations
     */
    public Map<Station, Integer> GetAllZones()
    {
        return this.zones;
    }
    
    /**
     * Gets all available prices of zones
     * @return All available prices of zones
     */
    public Map<Integer, Integer> GetAllPrices()
    {
        return this.prices;
    }
    
    @Override
    public void Delete()
    {
        File zFile = new File(this.ZfilePath);
        if (zFile.exists())
        {
            zFile.delete();
        }
        File pFile = new File(this.PfilePath);
        if (pFile.exists())
        {
            pFile.delete();
        }
    }
    
    /**
     * Gets zone of station
     * @param s Station which zone will be get
     * @return Number of zone to which station belongs to
     */
    public int getZone(Station s)
    {
        return this.zones.get(s);
    }
}
