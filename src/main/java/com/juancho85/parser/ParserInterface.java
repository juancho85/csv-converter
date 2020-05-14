package com.juancho85.parser;

import java.util.List;
import java.util.Map;

public interface ParserInterface {

    List<String> getHeaders();

    void setHeaders(List<String> headers);

    void parseLine(String line);

    void parse(List<String> line);
}
