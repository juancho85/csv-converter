package com.juancho85.parser;

import java.util.List;
import java.util.Map;

public interface ParserInterface {

    void addParsedLine(Map<String, String> parsedLine);

    List<Map<String, String>> getParsedLines();

    List<String> getHeaders();

    int getLineNumber();

    void increaseLineNumber();

    void setHeaders(List<String> headers);

    void parse(List<String> line);
}
