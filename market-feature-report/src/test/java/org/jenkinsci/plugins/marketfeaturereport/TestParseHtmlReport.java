package org.jenkinsci.plugins.marketfeaturereport;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestParseHtmlReport {
    private static LinkedHashMap<String, String> summary_table = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> summary_error_table = new LinkedHashMap<>();
    private static Document doc = null;


    @BeforeClass
    public static void setUp() {
        File input = new File("src/test/java/org/jenkinsci/plugins/marketfeaturereport/market_features.html");
        try {
            doc = Jsoup.parse(input, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;

        Elements content = doc.getElementsByTag("h1");
        Elements all_fields = content.first().getAllElements().
                tagName("span").get(0).getElementsByAttribute("class");
        Assert.assertNotNull("all fields ok", all_fields.text());


        int count_failed = 0, count = 0;

        for (Element s : all_fields) {
            System.out.println(s.className() + "--" + s.text());
            summary_table.put(s.className(), s.text());
            if (s.text().contains("failed"))
                summary_error_table.put(s.className(), s.text());
        }


    }

    public void testElementsArePresent() {
        Elements content = doc.getElementsByTag("h1");
        Elements all_fields = content.first().getAllElements().
                tagName("span").get(0).getElementsByAttribute("class");

        Assert.assertNotNull("all fields ok", all_fields.text());

        int count_failed = 0, count = 0;

        for (Element s : all_fields) {
            System.out.println(s.className() + "--" + s.text());
            summary_table.put(s.className(), s.text());
            System.out.println("getting value" + summary_table.get(s.className()));
            if (s.text().contains("error"))
                summary_error_table.put(s.className(), s.text());
        }

        Elements link_error = doc.getElementsByTag("a");
//        for (Element element : failedHeader) {
//            summary_table.put(element.text(), rows_failed.get(count_failed).text());
//            String linkHref = link_error.get(count_failed).attr("href");
//            summary_error_table.put(element.text(), linkHref);
//            ++count_failed;
        //       }

    }

    @Test
    public void testExtractedTitle() {
        Assert.assertEquals("missing title", doc.title(),  "Test Results — Functional Tests");

        for(Map.Entry<String, String> m:summary_table.entrySet()){
            System.out.println(m.getKey()+ "--" + m.getValue());
        }

    }

        @Test
    public void testSuccessKeysExtracted() {
        System.out.println(summary_table.keySet());
        for(Map.Entry<String, String> k:summary_table.entrySet()){
            System.out.println(k.getKey());

        }
        Assert.assertNotNull("Failed - No Market Feature Total", summary_table.get("total"));
        Assert.assertNotNull("Failed - No Passed Tests Key", summary_table.get("passed"));
        Assert.assertNotNull("Failed - No Failed Key", summary_table.get("failed"));
        Assert.assertNotNull("Failed - No Skip Key", summary_table.get("skipped"));
        Assert.assertNotNull("Failed - No Error Key", summary_table.get("ignored"));
    }

    @Test
    public void testSuccessValuesExtracted() {
        Assert.assertEquals("Failed - No Total Tests Value", summary_table.get("total"), "8 total,");
        Assert.assertEquals("Failed - No Passes Value", summary_table.get("passed"), "1 passed");
        Assert.assertEquals("Failed - No Errors Value", summary_table.get("ignored"), "1 error");
        Assert.assertEquals("Failed - No Failed Value", summary_table.get("skipped"), "4 skip");
    }

    @Test
    public void testNoLinkAddedInCaseOfErrorsIsZero() {
        Assert.assertEquals("Failed - No Errors Value", summary_table.get("ignored"), "1 error");
        Assert.assertNull("Error Keys is not empty", summary_error_table.get("ignored"));
    }

    @Test
    public void testLinkAddedInCaseOfFailureIsNotZero() {
        Assert.assertEquals("Failed - No Failed Value", summary_table.get("failed"), "2 failed");
        Assert.assertNotNull("Failed Keys is empty", summary_error_table.get("failed"));
    }

}
