package com.juancho85.parser;

import com.juancho85.output.OutputAggregator;
import com.juancho85.util.Utilities;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@Log4j2
public class CsvParser implements ParserInterface {

    @Getter
    @Setter
    private List<String> headers;

    private OutputAggregator aggregator;

    @Inject
    public CsvParser(OutputAggregator aggregator) {
        this.aggregator = aggregator;
    }

    @Override
    public void parse(List<String> line) {
        final Map<String, String> parsedLine = Utilities.zipToMap(getHeaders(), line);
        aggregator.handleLine(parsedLine);
    }
}