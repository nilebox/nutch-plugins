package ru.nilebox.nutch;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.parse.HTMLMetaTags;
import org.apache.nutch.parse.Parse;
import org.apache.nutch.parse.ParseFilter;
import org.apache.nutch.storage.WebPage;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.nilebox.nutch.config.FieldExtractorConfig;

/**
 * Nutch
 * filter
 * implementation
 * that
 * normalizes
 * HTML
 * as
 * XML,
 * extracts
 * fields
 * from
 * content
 * by
 * configured
 * XPath,
 * and
 * adds
 * them
 * to
 * result
 * metadata
 *
 * @author
 * nile
 */
public class FieldExtractorFilter implements ParseFilter {

	private static final Logger logger = LoggerFactory.getLogger(FieldExtractorFilter.class);
	private static final List<String> htmlMimeTypes = Arrays.asList(new String[]{"text/html", "application/xhtml+xml"});
	// Configuration
	private Configuration configuration;
	private FieldExtractorConfig fieldExtractorConfig;
	private Pattern encodingPattern;
	private String defaultEncoding;
	// Internal data
	private HtmlCleaner cleaner;
	private DomSerializer domSerializer;
	private DocumentBuilder documentBuilder;

	public FieldExtractorFilter() {
		init();
	}

	private void init() {

		// Initialize HTMLCleaner
		cleaner = new HtmlCleaner();
		CleanerProperties props = cleaner.getProperties();
		props.setAllowHtmlInsideAttributes(false);
		props.setAllowMultiWordAttributes(true);
		props.setRecognizeUnicodeChars(true);
		props.setOmitComments(true);
		props.setNamespacesAware(false);
		props.setUseCdataForScriptAndStyle(true);

		// Initialize DomSerializer
		domSerializer = new DomSerializer(props);

		// Initialize DOM builder
		try {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			logger.error("Error initializing DOM builder", e);
		}

		// Initialize encoding regex pattern
		String pattern = "\\<meta\\s*http-equiv=[\\\"\\']content-type[\\\"\\']\\s*content\\s*=\\s*[\"']text/html\\s*;\\s*charset=([a-z\\d\\-]*)[\\\"\\'\\>]";
		encodingPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
	}

	private void initConfig() {
		try {
			// Initialize configuration
			fieldExtractorConfig = FieldExtractorConfig.getInstance(configuration);
			defaultEncoding = configuration.get("parser.character.encoding.default", "UTF-8");
		} catch (Exception ex) {
			logger.error("Error initializing configuration", ex);
		}
	}

	@Override
	public Configuration getConf() {
		return configuration;
	}

	@Override
	public void setConf(Configuration configuration) {
		this.configuration = configuration;
		initConfig();
	}

	public Parse filter(String url, WebPage page, Parse parse, HTMLMetaTags metaTags, DocumentFragment doc) {
		byte[] rawContent = page.getContent().array();
		try {
			Document cleanedXmlHtml = documentBuilder.newDocument();
			if (htmlMimeTypes.contains(page.getContentType().toString())) {

				// Create reader so the input can be read in UTF-8
				Reader rawContentReader = new InputStreamReader(new ByteArrayInputStream(rawContent), getCharsetFromContent(rawContent));

				// Use the cleaner to "clean" the HTML and parse it as XML
				TagNode tagNode = cleaner.clean(rawContentReader);
				cleanedXmlHtml = domSerializer.createDOM(tagNode);
			} else if (page.getContentType().toString().contains(new StringBuilder("/xml")) || page.getContentType().toString().contains(new StringBuilder("+xml"))) {

				// Parse as xml - don't clean
				cleanedXmlHtml = documentBuilder.parse(new InputSource(new ByteArrayInputStream(rawContent)));
			}
			
			//TODO: extract fields from document

		} catch (IOException e) {
			// This can never happen because it's an in memory stream
			logger.error("Unexpected I/O error", e);
		} catch (ParserConfigurationException e) {
			System.err.println(e.getMessage());
			logger.error("HTML Cleaning error: " + e.getMessage());
			return parse;
		} catch (SAXException e) {
			System.err.println(e.getMessage());
			logger.error("XML parsing error: " + e.getMessage());
			return parse;
		}

		return parse;
	}

	public Collection<WebPage.Field> getFields() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private String getCharsetFromContent(byte[] rawContent) throws IOException {
		String startContent = new String(rawContent);
		Matcher matcher = encodingPattern.matcher(startContent);
		if (matcher.find()) {
			String charset = matcher.group(1);
			if (Charset.isSupported(charset)) {
				return charset;
			}
		}
		return defaultEncoding;
	}
}
