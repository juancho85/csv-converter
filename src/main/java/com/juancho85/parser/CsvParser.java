package com.juancho85.parser;

import com.juancho85.output.OutputAggregator;
import com.juancho85.statistics.Timed;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
public class CsvParser implements ParserInterface {

    private List<String> headers;

    private List<Map<String, String>> parsedLines = new ArrayList();

    private int lineNumber = 0;

    private OutputAggregator aggregator;

    @Inject
    public CsvParser(OutputAggregator aggregator) {
        this.aggregator = aggregator;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void increaseLineNumber() {
        lineNumber++;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    @Override
    public void parse(List<String> line) {
        final Map<String, String> parsedLine = zipToMap(getHeaders(), line);
        aggregator.handleLine(parsedLine);
    }

    public Map<String, String> zipToMap(List<String> keys, List<String> values) {
        if(keys.size() != values.size()) {
            log.error("Key value sizes not matching for");
            log.error("Keys: {}", keys);
            log.error("Values: {}", values);
        }
        return IntStream.range(0, keys.size())
                .boxed()
                .collect(Collectors.toMap(keys::get, values::get));
    }
}