package giveitforward.models;

import org.json.JSONObject;

public class StanfordNLP extends  Model {
    private boolean city;
    private boolean person;

    public StanfordNLP(boolean c, boolean p) {
        city = c;
        person = p;
    }

    public boolean getCity() {
        return city;
    }

    public boolean getPerson() {
        return person;
    }

    public void setCity(boolean city) {
        this.city = city;
    }

    public void setPerson(boolean person) {
        this.person = person;
    }

    public String asString() { return null; }

    public JSONObject asJSON() {
        JSONObject object = new JSONObject();
        object.put("city", this.city);
        object.put("person", this.person);
        return object;
    }

    public String populateFromJSON(JSONObject obj) { return null; }
}
