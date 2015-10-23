package org.jenkinsci.plugins.marketfeaturereport;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestParseHtmlReport {

    @Test
    public void TestParsing() throws IOException {
        System.out.println("Testing..");
        File input = new File(
                "/home/carvcal/IdeaProjects/plugin-project/market-feature-jenkins-plugin/market-feature-report/work/jobs/MM2/workspace/market_feature_passes.html");
        System.out.println("just name " + input.getName());
        Document doc = Jsoup.parse(input, "UTF-8");
        Element content = doc.getElementById("market-feature-header");
        System.out.println(content.getElementById("title").text());
        Elements header = content.getElementsByClass("rTableHead");
        Elements failedHeader = content.getElementsByClass("rTableHeadFailed");
        Elements rows = content.getElementsByClass("rTableCell");
        Elements rows_failed = content.getElementsByClass("rTableCellFailed");

        LinkedHashMap<String, String> summary_table = new LinkedHashMap<>();
        LinkedHashMap<String, String> summary_error_table = new LinkedHashMap<>();

        int count_failed = 0, count = 0;

        for (Element element : header) {
            summary_table.put(element.text(), rows.get(count).text());
            ++count;
        }
        Elements link_error = content.getElementsByTag("a");
        for (Element element : failedHeader) {
            summary_table.put(element.text(), rows_failed.get(count_failed).text());
            String linkHref = link_error.get(count_failed).attr("href");
            summary_error_table.put(element.text(),linkHref);
            ++count_failed;
        }

        for (Map.Entry<String, String> entry : summary_table.entrySet()) {
            if ((entry.getKey().equals("Failed") || entry.getKey().equals("Errors"))) {
                switch (entry.getKey()) {
                    case "Failed":
                        if (Integer.parseInt(entry.getValue()) != 0) {
                            System.out.println(
                                    entry.getKey() + "==" + entry.getValue() + " link Error="
                                            + summary_error_table
                                            .get(entry.getKey()));
                        } else {
                            System.out.println(entry.getKey() + "==" + entry.getValue());
                        }
                        break;
                    case "Errors":
                        if (Integer.parseInt(entry.getValue()) != 0) {
                            System.out.println(
                                    entry.getKey() + "==" + entry.getValue() + " link Error="
                                            + summary_error_table
                                            .get(entry.getKey()));
                        }
                        else{
                            System.out.println(entry.getKey() + "==" + entry.getValue());
                        }
                        break;
                }

            } else {
                System.out.println(entry.getKey() + "==" + entry.getValue());
            }

        }

    }

}
