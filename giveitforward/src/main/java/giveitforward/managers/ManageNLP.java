package giveitforward.managers;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import giveitforward.models.StanfordNLP;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

public class ManageNLP {

    public ManageNLP() {}

    public StanfordNLP parseText(String text, boolean useRegexner) {
        StanfordNLP ans = new StanfordNLP(null, null);

        StanfordCoreNLP pipeline = NLPSingleton.getPipelineInstance(useRegexner);
        CoreDocument document = new CoreDocument(WordUtils.capitalizeFully(text));
        pipeline.annotate(document);

        ArrayList<ArrayList<String>> cities = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> people = new ArrayList<ArrayList<String>>();

        for (CoreSentence sentence : document.sentences()) {
            List<String> nerTags = sentence.nerTags();
            for (int i = 0; i < nerTags.size();) {
                if (nerTags.get(i).equals("CITY") || nerTags.get(i).equals("PERSON")) {
                    ArrayList<String> match = new ArrayList<String>();

                    // LOOK FOR CITIES
                    while (i < nerTags.size() && nerTags.get(i).equals("CITY")) {
                        match.add(sentence.tokens().get(i++).value());
                    }
                    if (!match.isEmpty()) {
                        cities.add(match);
                        match = new ArrayList<String>();
                    }

                    // LOOK FOR PEOPLE
                    while (i < nerTags.size() && nerTags.get(i).equals("PERSON")) {
                        match.add(sentence.tokens().get(i++).value());
                    }
                    if (!match.isEmpty()) {
                        people.add(match);
                        match = new ArrayList<String>();
                    }
                } else {
                    i++;
                }
            }
        }

        if (!cities.isEmpty()) {
            ans.setCities(concatNames(cities));
        }
        if (!people.isEmpty()) {
            ans.setPeople(concatNames(people));
        }

        return ans;
    }

    private ArrayList<String> concatNames(ArrayList<ArrayList<String>> matches) {
        ArrayList<String> finalList = new ArrayList<String>();
        for (int i = 0; i < matches.size(); i++) {
            String concat = "";
            for (int j = 0; j < matches.get(i).size(); j++) {
                concat += matches.get(i).get(j);
                if (j < matches.get(i).size() - 1) {
                    concat += " ";
                }
            }
            if (!concat.equals(""))
                finalList.add(concat);
        }
        return finalList;
    }
}
