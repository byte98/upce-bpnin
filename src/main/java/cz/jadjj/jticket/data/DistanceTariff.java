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
 * 
 * @author jadjj
 */
public class DistanceTariff extends Tariff {

    /**
     * Path to file with prices;
     */
    private final String PfilePath;
    
    /**
     * Price list of distances of tariff
     */
    private Map<Integer, Integer> priceList;
    
    /**
     * Creates new distance tariff
     * @param name Name of tariff
     * @param abbreavation Abbreavation of tariff
     */
    public DistanceTariff(String name, String abbreavation)
    {
        super(TariffType.DISTANCE, name, abbreavation);
        this.PfilePath = "resources/data/" + this.getAbbr().toLowerCase() + ".jtp";
        this.priceList = new HashMap<>();
        this.LoadPrices();
    }

    @Override
    public int GetPrice(Station origin, Station destination)
    {
        int reti = 0;
        int dist = cz.jadjj.jticket.data.Distances.GetInstance().GetDistance(origin, destination);
        if (this.priceList.get(dist) != null)
        {
            reti = this.priceList.get(dist);
        }
        return reti;
    }
    
    @Override
    public void Delete()
    {
        File Pfile = new File(this.PfilePath);
        if (Pfile.exists())
        {
            Pfile.delete();
        }
    }
    
    /**
     * Sets price for distance passed by passenger 
     * @param distance Distance to which price will be assigned to
     * @param price Price for passed distance
     */
    public void SetPrice(int distance, int price)
    {
        this.priceList.put(distance, price);
        this.SavePrices();
    }
    
    /**
     * Saves prices to file
     */
    private void SavePrices()
    {
        // First, write everything to one array
        int output[] = new int[this.priceList.size() * 2];
        int idx = 0;
        for (int dist: this.priceList.keySet())
        {
            output[idx] = dist;   
            idx++;
            output[idx] = this.priceList.get(dist);
            idx++;
        }
        
        // Then write content to binary file
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
                    this.priceList.put(data[i], data[i + 1]);
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
     * Generates string containing table rows with price list of tariff
     * @param min Starting distance of the table
     * @param max Maximal distance shown in table
     * @return String with HTML table rows with price list of tariff
     */
    public String GeneratePriceListRows(int min, int max)
    {
        String reti = new String();
        for (int dist = min; dist <= max; dist++)
        {
            reti += "<tr><td>" + dist +  "&nbsp;km</td><td style='color: white;'>";
            reti += (this.priceList.get(dist) == null ? " " : this.priceList.get(dist) + " Kc");
            reti += "</td></tr>";
        }
        return reti;
    }
    
    /**
     * Gets price list of tariff
     * @return Price list of tariff
     */
    public Map<Integer, Integer> getPriceList()
    {
        return new HashMap<>(this.priceList);
    }
}
