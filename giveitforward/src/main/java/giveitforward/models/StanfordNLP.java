package giveitforward.models;

import org.json.JSONObject;

import java.util.ArrayList;

public class StanfordNLP extends  Model {
    private ArrayList<String> cities;
    private ArrayList<String> people;

    public StanfordNLP(ArrayList<String> c, ArrayList<String> p) {
        cities = c;
        people = p;
    }

    public ArrayList<String> getCities() {
        return cities;
    }

    public ArrayList<String> getPeople() {
        return people;
    }

    public void setCities(ArrayList<String> cities) {
        this.cities = cities;
    }

    public void setPeople(ArrayList<String> people) {
        this.people = people;
    }

    public String asString() { return null; }

    public JSONObject asJSON() {
        JSONObject object = new JSONObject();
        if (this.cities == null || this.cities.isEmpty()) {
            object.put("city", "");
        } else {
            object.put("city", list(this.cities));
        }
        if (this.people == null || this.people.isEmpty()) {
            object.put("person", "");
        } else {
            object.put("person", list(this.people));
        }
        return object;
    }

    private String list(ArrayList<String> tokens) {
        String ret = "";
        for (int i = 0; i < tokens.size(); i++) {
            ret += tokens.get(i);
            if (i < tokens.size() - 1) {
                ret += ", ";
            }
        }
        return ret;
    }

    public String populateFromJSON(JSONObject obj) { return null; }
}
