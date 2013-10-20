package SentenceAnalyzer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import WordType.*;

public class Word {

	private WordType wt;
	private String ws;
	private LinkedHashSet<WordType> possibleTypes = new LinkedHashSet<WordType>();
	
	public Word(String string) {
		this.setWs(string);
		setPossibleTypes(string);
		this.setWordType(this.possibleTypes);
	}

	public Word(Word w) {
		this.setWs(w.ws);
		for (Iterator<WordType> iterator = w.possibleTypes.iterator(); iterator.hasNext();) {
			WordType wt = iterator.next();
			this.possibleTypes.add(wt);
		}
		this.setWordType(this.possibleTypes);
	}

	private void setPossibleTypes(String string) {
//		getClosedClassTypes(string);
//		getOpenClassTypes(string);
		possibleTypes.addAll(WiktionaryParser.getTypes(string));
		if(possibleTypes.isEmpty()){
			possibleTypes.addAll(WiktionaryParser.getTypes(string.toLowerCase()));
		}
		if(possibleTypes.isEmpty() & Character.isUpperCase(string.charAt(0))){ // A NAME
			possibleTypes.add(new Pron());
		}
	}
	
	
	private Collection<? extends WordType> getWiktionaryTypes(String string) {
		
		
		LinkedHashSet<WordType> lhs = new LinkedHashSet<WordType>();
        try {
			Document doc = Jsoup.connect("http://en.wiktionary.org/wiki/"+string).get();
		    
		    Elements h2 = doc.getElementsByTag("h2");
			for (Iterator<Element> iterator = h2.iterator(); iterator.hasNext();) {
				Element element = (Element) iterator.next();
				Elements eng = element.getElementsMatchingText("English");
				if(eng.size()>0){
					Element e = eng.get(0).nextElementSibling();
					while(e!=null && e.tagName()!="h2"){
						if(!e.getElementsMatchingOwnText("Noun").isEmpty()){
							lhs.add(new Noun());						
						}
						if(!e.getElementsMatchingOwnText("Verb").isEmpty()){
							lhs.add(new Verb());
							while(e.tagName()!="ol"){
								System.out.println(e.text());
								e=e.nextElementSibling();
							}
						} 
						if(!e.getElementsMatchingOwnText("Adjective").isEmpty()){
							lhs.add(new Adj());					 
						} 
						if(!e.getElementsMatchingOwnText("Adverb").isEmpty()){
							lhs.add(new Adv());
						} 
						if(!e.getElementsMatchingOwnText("Determiner").isEmpty()){
							lhs.add(new Det());
						}
						if(!e.getElementsMatchingOwnText("Article").isEmpty()){
							lhs.add(new Det());
						} 
						if(!e.getElementsMatchingOwnText("Pronoun").isEmpty()){
							lhs.add(new Pron());
						} 
						if(!e.getElementsMatchingOwnText("Preposition").isEmpty()){
							lhs.add(new Prep());
						} 
						if(!e.getElementsMatchingOwnText("Conjunction").isEmpty()){
							lhs.add(new Conj());
						}
						e=e.nextElementSibling();
					}
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lhs;
		
	}
	
	
	public int possibleTypesSize() {
		return this.possibleTypes.size();
	}

	public void removeFirstWordType() {
		if(!this.possibleTypes.isEmpty()){
			remove(this.possibleTypes.iterator().next());
		}
	}

	public void remove(WordType wt) {
		this.possibleTypes.remove(wt);
		setWordType(this.possibleTypes);
	}

	public WordType getWordType() {
		return wt;
	}

	public void setWordType(Set<WordType> pt) {
		if(!pt.isEmpty()){
			wt = pt.iterator().next();
		}
	}

	public String getWs() {
		return ws;
	}

	public void setWs(String ws) {
		this.ws = ws;
	}		

	public String printPossibleTypes(){
		String s = "";
		for (Iterator<WordType> iterator = this.possibleTypes.iterator(); iterator.hasNext();) {
			WordType type = (WordType) iterator.next();
			if(s.length()>0){
				s += "/";
			}
			s += type.getClass().getSimpleName();
			
		}
		return s;
	}

	public static void main(String[] args) {
		Word w = new Word("Through");
		System.out.println(w.printPossibleTypes());
		if(w.wt != null){
			System.out.println(w.wt.getClass().getName());
		}
	}




}
