package com.juancho85.template;

import java.util.Map;

public interface TemplatingEngine {

    String render(Map<String, String> values);
}
