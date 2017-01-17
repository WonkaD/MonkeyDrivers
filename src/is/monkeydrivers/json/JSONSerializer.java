package is.monkeydrivers.json;

public class JSONSerializer {

    private final String json;

    public JSONSerializer(String[] fields, String[] values) {
        String json = "";
        for (int i = 0; i < Math.min(fields.length, values.length); i++)
            json += "\"" + fields[i] + "\":\"" + values[i] + "\",";

        this.json = "{" + (json.endsWith(",") ? json.substring(0, json.length() - 1) : json) + "}";
    }

    public String json() {
        return json;
    }
}
