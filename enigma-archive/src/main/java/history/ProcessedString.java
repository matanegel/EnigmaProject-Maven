package history;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ProcessedString implements Serializable {
    @JsonProperty("input")
    public final String source;
    @JsonProperty("output")
    public final String result;
    @JsonProperty("duration")
    public final long timeTaken;

    public ProcessedString(String source, String result, long timeTaken) {
        this.source = source;
        this.result = result;
        this.timeTaken = timeTaken;
    }

    @Override
    public String toString() {
        return String.format("<%s> --> <%s> (%d nano-seconds)", source, result, timeTaken);
    }
}
