package ru.nilebox.nutch;

import java.util.Collection;
import org.apache.nutch.parse.HTMLMetaTags;
import org.apache.nutch.parse.Parse;
import org.apache.nutch.parse.ParseFilter;
import org.apache.nutch.storage.WebPage;
import org.w3c.dom.DocumentFragment;

/**
 *
 * @author
 * nile
 */
public class FieldExtractor implements ParseFilter {

	public Parse filter(String string, WebPage wp, Parse parse, HTMLMetaTags htmlmt, DocumentFragment df) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public Collection<WebPage.Field> getFields() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}

