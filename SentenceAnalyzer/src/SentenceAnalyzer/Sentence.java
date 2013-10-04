package SentenceAnalyzer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import WordType.WordType;


public class Sentence {
	private String sentenceString;
	private List<Word> words = new ArrayList<Word>();
	private List<Word> subject = new ArrayList<Word>();
	private List<Word> object = new ArrayList<Word>();
	private Word verb;
	
	public Sentence(String string) {
		this.sentenceString = string;
		this.separatePunctuation(this.sentenceString);
		this.fixConjunctions(this.sentenceString);
		this.separateWords(this.sentenceString);
//		this.fixAmbiguity((ArrayList<Word>) this.words);
		seperateSubjectObject();
	}

	private void seperateSubjectObject() {
		int verbIndex = getVerbIndex();
		for (int i = 0; i < verbIndex; i++) {
			this.subject.add(words.get(i));
		}
		this.verb = this.words.get(verbIndex);
		for (int i = verbIndex+1; i < words.size(); i++) {
			this.object.add(words.get(i));
		}
	}

	private int getVerbIndex() {
		for (int i = 0; i < words.size(); i++) {
			if(words.get(i).getWt().getClass().getName().contains("Verb")){
				return i;
			}
		}
		return 0;
	}

	public Sentence(Sentence sentence) {
		this.sentenceString = sentence.sentenceString;
		for (Iterator<Word> iterator = sentence.words.iterator(); iterator.hasNext();) {
			Word w = iterator.next();
			this.words.add(new Word(w));
		}
	}

	private void separatePunctuation(String s) {
		this.sentenceString = s.replaceAll(",", " ,");
	}


	private void fixConjunctions(String s) {
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
		this.sentenceString = ns;
	}

	private void separateWords(String string) {
		Scanner s = new Scanner(string);
		while (s.hasNext()) {
			String w = s.next();
			this.words.add(new Word(w));
		}
		s.close();
	}

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

	public Sentence reverse() {
		List<Word> reversedWords = new ArrayList<Word>();
		for (int i = 0; i < object.size(); i++) {
			reversedWords.add(object.get(i));
		}
		reversedWords.add(verb);
		for (int i = 0; i < subject.size(); i++) {
			reversedWords.add(subject.get(i));
		}
		String reverseString = joinWords(reversedWords);
		Sentence reverseSentence = new Sentence(reverseString);
		return reverseSentence;
	}
	
	private String joinWords(List<Word> words) {
		String s = "";
		for (int i = 0; i < words.size(); i++) {
			s+= words.get(i).getWs()+" ";
		}
		return s;
	}

	public Sentence replaceWH(String info) {
		for (int i = 0; i < words.size(); i++) {
			if(isWH(words.get(i))){
				words.remove(i);
			}
		}
		String s = joinWords(words);
		String[] ss = seperateSentences(info);
		for (int i = 0; i < ss.length; i++) {
			if(ss[i].contains(s)){
				Sentence sn = new Sentence(ss[i]);
				return sn;
			}
		}
		return null;
	}

	private String[] seperateSentences(String s) {
		return s.split("([\\.!?])\\s+");
	}

	private boolean isWH(Word word) {
		String ws = word.getWs();
		String [] wh = {"what","where","when","why","how","who","which"};
		for (int i = 0; i < wh.length; i++) {
			if(ws.equals(wh[i])){
				return true; 
			}
		}
		return false;
	}

//	Print functions
	
	public String printWords(List<Word> wl) {
		String s = "";
		for (int i = 0; i < wl.size(); i++) {
			s  += String.format("%-25s", wl.get(i).getWs());
		}
		return s;
	}
	
	public String printWordTypes(List<Word> wl) {
		String s = "";
		for (int i = 0; i < wl.size(); i++) {
			WordType wt = wl.get(i).getWt();
			if(wt != null){
				s  += String.format("%-25s", wt.getClass().getName());
			} else {
				s += String.format("%-25s", "Null");
			}
		}
		return s;
	}

	public String printWordPossibleTypes(List<Word> wl) {
		String s = "";
		for (int i = 0; i < wl.size(); i++) {
			s  += String.format("%-25s", wl.get(i).printPossibleTypes());
		}
		return s;
	}

	@Override
	public String toString() {
		String s = "";
		s+=printWords(this.words)+"\n";
		s+=printWordTypes(this.words)+"\n";
		s+=printWordPossibleTypes(this.words)+"\n";
		return s;
	}

	/**
	 * @return a list of sentences in which one of the words has altered it's type
	 */
	public ArrayList<Sentence> getNextPossibleSenses() {
		ArrayList<Sentence> sl = new ArrayList<Sentence>();
		for (int i = 0; i < this.words.size(); i++) {
			if(this.words.get(i).possibleTypesSize() > 1){
				Sentence s = new Sentence(this);
				s.words.get(i).removeFirstWT();
				sl.add(s);
			}
		}
		return sl;
	}


}
