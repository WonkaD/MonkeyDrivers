package is.monkeydrivers.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONDeserializer {
    private final String json;

    public JSONDeserializer(String json) {
        this.json = json;
    }

    public String getValueOfField(String field) {
        Pattern pattern = Pattern.compile("\"" + field + "\":\"(\\w+)\"");
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? matcher.group(1) : "null";
    }
}
