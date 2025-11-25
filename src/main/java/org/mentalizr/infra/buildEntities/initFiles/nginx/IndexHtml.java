package org.mentalizr.infra.buildEntities.initFiles.nginx;

import de.arthurpicht.utils.core.strings.Strings;
import de.arthurpicht.utils.io.stream.InputStreams;
import org.mentalizr.infra.InfraRuntimeException;
import org.mentalizr.infra.buildEntities.initFiles.InitFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class IndexHtml implements InitFile {

    private final String string;

    public static IndexHtml getInstance() {
        return new IndexHtml();
    }

    private IndexHtml() {
        List<String> stringList = getAsStrings();
        this.string = Strings.listing(stringList, "\n");
    }

    @Override
    public String getFileName() {
        return "index.html";
    }

    @Override
    public String getContent() {
        return this.string;
    }

    private List<String> getAsStrings() {
        InputStream inputStream = InputStreams.getFileFromResourceAsStream("nginx/index.html");
        try {
            return InputStreams.toStrings(inputStream);
        } catch (IOException e) {
            throw new InfraRuntimeException("Could not read " + getFileName() + " from resources.");
        }
    }
}
