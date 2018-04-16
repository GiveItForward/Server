package giveitforwardtests;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.List;
import java.util.Properties;


public class NLPTest {

    public static String text = "Kenzie Elliott In 2017, he went to Paris, France with his friend Maria Nakamura who is" +
            " from the United States also Xi Jinping who lived in Utah and utah, salt lake city " +
            "Provo Salt Lake City great lakes Murray Sandy Taylorsville West Valley";

    public static void main(String[] args) {
        boolean useRegexner = false;

        Properties props = new Properties();
        if (useRegexner) {
            props.put("annotators", "tokenize,ssplit,pos,lemma,ner,regexner");
//            props.put("regexner.mapping.header", "pattern,overwrite");
            props.put("regexner.mapping", "locations.txt");
        } else {
            props.put("annotators", "tokenize,ssplit,pos,lemma,ner");
        }

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        CoreSentence sentence = document.sentences().get(0);

        List<String> nerTags = sentence.nerTags();
        System.out.println(nerTags);
        for (int i = 0; i < nerTags.size(); i++) {
            if (!nerTags.get(i).equals("O")) {
                System.out.println(nerTags.get(i) + ": " + sentence.tokens().get(i).value());
            }
        }
    }
}
