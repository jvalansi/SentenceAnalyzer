package SentenceAnalyzer;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import WordType.Adj;
import WordType.Adv;
import WordType.Conj;
import WordType.Det;
import WordType.Noun;
import WordType.Prep;
import WordType.Pron;
import WordType.Verb;
import WordType.WordType;

public class WiktionaryParser {
		
	public static Collection<? extends WordType> getTypes(String string) {
		LinkedHashSet<WordType> lhs = new LinkedHashSet<WordType>();
        try {
			Document doc = Jsoup.connect("http://en.wiktionary.org/wiki/"+string).get();
			Element english = doc.getElementById("English");
			if(english == null){
				return lhs;
			}
			Elements englishContent = getContent(english.parent());
			for (Element el : englishContent) {
				if(!el.getElementsMatchingOwnText("Noun").isEmpty()){
					lhs.add(new Noun());						
				}
				if(!el.getElementsMatchingOwnText("Verb").isEmpty()){
					Verb v = new Verb();
					Elements verbContent = getContent(el);
					String baseForm = getBaseForm(verbContent);
					if(baseForm==""){
						baseForm = string;
					}
					v.setBaseForm(baseForm);
					lhs.add(v);
					
				} 
				if(!el.getElementsMatchingOwnText("Adjective").isEmpty()){
					lhs.add(new Adj());					 
				} 
				if(!el.getElementsMatchingOwnText("Adverb").isEmpty()){
					lhs.add(new Adv());
				} 
				if(!el.getElementsMatchingOwnText("Determiner").isEmpty()){
					lhs.add(new Det());
				}
				if(!el.getElementsMatchingOwnText("Article").isEmpty()){
					lhs.add(new Det());
				} 
				if(!el.getElementsMatchingOwnText("Pronoun").isEmpty()){
					lhs.add(new Pron());
				} 
				if(!el.getElementsMatchingOwnText("Preposition").isEmpty()){
					lhs.add(new Prep());
				} 
				if(!el.getElementsMatchingOwnText("Conjunction").isEmpty()){
					lhs.add(new Conj());
				}				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lhs;
	}

	private static String getBaseForm(Elements verbContent) {
		String baseForm = "";
		for (Element element : verbContent) {
			if(element.tagName() == "ol"){
				String definition = element.child(0).text();
				if(definition.matches(".* form of \\w+")){
					baseForm = definition.replaceAll("^.*? (\\w+)$", "$1");
				}
			}
		}
		return baseForm;
	}

	private static Elements getContent(Element title) {
		if(title == null){
			return null;
		}
		Element el = title.nextElementSibling();
		Elements els = new Elements(); 
		while(el!=null && tagLevel(el)>tagLevel(title)){
			els.add(el);
			el = el.nextElementSibling();
		}
		return els;
	}

	private static int tagLevel(Element el) {
		Pattern p = Pattern.compile("\\d");
		Matcher m = p.matcher(el.tagName());
		if(m.find()){
			return Integer.parseInt(m.group());
		} else {
			return Integer.MAX_VALUE;
		}
		
	}

}
