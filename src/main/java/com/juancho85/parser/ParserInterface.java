package com.juancho85.parser;

import java.util.List;

public interface ParserInterface {

    List<String> getHeaders();

    int getLineNumber();

    void increaseLineNumber();

    void setHeaders(List<String> headers);

    void parse(List<String> line);
}
