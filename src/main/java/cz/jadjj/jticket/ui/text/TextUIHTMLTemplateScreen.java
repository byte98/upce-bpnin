package cz.jadjj.jticket.ui.text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class representing screen which content is in HTML and there is data which will be displayed
 * @author jadjj
 */
public class TextUIHTMLTemplateScreen extends TextUIHTMLScreen
{
    /**
     * Original content of HTML file with template
     */
    private String originalContent = null;
    
    /**
     * Creates new HTML screen with supported templates
     * @param name Name of screen
     * @param fileName File with screen content
     */
    public TextUIHTMLTemplateScreen(String name, String fileName)
    {
        super(name, fileName);
    }
    
    /**
     * Replaces content placeholders with data
     * @param data Data which will replace placeholders
     */
    public void setContent(Map<String, String> data)
    {        
        if (this.content == null)
        {         
            try
            {
                File html = new File(TextUI.HTML_DIR + this.fileName);
                Scanner reader = new Scanner(html);
                while (reader.hasNextLine())
                {
                    this.originalContent += reader.nextLine();
                }
                reader.close();
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(TextUIHTMLTemplateScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.content = this.originalContent;
        data.entrySet().forEach(entry -> {
            this.content = this.content.replace("{$" + entry.getKey() + "}", entry.getValue());
        });
    }
    
    @Override
    public String getContent()
    {
        if (this.originalContent == null) // Load content only once
        {
            try
            {
                File html = new File(TextUI.HTML_DIR + this.fileName);
                Scanner reader = new Scanner(html);
                while (reader.hasNextLine())
                {
                    this.originalContent += reader.nextLine();
                }
                reader.close();
                this.content = this.originalContent;
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(TextUIHTMLTemplateScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.content;
    }
}
