package SentenceAnalyzer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
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
	private LinkedHashSet<WT> possibleTypes = new LinkedHashSet<WT>();
	
	public Word(String string) {
		this.setWs(string);
		setPossibleTypes(string);
		this.setWt(this.possibleTypes);
	}

	public Word(Word w) {
		this.setWs(w.ws);
		for (Iterator<WT> iterator = w.possibleTypes.iterator(); iterator.hasNext();) {
			WT wt = iterator.next();
			this.possibleTypes.add(wt);
		}
		this.setWt(this.possibleTypes);
	}

	private void setPossibleTypes(String string) {
//		getClosedClassTypes(string);
//		getOpenClassTypes(string);
		possibleTypes.addAll(getWiktionaryTypes(string));
		if(possibleTypes.isEmpty()){
			possibleTypes.addAll(getWiktionaryTypes(string.toLowerCase()));
		}
		if(possibleTypes.isEmpty() & Character.isUpperCase(string.charAt(0))){ // A NAME
			possibleTypes.add(WT.PRON);
		}
	}
	
	
	private LinkedHashSet<WT> getWiktionaryTypes(String string) {
		LinkedHashSet<WT> lhs = new LinkedHashSet<WT>();
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
							lhs.add(WT.NOUN);						
						}
						if(!e.getElementsMatchingOwnText("Verb").isEmpty()){
							lhs.add(WT.VERB);
						} 
						if(!e.getElementsMatchingOwnText("Adjective").isEmpty()){
							lhs.add(WT.ADJ);					 
						} 
						if(!e.getElementsMatchingOwnText("Adverb").isEmpty()){
							lhs.add(WT.ADV);
						} 
						if(!e.getElementsMatchingOwnText("Determiner").isEmpty()){
							lhs.add(WT.DET);
						}
						if(!e.getElementsMatchingOwnText("Article").isEmpty()){
							lhs.add(WT.DET);
						} 
						if(!e.getElementsMatchingOwnText("Pronoun").isEmpty()){
							lhs.add(WT.PRON);
						} 
						if(!e.getElementsMatchingOwnText("Preposition").isEmpty()){
							lhs.add(WT.PREP);
						} 
						if(!e.getElementsMatchingOwnText("Conjunction").isEmpty()){
							lhs.add(WT.CONJ);
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

	public void removeFirstWT() {
		if(!this.possibleTypes.isEmpty()){
			remove(this.possibleTypes.iterator().next());
		}
	}

	public void remove(WT wt) {
		this.possibleTypes.remove(wt);
		setWt(this.possibleTypes);
	}

	public WordType getWt() {
		return wt;
	}

	public void setWt(Set<WT> pt) {
		if(!pt.isEmpty()){
			WT wt = (WT) pt.iterator().next();
			switch (wt) {
			case NOUN:
				this.wt = new Noun();			
				break;
			case VERB:
				this.wt = new Verb();			
				break;
			case ADJ:
				this.wt = new Adj();			
				break;
			case ADV:
				this.wt = new Adv();			
				break;
			case CONJ:
				this.wt = new Conj();			
				break;
			case DET:
				this.wt = new Det();			
				break;
			case PREP:
				this.wt = new Prep();			
				break;
			case PRON:
				this.wt = new Pron();			
				break;
			default:
				this.wt = null;			
				break;
			}
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
		for (Iterator<WT> iterator = this.possibleTypes.iterator(); iterator.hasNext();) {
			WT type = (WT) iterator.next();
			if(s.length()>0){
				s += "/";
			}
			s += type.name();
			
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
