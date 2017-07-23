package name.paulshipley.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import name.paulshipley.Common.ExceptionHandler;
import name.paulshipley.Common.XML.StateAwareHandler;
import name.paulshipley.Common.XML.TagHandler;

/**
 * StoreParser is an example of using the StateAwareHandler class.
 * 
 * @author Paul Shipley (pshipley@melbpc.org.au)
 * @version $Id: StoreParser.java,v 1.3 2010/03/26 05:55:20 paul Exp $
 */
public class StoreParser {

	/** The list of Items. */
	private ArrayList<Item> itemlist = new ArrayList<Item>();

	/**
	 * Item class.
	 */
	private class Item {

		/** The item barcode. */
		private String barcode;

		/** The item colour. */
		private String colour;

		/** The item description. */
		private String description;

		/**
		 * Gets the barcode.
		 * 
		 * @return the barcode
		 */
		public String getBarcode() {
			return barcode;
		}

		/**
		 * Sets the barcode.
		 * 
		 * @param barcode
		 *            the new barcode
		 */
		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}

		/**
		 * Gets the colour.
		 * 
		 * @return the colour
		 */
		public String getColour() {
			return colour;
		}

		/**
		 * Sets the colour.
		 * 
		 * @param colour
		 *            the new colour
		 */
		public void setColour(String colour) {
			this.colour = colour;
		}

		/**
		 * Gets the description.
		 * 
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}

		/**
		 * Sets the description.
		 * 
		 * @param description
		 *            the new description
		 */
		public void setDescription(String description) {
			this.description = description;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Item [barcode=" + barcode + ", colour=" + colour
					+ ", description=" + description + "]";
		}
	}

	/**
	 * StoreTag class handles the &lt;store&gt; elements.
	 */
	private class StoreTag extends TagHandler {

		/*
		 * (non-Javadoc)
		 * 
		 * @see name.paulshipley.Common.XML.TagHandler#tagName()
		 */
		public String tagName() {
			return "store";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see name.paulshipley.Common.XML.TagHandler#start()
		 */
		public void start() {
			System.out.println("Start store");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see name.paulshipley.Common.XML.TagHandler#end()
		 */
		public void end() {
			System.out.println("End store");
		}
	}

	/**
	 * ItemTag class handles the &lt;item&gt; elements.
	 */
	private class ItemTag extends TagHandler {

		/** The new item instance. */
		Item i;

		/** A temporary buffer to build the description. */
		StringBuffer description;

		/*
		 * (non-Javadoc)
		 * 
		 * @see name.paulshipley.Common.XML.TagHandler#tagName()
		 */
		public String tagName() {
			return "item";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see name.paulshipley.Common.XML.TagHandler#start()
		 */
		public void start() {
			i = new Item();
			i.setBarcode(this.getAttrib("barcode"));
			i.setColour(this.getAttrib("colour"));
			description = new StringBuffer();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * name.paulshipley.Common.XML.TagHandler#characters(java.lang.String)
		 */
		public void characters(String s) {
			description.append(s.trim());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see name.paulshipley.Common.XML.TagHandler#end()
		 */
		public void end() {
			i.setDescription(description.toString());
			itemlist.add(i);
			System.out.println("Item: " + i.getDescription());
		}
	}

	/**
	 * Use the StateAwareHandler to parse the xml file.
	 * 
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws SAXException
	 *             the SAX exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void parse() throws ParserConfigurationException, SAXException,
			IOException {
		// Configure the SAX event handler
		StateAwareHandler handler = new StateAwareHandler();
		handler.addTagHandler(new StoreParser.StoreTag());
		handler.addTagHandler(new StoreParser.ItemTag());

		// Define validation
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		StreamSource xsd = new StreamSource(StoreParser.class.getResourceAsStream("store.xsd"));
		Schema schema = sf.newSchema(xsd);
		Validator validator = schema.newValidator();

		// Define input as xml file
		InputSource is = new InputSource(StoreParser.class.getResourceAsStream("store.xml"));
		SAXSource saxIn = new SAXSource(is);

		// Output to my handler
		SAXResult saxOut = new SAXResult(handler);

		// Parse and validate
		validator.validate(saxIn, saxOut);
	}

	/**
	 * The main method.
	 * 
	 * @param args the command line args
	 */
	public static void main(String[] args) {
		try {
			// Create a new parser and parse the file
			StoreParser sp = new StoreParser();
			sp.parse();

			// Output the Item list
			System.out.println("Item List");
			Iterator<Item> iit = sp.itemlist.iterator();
			while (iit.hasNext()) {
				Item i = iit.next();
				System.out.println(i.toString());
			}
		} catch (Exception e) {
			ExceptionHandler.handleAndTerminate(e);
		}

		System.exit(0);
	}

}
