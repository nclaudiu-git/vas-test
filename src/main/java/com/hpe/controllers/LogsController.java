package com.hpe.controllers;

import com.hpe.data.Kpis;
import com.hpe.data.Metrics;
import com.hpe.services.JsonService;
import com.hpe.services.KpisService;
import com.hpe.services.MetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

@RestController
public class LogsController {

    private static final String JSON_GITHUB_URL = "https://raw.githubusercontent.com/vas-test/test1/master/logs/MCP_%s.json";

    @Autowired
    private JsonService jsonService;

    @Autowired
    private MetricsService metricsService;

    @Autowired
    private KpisService kpisService;

    @GetMapping("/process-json/{date}")
    public String processJson(@PathVariable String date) throws IOException {
        String path = String.format(JSON_GITHUB_URL, date);

        try (Scanner scanner = new Scanner(new URL(path).openStream(), StandardCharsets.UTF_8.toString())) {
            scanner.useDelimiter("\\A");
            String content = scanner.hasNext() ? scanner.next() : "";
            jsonService.process(date, Arrays.stream(content.split("\\r?\\n")));
            return "Processed.";
        } catch (FileNotFoundException exception) {
            // return 404
            throw new JsonNotFoundException();
        }
    }

    @GetMapping("/metrics")
    public Metrics getMetrics() {
        return metricsService.getLatestMetrics();
    }

    @GetMapping("/kpis")
    public Kpis getKpis() {
        return kpisService.getKpis();
    }
}