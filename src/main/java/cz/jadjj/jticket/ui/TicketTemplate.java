package cz.jadjj.jticket.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class representing template for printing tickets
 * @author Jiri Skoda <skodaji1@uhk.cz.cz>
 */
public class TicketTemplate
{
    /**
     * Path to background file
     */
    private String background;
    
    /**
     * Path to template file
     */
    private String template;
    
    /**
     * List of all available fields
     */
    private List<TemplateField> fields;

    /**
     * Creates new template for printing tickets
     * @param background File with background of tickets
     * @param template File with template of ticket
     */
    public TicketTemplate(String background, String template)
    {
        this.background = background;
        this.template = template;
        this.fields = new ArrayList<>();
        this.loadFromFile();
    }
    
    /**
     * Loads template from file
     */
    private void loadFromFile()
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        try
        {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(this.template);
            doc.getDocumentElement();
            
            // Parse header
            Node header = doc.getElementsByTagName("header").item(0);
            NodeList headerItems = header.getChildNodes();
            for (int i = 0; i < headerItems.getLength(); i++)
            {
                Node item = headerItems.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) item;
                    if (element.getNodeName().toLowerCase().equals("text")) // Parse textfield
                    {
                        TemplateField field = new TemplateField(
                                "<<text>>",
                                Integer.parseInt(element.getAttribute("top")),
                                Integer.parseInt(element.getAttribute("left")),
                                Integer.parseInt(element.getAttribute("font-size")),
                                (element.getAttribute("font-weight").equals("") ? "normal" : element.getAttribute("font-weight")),
                                (element.getAttribute("max-length").equals("") ? Integer.MAX_VALUE : Integer.parseInt(element.getAttribute("max-length")))
                        );
                        field.setContent(element.getAttribute("content"));
                        this.fields.add(field);
                    }
                    else if (element.getNodeName().toLowerCase().equals("field")) // Parse named field
                    {
                        this.fields.add(new TemplateField(
                                element.getAttribute("name"),
                                Integer.parseInt(element.getAttribute("top")),
                                Integer.parseInt(element.getAttribute("left")),
                                Integer.parseInt(element.getAttribute("font-size")),
                                (element.getAttribute("font-weight").equals("") ? "normal" : element.getAttribute("font-weight")),
                                (element.getAttribute("max-length").equals("") ? Integer.MAX_VALUE : Integer.parseInt(element.getAttribute("max-length")))
                        ));
                    }
                }
            }
            
            // Parse body
            Node body = doc.getElementsByTagName("body").item(0);
            NodeList bodyItems = body.getChildNodes();
            for (int i = 0; i < bodyItems.getLength(); i++)
            {
                Node item = bodyItems.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) item;
                    if (element.getNodeName().toLowerCase().equals("text")) // Parse textfield
                    {
                        TemplateField field = new TemplateField(
                                "<<text>>",
                                Integer.parseInt(element.getAttribute("top")),
                                Integer.parseInt(element.getAttribute("left")),
                                Integer.parseInt(element.getAttribute("font-size")),
                                (element.getAttribute("font-weight").equals("") ? "normal" : element.getAttribute("font-weight")),
                                (element.getAttribute("max-length").equals("") ? Integer.MAX_VALUE : Integer.parseInt(element.getAttribute("max-length"))));
                        field.setContent(element.getAttribute("content"));
                        this.fields.add(field);
                    }
                    else if (element.getNodeName().toLowerCase().equals("field")) // Parse named field
                    {
                        this.fields.add(new TemplateField(
                                element.getAttribute("name"),
                                Integer.parseInt(element.getAttribute("top")),
                                Integer.parseInt(element.getAttribute("left")),
                                Integer.parseInt(element.getAttribute("font-size")),
                                (element.getAttribute("font-weight").equals("") ? "normal" : element.getAttribute("font-weight")),
                                (element.getAttribute("max-length").equals("") ? Integer.MAX_VALUE : Integer.parseInt(element.getAttribute("max-length")))
                        ));
                    }
                }
            }
            
            // Parse footer
            Node footer = doc.getElementsByTagName("footer").item(0);
            NodeList footerItems = footer.getChildNodes();
            for (int i = 0; i < footerItems.getLength(); i++)
            {
                Node item = footerItems.item(i);
                if (item.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element element = (Element) item;
                    if (element.getNodeName().toLowerCase().equals("text")) // Parse textfield
                    {
                        TemplateField field = new TemplateField(
                                "<<text>>",
                                Integer.parseInt(element.getAttribute("top")),
                                Integer.parseInt(element.getAttribute("left")),
                                Integer.parseInt(element.getAttribute("font-size")),
                                (element.getAttribute("font-weight").equals("") ? "normal" : element.getAttribute("font-weight")),
                                (element.getAttribute("max-length").equals("") ? Integer.MAX_VALUE : Integer.parseInt(element.getAttribute("max-length"))));
                        field.setContent(element.getAttribute("content"));
                        this.fields.add(field);
                    }
                    else if (element.getNodeName().toLowerCase().equals("field")) // Parse named field
                    {
                        this.fields.add(new TemplateField(
                                element.getAttribute("name"),
                                Integer.parseInt(element.getAttribute("top")),
                                Integer.parseInt(element.getAttribute("left")),
                                Integer.parseInt(element.getAttribute("font-size")),
                                (element.getAttribute("font-weight").equals("") ? "normal" : element.getAttribute("font-weight")),
                                (element.getAttribute("max-length").equals("") ? Integer.MAX_VALUE : Integer.parseInt(element.getAttribute("max-length")))
                        ));
                    }
                }
            }
        }
        catch (Exception ex)
        {
          Logger.getLogger(TicketTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sets value of field
     * @param name Name of field
     * @param value Value of field
     * @return Actual template of ticket
     */
    public TicketTemplate setValue(String name, String value)
    {
        this.fields.stream().filter(field -> (field.getName().toLowerCase().equals(name.toLowerCase()))).forEachOrdered(field -> {
            field.setContent(value);
        });
        return this;
    }
    
    /**
     * Generates PDF file
     * @param output Path to output file which will be generated
     */
    public void generatePDF(String output)
    {
        PDDocument doc = new PDDocument();
        float POINTS_PER_INCH = 72f;
        PDPage page = new PDPage(new PDRectangle(5.8f * POINTS_PER_INCH, 8.2f * POINTS_PER_INCH));
        
        // Add background image
        try
        {
            
            PDPageContentStream stream = new PDPageContentStream(doc, page, false, true);
            PDImageXObject image = PDImageXObject.createFromFile(this.background, doc);
            stream.saveGraphicsState();
            stream.drawImage(image, 0, 0);
            stream.restoreGraphicsState();
            stream.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(TicketTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.fields.forEach(field -> {
            this.writeTextToPage(
                    doc,
                    page,
                    field.getContent(),
                    field.getFont(),
                    field.getFontSize(),
                    field.getTop(),
                    field.getLeft(),
                    field.getMaxLength()
            );
        });
        
        doc.addPage(page);
        try
        {
            doc.save(output);
            doc.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(TicketTemplate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Writes text to PDF document
     * @param document Document to which text will be written
     * @param page Page to which text will be written
     * @param text Text which will be written to page
     * @param fontFamily Font defining appearance of text which will be written to page
     * @param fontSize Size of font of text which will be written to page
     * @param top Top position of text
     * @param left Left position of text
     * @param maxLength Maximal length of row
     */
    private void writeTextToPage(PDDocument document, PDPage page, String text, PDType1Font fontFamily, int fontSize, int top, int left, int maxLength)
    {
        if (text != null)
        {
            if (text.length() > maxLength)
            {
                // If longer than expected line length, split to multiline
                String parts[] = text.split(" ");
                List<String> newLines = new ArrayList<>();
                String line = new String();
                for (String part: parts)
                {
                    if (part.length() > 1)
                    {
                        if (line.length() + part.length() > maxLength)
                        {
                            newLines.add(line);
                            line = part;
                        }
                        else
                        {
                            line += part;
                        }
                    }
                    else
                    {
                        line += part;
                    }
                    line += " ";
                }
                newLines.add(line);
                ListIterator<String> it = newLines.listIterator();
                int idx = 0;
                while (it.hasNext())
                {
                    this.writeTextToPage(document, page, it.next(), fontFamily, fontSize, top - (idx * fontSize), left, Integer.MAX_VALUE);
                    idx++;
                }
            }
            else
            {
                try
                {
                    PDPageContentStream stream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND,true,true);        
                    stream.setFont(fontFamily, fontSize);
                    stream.beginText();
                    stream.newLineAtOffset(left, top);
                    try
                    {
                    stream.showText(text);
                    }
                    catch (Exception ex){}
                    stream.endText();
                    stream.close();
                }
                catch (IOException ex)
                {
                    Logger.getLogger(TicketTemplate.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
