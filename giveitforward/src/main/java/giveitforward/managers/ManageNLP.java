package giveitforward.managers;

import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import giveitforward.models.StanfordNLP;

import java.util.List;

public class ManageNLP {

    public ManageNLP() {}

    public StanfordNLP parseText(String text, boolean useRegexner) {
        StanfordNLP ans = new StanfordNLP(false, false);

        StanfordCoreNLP pipeline = NLPSingleton.getPipelineInstance(useRegexner);
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);

        for (CoreSentence sentence : document.sentences()) {
            List<String> nerTags = sentence.nerTags();
            for (String tag : nerTags) {
                if (tag.equals("CITY") || tag.equals("LOCATION")) {
                    ans.setCity(true);
                }
                if (tag.equals("PERSON")) {
                    ans.setPerson(true);
                }
                // quit early if we find both
                if (ans.getCity() && ans.getPerson()) {
                    return ans;
                }
            }
        }
        return ans;
    }
}
