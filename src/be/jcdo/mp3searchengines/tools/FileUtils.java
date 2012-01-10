package be.jcdo.mp3searchengines.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;


public class FileUtils {
	
	private FileUtils() {}

    public static void parseXslt(File in, File out, File xsl) {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(xsl));
            transformer.transform(new StreamSource(in), new StreamResult(new FileOutputStream(out)));
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
        } catch (TransformerException ex) {
        	ex.printStackTrace();
        } catch (FileNotFoundException ex) {
        	ex.printStackTrace();
        }
    }

    public static void inputStream2File(InputStream inputStream, File dest) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(dest);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException ex) {
        	ex.printStackTrace();
        } finally {
        	out.close();
        }
    }
    
    public static String inputStream2String(InputStream inputStream, String encoding) throws IOException {
    	StringWriter writer = new StringWriter();
    	
		IOUtils.copy(inputStream, writer, encoding);
        
        return writer.toString();
    }
    
    public static String file2String(String path) throws IOException {
		FileInputStream stream = new FileInputStream(new File(path));
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		}
		finally {
			stream.close();
		}
    }
    
    public static String file2String(File file) throws IOException {
		FileInputStream stream = new FileInputStream(file);
		try {
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			/* Instead of using default, pass in a decoder. */
			return Charset.defaultCharset().decode(bb).toString();
		}
		finally {
			stream.close();
		}
    }
    
    public static void cleanHtmlFile(File file, String encoding) {
        try {
            CleanerProperties props = new CleanerProperties();

            // set some properties to non-default values
            props.setRecognizeUnicodeChars(false);
            props.setOmitComments(true);

            // do parsing
            HtmlCleaner htmlc = new HtmlCleaner(props);
            TagNode tagNode = htmlc.clean(file, encoding);

            // serialize to xml file
            new PrettyXmlSerializer(props).writeToFile(tagNode, file.getAbsolutePath(), encoding);
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    }

    public static void cleanHtmlFile(File in, File out, String encoding) {
        try {
            CleanerProperties props = new CleanerProperties();

            // set some properties to non-default values
            props.setRecognizeUnicodeChars(false);
            props.setOmitComments(true);

            // do parsing
            HtmlCleaner htmlc = new HtmlCleaner(props);
            TagNode tagNode = htmlc.clean(in, encoding);

            // serialize to xml file
            new PrettyXmlSerializer(props).writeToFile(tagNode, out.getAbsolutePath(), encoding);
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
    }
    
    //Suppress leading and trailing white spaces and replace invalid characters in filename by white spaces
    public static String encodeFilename(String filename)
    {
        return filename.trim()
                .replace("\\", " ")
                .replace("/", " ")
                .replace(":", " ")
                .replace("*", " ")
                .replace("?", " ")
                .replace("\"", " ")
                .replace("<", " ")
                .replace(">", " ")
                .replace("|", " ");
    }
}
