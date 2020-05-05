package com.juancho85.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class CsvParser implements ParserInterface {

    private List<String> headers;

    private List<Map<String, String>> parsedLines = new ArrayList();

    private int lineNumber = 0;


    public void addParsedLine(Map<String, String> parsedLine) {
        this.parsedLines.add(parsedLine);
    }

    public List<Map<String, String>> getParsedLines() {
        return this.parsedLines;
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
        Map<String, String> parsedLine = new HashMap<>();
        IntStream.range(0, line.size()).forEach((index) -> {
            parsedLine.put(getHeaders().get(index), line.get(index));
        });
        addParsedLine(parsedLine);
    }
}