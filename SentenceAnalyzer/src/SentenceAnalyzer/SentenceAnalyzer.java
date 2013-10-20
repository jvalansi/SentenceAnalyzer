package SentenceAnalyzer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author jvalansi
 * 
 * Should analyze a sentence to it's components
 */
public class SentenceAnalyzer {

	/**
	 * 
	 * gets sentence, and tries to iterate over possible senses until a grammatically correct sentence is found  
	 * 
	 * @param s - the sentence
	 * @param credits - the difference from most possible sense
	 * @return
	 */
	public static Sentence findGoodSentence(Sentence s, int credits){
		List<Sentence> sl = new ArrayList<Sentence>();
		List<Sentence> nsl = new ArrayList<Sentence>();		
		sl.add(s);
		for (int i = 0; i < credits; i++) {
			System.out.println("credit: "+i);
			if(sl.size() == 0){
				System.out.println("No more senses found");
				return null;
			}
			for (Iterator<Sentence> iterator = sl.iterator(); iterator.hasNext();) {
				Sentence sentence = (Sentence) iterator.next();
				if(checkGrammar(sentence)){
					return sentence;
				} else {
					nsl.addAll(sentence.getNextPossibleSenses());
				}
			}
			sl = new ArrayList<Sentence>(nsl);
			nsl.clear();
		}
		return null;
	}
	
	private static boolean checkGrammar(Sentence sentence) {
//		System.out.println(sentence);
		return true;
	}

	private static Sentence answer(Sentence question, String info){
//		reverse sentence: what is that -> that is what
		Sentence answer = question.reverseSentence();
		System.out.println(answer);
//		replace interrogative word
		answer = answer.replaceWH(info);
		return answer;
	}
	
	
	/**
	 * Transform sentence to Sentence object
	 * 
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		Sentence s; 
		String st;

//		st = "Dani ate the apple that stood on the table";
//		s = new Sentence(st);
//		findGoodSentence(s, 1);
//		System.out.println(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));
//		st = "he did the thing and was on the thing";
//		s = new Sentence(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));

		String info = "Dani sees trains.";
		String q = "What does Dani see?";
		s = new Sentence(q);
		System.out.println(s);
		System.out.println(answer(s, info));
		
		
//		st = "I've watched through his eyes, I've listened through his ears, and tell you he's the one";
//		s = new Sentence(st);
//		System.out.println(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));
//		st = "I  have been in his things , I have been in his things , and [I] (tell) you [that] he is the thing";
//		s = new Sentence(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));

//		st = "He could feel his legs thrashing, and his hands were clenching each other, wringing each other so tightly that they ached";
//		s = new Sentence(st);
//		System.out.println(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));
//		st = "He would (feel) his things doing , and his things were doing them , doing them very well that they were";				
//		s = new Sentence(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));

//		st = "Of course, they had bigger keyboards -- but how could their thick fingers draw a fine line, the way Ender could, a thin line so precise that he could make it spiral seventy-nine times from the center to the edge of the desk without the lines ever touching or overlapping";
//		s = new Sentence(st);
//		System.out.println(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));
//		st = "well , they were good things -- and how would their good things do a good thing , the thing he would , a good thing so good (that) he would (make) it do good things (from) the thing (to) the thing (of) the thing (without) the things well being or being?";
//		s = new Sentence(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));
//
//		st = "For a moment, the others backed away and Stilson lay motionless";
//		s = new Sentence(st);
//		System.out.println(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));
//		st = "in a thing, the things were well and he was well";
//		s = new Sentence(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));
//
//		st = "I'm sorry, Ender";
//		s = new Sentence(st);
//		System.out.println(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));
//		st = "I am good, you";
//		s = new Sentence(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));
//
//		st = "it's better to play the war games, and have a better chance of surviving when the buggers came again";
//		s = new Sentence(st);
//		System.out.println(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));
//		st = "it is good to do the thing things , and be a good thing in being (in-the-thing-that) the things were well";
//		s = new Sentence(st);
////		System.out.println(s);
//		System.out.println(findGoodSentence(s, 1));
//
//		st = "I'm dying to see how he handles them, too.";
//		s = new Sentence(st);
//		System.out.println(st);
//		System.out.println(s);
//		st = "I am (dying) to be how he does them, well";
//		s = new Sentence(st);
//		System.out.println(s);
//
//		st = "It only works, during your regularly scheduled practice sessions.";
//		s = new Sentence(st);
//		System.out.println(st);
//		System.out.println(s);
//		st = "it (only) is, in your well being thing things";
//		s = new Sentence(st);
//		System.out.println(s);
//
//		st = "His belly spilled over both armrests now, even when he sat upright.";		
//		s = new Sentence(st);
//		System.out.println(st);
//		System.out.println(s);
//		st = "his thing was in good things now, and (in-the-thing-that) he was well";
//		s = new Sentence(st);
//		System.out.println(s);

	}

}
