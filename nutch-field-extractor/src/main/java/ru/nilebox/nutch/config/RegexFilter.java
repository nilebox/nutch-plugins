package ru.nilebox.nutch.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author nile
 */
public class RegexFilter {
	private final Pattern[] acceptPatterns;
	private final Pattern[] rejectPatterns;
	
	public RegexFilter(Pattern[] acceptPatterns, Pattern[] rejectPatterns) {
		this.acceptPatterns = acceptPatterns;
		this.rejectPatterns = rejectPatterns;
	}
	
	public static RegexFilter load(InputStream stream) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
		String line;
		List<String> acceptPatternStrings = new ArrayList<String>();
		List<String> rejectPatternStrings = new ArrayList<String>();
		while ((line = reader.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("#")) {
				continue;
			}
			if (line.startsWith("+")) {
				acceptPatternStrings.add(line.substring(1));
			}
			else if (line.startsWith("-")) {
				rejectPatternStrings.add(line.substring(1));
			} else {
				throw new RuntimeException("Line should starts with '+' or '-'");
			}
		}
		reader.close();
		
		Pattern[] acceptPatterns = new Pattern[acceptPatternStrings.size()];
		for (int i=0; i<acceptPatternStrings.size(); i++) {
			String acceptPatternString = acceptPatternStrings.get(i);
			Pattern pattern = Pattern.compile(acceptPatternString);
			acceptPatterns[i] = pattern;
		}
		
		Pattern[] rejectPatterns = new Pattern[rejectPatternStrings.size()];
		for (int i=0; i<rejectPatternStrings.size(); i++) {
			String rejectPatternString = rejectPatternStrings.get(i);
			Pattern pattern = Pattern.compile(rejectPatternString);
			rejectPatterns[i] = pattern;
		}
		
		return new RegexFilter(acceptPatterns, rejectPatterns);
	}
	
	public static RegexFilter loadFromResource(String path) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		InputStream stream = XPathFilter.class.getClassLoader().getResourceAsStream(path);
		RegexFilter filter = load(stream);
		stream.close();
		return filter;
	}
	
	public boolean shouldVisit(String url) {
		for (Pattern p : acceptPatterns) {
			if (p.matcher(url).matches())
				return true;
		}
		
		for (Pattern p : rejectPatterns) {
			if (p.matcher(url).matches())
				return false;
		}
		
		return true;
	}
}
