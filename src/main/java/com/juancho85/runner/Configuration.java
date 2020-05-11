package com.juancho85.runner;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Configuration {
    private String file;
    private String url;
    private List<String> headers;
    private String template;
    private String output;
}
