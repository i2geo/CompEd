/**
 * 
 */
package net.i2geo.comped.parser;

import org.jdom.Element;

/**
 * @author Martin Homik
 *
 */
public class SampleFactory {
	
	public static Element createTable(String name) {
		Element e = new Element("table");
		e.setAttribute("name", name);
		return e;
	}

	public static Element createColumn(String text) {
		Element e = new Element("column");
		e.setText(text);

		return e;
	}

	private static Element createRow() {
		Element e = new Element("row");
		return e;
	}

	private static Element createValue(String description, String value) {
		Element e = new Element("value");

		e.setAttribute("description", description);
		e.setText(value);

		return e;
	}

	private Element createThingTable() {
		Element e = createTable("thing");

		e = e.addContent(createColumn("id"));
		e = e.addContent(createColumn("date_created"));
		e = e.addContent(createColumn("date_modified"));
		e = e.addContent(createColumn("status"));
		e = e.addContent(createColumn("comment_status"));
		e = e.addContent(createColumn("comment_days"));
		e = e.addContent(createColumn("creator_id"));

		return e;
	}

}
