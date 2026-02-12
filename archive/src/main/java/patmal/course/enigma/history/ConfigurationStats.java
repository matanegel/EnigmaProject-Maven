package patmal.course.enigma.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationStats implements Serializable {
    private String machineConfiguration;
    private List<ProcessedString> processedStrings;

    public ConfigurationStats(String currentConfig) {
        this.machineConfiguration = currentConfig;
        this.processedStrings = new ArrayList<>();
    }

    public void addProcessedString(String src, String res, long time) {
        processedStrings.add(new ProcessedString(src, res, time));
    }

    public String getMachineConfiguration() {
        return machineConfiguration;
    }
    public List<ProcessedString> getProcessedStrings() {
        return processedStrings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //print the configuration
        sb.append("Code: ").append(machineConfiguration).append("\n");


        if (processedStrings.isEmpty()) {
            sb.append("No strings processed with this configuration.\n");
        } else {
            for (int i = 0; i < processedStrings.size(); i++) {
                sb.append(i + 1)
                        .append(". ")
                        .append(processedStrings.get(i))
                        .append("\n");
            }
        }

        return sb.toString();
    }
}

