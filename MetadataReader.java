package minhash;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class MetadataReader {
   
	private static String filename;
	
	private String author;
	private String barCode;
	private String lang;
	private String title;
	
	MetadataReader(String name)
	{
		filename = name;
	}
	
	public String retAuthor()
	{
		return author;
	}
	
	public String retLang()
	{
		return lang;
	}
	
	public String retTitle()
	{
		return title;
	}
	
	public String retBarcode()
	{
		return barCode;
	}
	   	
    public void process() {
       try {
          File inputFile = new File(filename);

          SAXBuilder saxBuilder = new SAXBuilder();

          Document document = saxBuilder.build(inputFile);

          Element classElement = document.getRootElement();

          List<Element> studentList = classElement.getChildren();
          for (int temp = 0; temp < studentList.size(); temp++) {    
             Element student = studentList.get(temp);
             Attribute attribute =  student.getAttribute("element");
             switch(attribute.getValue())
             {
             	case "contributor": author = student.getTextNormalize(); break;
             	case "language": lang = student.getTextNormalize(); break;
             	case "title": title = student.getTextNormalize(); break; 
             	case "identifier":
             		Attribute attribute2 = student.getAttribute("qualifier");
             		if(attribute2.getValue().equals("other"))
             		{
             			barCode = student.getTextNormalize();
             		}
                    break;
             }
          }
       }
       catch(JDOMException e){
          e.printStackTrace();
       }
       catch(IOException ioe){
          ioe.printStackTrace();
       }
    }
    
    /*public static void main(String[] args)
    {
    	MetadataReader file1 = new MetadataReader("dublin_core.xml");
    	file1.process();
    	System.out.println("Author : " + retAuthor());
        System.out.println("Title : " + retTitle());	
        System.out.println("Language : " + retLang());	
        System.out.println("Barcode : " + retBarcode());    	
    }*/    
}