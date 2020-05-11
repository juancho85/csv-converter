package com.juancho85.http;

import com.juancho85.output.OutputAggregator;
import com.juancho85.parser.ParserInterface;
import com.juancho85.parser.generated.CSV;
import com.juancho85.parser.generated.ParseException;
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


    private ParserInterface parser;

    @Inject
    public AsyncCsvHandler(ParserInterface parser) {
        this.parser = parser;
    }

    @Override
    public State onBodyPartReceived(HttpResponseBodyPart bodyPart)
            throws Exception {
        // TODO: More efficient to work on bytes
        String chunk = new String(bodyPart.getBodyPartBytes());
        // TODO: edge cases? chunk smaller than a line?
        if(!incompleteLine.isEmpty()) {
            chunk = incompleteLine + chunk;
        }
        if(!bodyPart.isLast()) {
            // TODO handle windows CR
            int position = chunk.lastIndexOf("\n");
            if(position!=-1 && position!= chunk.length()){
                // Store incomplete line to add to next chuck received
                incompleteLine = chunk.substring(position + 1);
                // Remove incomplete line from chunk
                chunk = chunk.substring(0, position);
            }
        }

        InputStream inputStream = new ByteArrayInputStream(chunk.getBytes(StandardCharsets.UTF_8));
        refreshInstance(inputStream);
        try {
            CSV.parse(inputStream, parser);
        } catch (ParseException e) {
            log.error("Exception in Chunk");
            log.error(chunk);
        }

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
