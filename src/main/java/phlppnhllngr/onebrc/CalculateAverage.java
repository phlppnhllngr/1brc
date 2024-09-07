package phlppnhllngr.onebrc;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CalculateAverage {

    public static void main(String[] args) throws Exception {
        Path path = Paths.get(args.length > 0 ? args[0] : "measurements.txt");
        Map<String, Statistics> measurements = new TreeMap<>();

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                int idx = line.indexOf(';');
                String name = line.substring(0, idx);
                float value = parseTemperature(line.substring(idx + 1));
                Statistics stat = measurements.get(name);
                if (stat == null) {
                    measurements.put(name, new Statistics(value, value, 1, value));
                }
                else {
                    if (value < stat.min) {
                        stat.min = value;
                    }
                    else if (value > stat.max) {
                        stat.max = value;
                    }
                    // Derive new mean from old mean and number of measurements.
                    // This way, not each temperature value needs to be stored.
                    stat.mean = (stat.mean * stat.count + value) / (stat.count + 1);
                    stat.count++;
                }
            }
        }

        var result = measurements
                .entrySet()
                .stream()
                .map(e -> STR."\{e.getKey()}=\{e.getValue()}")
                .collect(Collectors.joining(", "));

        System.out.println(STR."{\{result}}");
    }

    /**
     * According to the {@link https://github.com/gunnarmorling/1brc?tab=readme-ov-file#rules-and-limits rules}
     * temperature is a "non null double between -99.9 (inclusive) and 99.9 (inclusive), always with one fractional digit",
     * so some checks that Float.parseFloat does can be skipped for better performance.
     *
     * @see jdk.internal.math.FloatingDecimal#readJavaFormatString
     */
    static float parseTemperature(String in) {
        boolean negative = in.charAt(0) == '-';
        int idx = negative ? 1 : 0;
        int firstDigit = ((int) in.charAt(idx)) - 48; // ASCII: '0' = 48, '1' = 49, ...
        int fractionalDigit;
        char c = in.charAt(++idx);
        float f;
        if (c == '.') {
            fractionalDigit = ((int) in.charAt(idx + 1)) - 48;
            f = firstDigit + fractionalDigit / 10f;
        }
        else {
            int secondDigit = ((int) c) - 48;
            fractionalDigit = ((int) in.charAt(idx + 2)) - 48;
            f = 10 * firstDigit + secondDigit + fractionalDigit / 10f;
        }
        return negative ? -f : f;
    }


    static class Statistics {
        float min;
        float max;
        int count;
        float mean;

        Statistics(float min, float max, int count, float mean) {
            this.min = min;
            this.max = max;
            this.count = count;
            this.mean = mean;
        }

        @Override
        public String toString() {
            return STR."""
            \{BigDecimal.valueOf(min).setScale(1, RoundingMode.HALF_UP)}\
            /\
            \{BigDecimal.valueOf(mean).setScale(1, RoundingMode.HALF_UP)}\
            /\
            \{BigDecimal.valueOf(max).setScale(1, RoundingMode.HALF_UP)}\
            """;
        }
    }

}
