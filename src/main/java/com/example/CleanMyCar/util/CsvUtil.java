package com.example.CleanMyCar.util;

import java.util.List;
import java.util.stream.Collectors;

public class CsvUtil {

    public static String toCsv(List<String> headers, List<List<String>> rows) {

        String headerLine = String.join(",", headers);

        String rowLines = rows.stream()
                .map(row -> row.stream()
                        .map(value -> value == null ? "" : value.replace(",", " ")) // Remove commas
                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining("\n"));

        return headerLine + "\n" + rowLines;
    }
}

