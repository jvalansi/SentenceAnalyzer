package SentenceAnalyzer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import WordType.Verb;
import WordType.WordType;


public class Sentence {
//	private List<Word> words = new ArrayList<Word>();
	private SentenceType type;
	private List<Word> subject = new ArrayList<Word>();
	private List<Word> object = new ArrayList<Word>();
	private Word verb;
	
	public Sentence(String string) {
		type = getSentenceType(string);
		String s = removePunctuation(string);
		s = separatePunctuation(s);
		s = fixConjunctions(s);
		List<Word> words = separateWords(s);
//		this.fixAmbiguity((ArrayList<Word>) this.words);
		seperateSubjectObject(words);
	}

	private String removePunctuation(String s) {
		s = s.replaceAll("\\.", "");
		s = s.replaceAll("!", "");
		s = s.replaceAll("\\?", "");
		return s;
	}

	private SentenceType getSentenceType(String string) {
		if(string.endsWith("?")){
			return SentenceType.INTERROGATIVE;
		} else if(string.endsWith("!")){
			return SentenceType.EXCLAMATORY;
		} else {
			return SentenceType.DECLARATIVE;
		}
	}

	public Sentence(Sentence sentence) {
		for (Iterator<Word> iterator = sentence.subject.iterator(); iterator.hasNext();) {
			Word w = iterator.next();
			this.subject.add(new Word(w));
		}
		this.verb = sentence.verb;
		for (Iterator<Word> iterator = sentence.object.iterator(); iterator.hasNext();) {
			Word w = iterator.next();
			this.object.add(new Word(w));
		}
	}

	private String separatePunctuation(String s) {
		s = s.replaceAll(",", " ,");
		return s;
	}


	private String fixConjunctions(String s) {
		Scanner sc = new Scanner(s);
		String ns = new String();
		while (sc.hasNext()) {
			String w = sc.next();
			if(w.contains("'")){
				w=w.replaceAll("ain't$", "is not");
				w=w.replaceAll("n't$", " not");
				w=w.replaceAll("^let's$", "let us");
				w=w.replaceAll("'m$", " am");
				w=w.replaceAll("'re$", " are");
				w=w.replaceAll("^he's$", "he is");
				w=w.replaceAll("^she's$", "she is");
				w=w.replaceAll("^it's$", "it is");
				w=w.replaceAll("'ve$", " have");
				w=w.replaceAll("'d$", " had");
				w=w.replaceAll("'ll$", " will");
				w=w.replaceAll("^o'", "of ");
//				w=w.replaceAll("^'t", "it ");
				w=w.replaceAll("'em$", " them");
			}
			ns += w;
			if(sc.hasNext()){
				ns += " ";
			}
		}		
		sc.close();
		return ns;
	}

	private  List<Word> separateWords(String string) {
		List<Word> words = new ArrayList<Word>();
		Scanner s = new Scanner(string);
		while (s.hasNext()) {
			String w = s.next();
			words.add(new Word(w));
		}
		s.close();
		return words;
	}

	private List<Word> joinSubjectObject() {
		List<Word> words = new ArrayList<Word>();
		words.addAll(subject);
		words.add(verb);
		words.addAll(object);
		return words;
	}

	private void seperateSubjectObject(List<Word> words) {
		int verbIndex = getVerbIndex(words);
		for (int i = 0; i < verbIndex; i++) {
			this.subject.add(words.get(i));
		}
		if(verbIndex>=0){
			this.verb = words.get(verbIndex);
		}
		for (int i = verbIndex+1; i < words.size(); i++) {
			this.object.add(words.get(i));
		}
	}

