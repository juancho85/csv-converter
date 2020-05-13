package com.juancho85.http.hac;

import com.juancho85.parser.ParserInterface;
import com.juancho85.parser.generated.CSV;
import lombok.extern.log4j.Log4j2;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.HttpResponseBodyPart;
import org.asynchttpclient.Response;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Log4j2
public class AsyncCsvHandler extends AsyncCompletionHandler<Integer> {

    private String incompleteLine = "";

    // TODO: Use the actual line separator of the file received
    String OS_LINE_SEPARATOR = System.getProperty("line.separator");


    private ParserInterface parser;

    @Inject
    public AsyncCsvHandler(ParserInterface parser) {
        this.parser = parser;
    }

    @Override
    public State onBodyPartReceived(HttpResponseBodyPart bodyPart)
            throws Exception {
        StringBuilder builder = new StringBuilder(new String(bodyPart.getBodyPartBytes()));
        builder.insert(0, incompleteLine);
        int lastCompleteLineCharPosition = builder.length() - 1;
        if(!bodyPart.isLast()) {
            lastCompleteLineCharPosition = builder.lastIndexOf(OS_LINE_SEPARATOR);
            if(lastCompleteLineCharPosition != -1 && lastCompleteLineCharPosition != builder.length()) {
                incompleteLine = builder.substring(lastCompleteLineCharPosition + 1);
            }
        }

        InputStream inputStream = new ByteArrayInputStream(builder.substring(0, lastCompleteLineCharPosition).getBytes(StandardCharsets.UTF_8));
        refreshInstance(inputStream);
        // TODO: Partial handling of file
        CSV.parse(inputStream, parser);
        return State.CONTINUE;
    }

    private void refreshInstance(InputStream inputStream) {
        new CSV(inputStream);
    }

    @Override
    public Integer onCompleted(Response response)
            throws Exception {
        log.info("Done");
        return 0;
    }
}
