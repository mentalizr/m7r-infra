package org.mentalizr.infra.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InputStreams {

    public static List<String> toStrings(InputStream inputStream) throws IOException {
        List<String> strings = new ArrayList<>();
        try (Reader isr = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(isr)) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                strings.add(line);
            }
        }
        return strings;
    }

}
