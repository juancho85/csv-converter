package com.juancho85.parser;

import com.google.common.base.Splitter;
import com.juancho85.injection.ConverterModule;
import com.juancho85.output.OutputAggregator;
import com.juancho85.util.Utilities;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Log4j2
public class CsvParser implements ParserInterface {

    @Getter
    @Setter
    private List<String> headers;

    private OutputAggregator aggregator;

    Pattern tokensPattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

    @Inject
    public CsvParser(OutputAggregator aggregator, @ConverterModule.HeadersAnnotation List<String> headers) {
        this.aggregator = aggregator;
        this.headers = headers;
    }

    @Override
    public void parseLine(String line) {
        List<String> fileHeaders = headers.isEmpty() ? getHeaders() : headers;
        aggregator.handleLine(Utilities.zipToMap(fileHeaders, Splitter.on(tokensPattern).trimResults().splitToList(line)));
    }

    @Override
    public void parse(List<String> line) {
        List<String> fileHeaders = headers.isEmpty() ? getHeaders() : headers;
        final Map<String, String> parsedLine = Utilities.zipToMap(fileHeaders, line);
        aggregator.handleLine(parsedLine);
    }
}