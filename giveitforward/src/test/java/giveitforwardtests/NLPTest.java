//package giveitforwardtests;
//
//import edu.stanford.nlp.pipeline.CoreDocument;
//import edu.stanford.nlp.pipeline.CoreSentence;
//import edu.stanford.nlp.pipeline.StanfordCoreNLP;
//import edu.stanford.nlp.pipeline.TokensRegexNERAnnotator;
//import org.apache.commons.lang3.text.WordUtils;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Properties;
//
//
//public class NLPTest {
//
//    public static String text = "Hello my name is Makenzie Elliott but you can call me Isabelle, " +
//            "I am from St George, and sandy but was born in provo and i commute to draper.";
//
//    public static void main(String[] args) {
//        boolean useRegexner = true;
//
//        Properties props = new Properties();
//        if (useRegexner) {
//            props.put("annotators", "tokenize,ssplit,pos,lemma,ner,regexner");
////            props.put("regexner.mapping.header", "pattern,ner,overwrite,normalized,priority,group");
//            props.put("regexner.mapping", "regexner/utah_mappings.txt");
//        } else {
//            props.put("annotators", "tokenize,ssplit,pos,lemma,ner");
//        }
//
//        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
//        CoreDocument document = new CoreDocument(WordUtils.capitalizeFully(text));
//        pipeline.annotate(document);
//
//        CoreSentence sentence = document.sentences().get(0);
//
//        List<String> nerTags = sentence.nerTags();
//        System.out.println(nerTags);
//        ArrayList<ArrayList<String>> cities = new ArrayList<ArrayList<String>>();
//        ArrayList<ArrayList<String>> people = new ArrayList<ArrayList<String>>();
//        ArrayList<ArrayList<String>> locations = new ArrayList<ArrayList<String>>();
//        for (int i = 0; i < nerTags.size();) {
//            // LOOK FOR CITIES
//            if (nerTags.get(i).equals("CITY") || nerTags.get(i).equals("PERSON") || nerTags.get(i).equals("LOCATION")) {
//                ArrayList<String> match = new ArrayList<String>();
//                while (i < nerTags.size() && nerTags.get(i).equals("CITY")) {
//                    match.add(sentence.tokens().get(i++).value());
//                }
//                if (!match.isEmpty()) {
//                    cities.add(match);
//                    match = new ArrayList<String>();
//                }
//
//                // LOOK FOR PEOPLE
//                while (i < nerTags.size() && nerTags.get(i).equals("PERSON")) {
//                    match.add(sentence.tokens().get(i++).value());
//                }
//                if (!match.isEmpty()) {
//                    people.add(match);
//                    match = new ArrayList<String>();
//                }
//
//                // LOOK FOR LOCATIONS
//                while (i < nerTags.size() && nerTags.get(i).equals("LOCATION")) {
//                    match.add(sentence.tokens().get(i++).value());
//                }
//                if (!match.isEmpty())
//                    locations.add(match);
//            } else {
//                i++;
//            }
//        }
//
//        printConcatNames(cities, "CITY");
//        printConcatNames(people, "PERSON");
//        printConcatNames(locations, "LOCATION");
//    }
//
//    private static void printConcatNames(ArrayList<ArrayList<String>> matches, String tag) {
//        for (int i = 0; i < matches.size(); i++) {
//            String concat = "";
//            for (int j = 0; j < matches.get(i).size(); j++) {
//                concat += matches.get(i).get(j);
//                if (j < matches.get(i).size() - 1) {
//                    concat += " ";
//                }
//            }
//            System.out.println(tag + ":    " + concat);
//        }
//    }
//}