	private int getVerbIndex(List<Word> words) {
		for (int i = 0; i < words.size(); i++) {
			WordType wordType = words.get(i).getWordType();
			if(wordType==null){
				continue;
			}
			if(wordType.getClass().getName().contains("Verb")){
				return i;
			}
		}
		return -1;
	}
	
/*
//	private void fixAmbiguity(ArrayList<Word> words) {
//		Word prevWord = null;
//		for (int i = 0; i < words.size(); i++) {
//			Word word = words.get(i);
//			if(i+1 < words.size()){
//				Word nextWord = words.get(i+1);
//			}
//			//  if x is a then y is not b
//			//	if y is b then x is not a
//			never(prevWord, "Det", word, "Verb");
//			never(prevWord, "Noun", word, "Det");
//			never(prevWord, "Adj", word, "Det");
//			// if x is not a and y is not b then z is not c
//
//			prevWord = word;
//		}
//	}
	
//	private void never(Word word1, String string1, Word word2, String string2) {
//		if(word1 == null | word2 == null){
//			return;
//		} else {
//			WordType type1 = word1.getWt();
//			WordType type2 = word2.getWt();
//			if(type1 != null && type1.getClass().getName() == string1){ // TODO - fix
//				word2.remove(WT.valueOf(string2.toUpperCase()));
//			}
//			if(type2 != null && type2.getClass().getName() == string2){
//				word1.remove(WT.valueOf(string1.toUpperCase()));
//			}
//		}
//	}
*/
	
	public Sentence reverseSentence() {
		Sentence reverseSentence = new Sentence(this);
		if(((Verb)reverseSentence.verb.getWordType()).getBaseForm().matches("do")){
			reverseSentence.verb = null;
		}
		List<Word> temp = reverseSentence.object;
		reverseSentence.object = reverseSentence.subject;
		reverseSentence.subject = temp;
		return reverseSentence;
	}
	
	public Sentence replaceWH(String info) {
		List<Word> words = joinSubjectObject();
		String s = this.printWords(words,1);
		String [] wh = {"What","Where","When","Why","How","Who","Which"};
		for (int i = 0; i < wh.length; i++) {
			s = s.replace(wh[i], "").trim();			
		}
		String[] ss = info.split("([\\.!?])\\s+");
		for (int i = 0; i < ss.length; i++) {
			if(ss[i].contains(s)){
				Sentence sn = new Sentence(ss[i]);
				return sn;
			}
		}
		return null;
	}

//	Print functions
	
	public String printWords(List<Word> wl, int width) {
		String s = "";
		for (int i = 0; i < wl.size(); i++) {
			Word word = wl.get(i);
			String wordString = "";
			if(word!=null){
				wordString = word.getWs();
			}
			s += String.format("%"+width+"s", wordString);
			s += " ";
		}
		return s;
	}
	
	public String printWordTypes(List<Word> wl, int width) {
		String s = "";
		for (int i = 0; i < wl.size(); i++) {
			Word word = wl.get(i);
			String wordTypeString = "Null";
			if(word!=null){
				WordType wt = word.getWordType();
				if(wt != null){
					wordTypeString = wt.getClass().getSimpleName();
				}
			}
			s += String.format("%"+width+"s", wordTypeString);
			s += " ";
		}
		return s;
	}

	public String printWordPossibleTypes(List<Word> wl,int width) {
		String s = "";
		for (int i = 0; i < wl.size(); i++) {
			Word word = wl.get(i);
			String wordTypesString = "Null";
			if(word!=null){
				wordTypesString = word.printPossibleTypes();
			}
			s += String.format("%"+width+"s", wordTypesString);
			s += " ";

		}
		return s;
	}

	@Override
	public String toString() {
		String s = "";
		List<Word> words = joinSubjectObject();
		int width = -25;
		s+=printWords(words,width)+"\n";
		s+=printWordTypes(words,width)+"\n";
		s+=printWordPossibleTypes(words,width)+"\n";
		return s;
	}

	/**
	 * @return a list of sentences in which one of the words has altered it's type
	 */
	public ArrayList<Sentence> getNextPossibleSenses() {
		ArrayList<Sentence> sl = new ArrayList<Sentence>();
		List<Word> words = joinSubjectObject();
		for (int i = 0; i < words.size(); i++) {
			if(words.get(i).possibleTypesSize() > 1){
				Sentence s = new Sentence(this);
				List<Word> sWords = s.joinSubjectObject();
				sWords.get(i).removeFirstWordType();
				sl.add(s);
			}
		}
		return sl;
	}


}
