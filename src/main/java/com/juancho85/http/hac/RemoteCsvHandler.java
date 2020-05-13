package com.juancho85.http.hac;

import com.juancho85.statistics.Timed;
import lombok.extern.log4j.Log4j2;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Dsl;

import javax.inject.Inject;
import java.io.IOException;

@Log4j2
public class RemoteCsvHandler {

    private AsyncCsvHandler handler;

    @Inject
    public RemoteCsvHandler(AsyncCsvHandler handler) {
        this.handler = handler;
    }

    @Timed
    public boolean handleFileUrl(String fileUrl) {
        try(AsyncHttpClient client = Dsl.asyncHttpClient()) {
            client.prepareGet(fileUrl)
                    .setFollowRedirect(true)
                    .execute(handler)
                    .toCompletableFuture()
                    .join();
            return true;
        } catch (IOException e) {
            log.error(e);
            return false;
        }
    }
}
