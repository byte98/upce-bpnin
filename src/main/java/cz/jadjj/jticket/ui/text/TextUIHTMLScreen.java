package cz.jadjj.jticket.ui.text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing screen which content is written in HTML
 * @author jadjj
 */
public class TextUIHTMLScreen implements ITextUIScreen
{
    /**
     * File containing content of screen
     */
    protected final String fileName;
    
    /**
     * Name of screen
     */
    protected final String name;
    
    /**
     * Content of the screen
     */
    protected String content = null;
    
    /**
     * Creates new instance of screen which content is saved in HTML file
     * @param name Name of screen
     * @param fileName Name of file with screen content
     */
    public TextUIHTMLScreen(String name, String fileName)
    {
        this.name = name;
        this.fileName = fileName;
    }

    @Override
    public String getContent()
    {
        if (this.content == null) // Load content only once
        {
            try
            {
                File html = new File(TextUI.HTML_DIR + this.fileName);
                Scanner reader = new Scanner(html);
                while (reader.hasNextLine())
                {
                    this.content += reader.nextLine();
                }
                reader.close();
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(TextUIHTMLScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.content;        
    }

    @Override
    public String getName()
    {
        return this.name;
    }
}
    