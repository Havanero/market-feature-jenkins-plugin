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

public class TestParseHtmlReport {
    private static LinkedHashMap<String, String> summary_table = new LinkedHashMap<>();
    private static LinkedHashMap<String, String> summary_error_table = new LinkedHashMap<>();


    @BeforeClass
    public static void setUp(){
        File input = new File("src/test/java/org/jenkinsci/plugins/marketfeaturereport/market_features.html");
        Document doc = null;
        try {
            doc = Jsoup.parse(input, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert doc != null;
        Element content = doc.getElementById("market-feature-header");
        Elements header = content.getElementsByClass("rTableHead");
        Elements failedHeader = content.getElementsByClass("rTableHeadFailed");
        Elements rows = content.getElementsByClass("rTableCell");
        Elements rows_failed = content.getElementsByClass("rTableCellFailed");
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

    }

    @Test
    public void testSuccessKeysExtracted()  {
        Assert.assertNotNull("Failed - No Market Features Key", summary_table.get("Market Features"));
        Assert.assertNotNull("Failed - No Total Tests Key", summary_table.get("Total Tests"));
        Assert.assertNotNull("Failed - No Passes Key", summary_table.get("Passes"));
        Assert.assertNotNull("Failed - No Errors Key", summary_table.get("Errors"));
        Assert.assertNotNull("Failed - No Failed Key", summary_table.get("Failed"));
    }

    @Test
    public void testSuccessValuesExtracted()  {
        Assert.assertEquals("Failed - No Market Features Value", summary_table.get("Market Features"), "2");
        Assert.assertEquals("Failed - No Total Tests Value", summary_table.get("Total Tests"), "8");
        Assert.assertEquals("Failed - No Passes Value", summary_table.get("Passes"), "6");
        Assert.assertEquals("Failed - No Errors Value", summary_table.get("Errors"), "0");
        Assert.assertEquals("Failed - No Failed Value", summary_table.get("Failed"), "2");
    }

    @Test
    public void testNoLinkAddedInCaseOfErrorsIsZero(){
        Assert.assertEquals("Failed - No Errors Value", summary_table.get("Errors"), "0");
        Assert.assertNull("Error Keys is not empty", summary_error_table.get("Errors"));
    }

    @Test
    public void testLinkAddedInCaseOfFailureIsNotZero(){
        Assert.assertEquals("Failed - No Failed Value", summary_table.get("Failed"), "2");
        Assert.assertNotNull("Failed Keys is empty", summary_error_table.get("Failed"));
    }

}
