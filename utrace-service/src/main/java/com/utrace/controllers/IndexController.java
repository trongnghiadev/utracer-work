package com.utrace.controllers;

import java.util.HashMap;
import java.util.Map;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 *
 * @author superman
 */
public class IndexController {
    public static Route serveIndex = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        return "Hello world!";
    };
}
