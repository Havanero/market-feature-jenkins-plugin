package org.jenkinsci.plugins.marketfeaturereport;

import org.jenkinsci.plugins.marketfeaturereport.report.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class ParseCss {

    private LinkedHashMap<String, String> summary_table = new LinkedHashMap<>();
    private LinkedHashMap<String, String> summary_error_table = new LinkedHashMap<>();
    private static Section resultat;
    private Section section;
    private Table tmpTable;
    private Tr tmpTr;
    private Tr rowTr;
    private Td tmpTd;
    private Td rowTd;
    private Tab tmpTab;


    public ParseCss() {
        super();
        section = new Section();
        tmpTable = new Table();
        tmpTr = new Tr();
        tmpTab = new Tab();

    }

    public Section result() {
        return resultat;
    }

    public Boolean parse(String cssFile) throws IOException {
        try {

            File input = new File(cssFile);
            Document doc = Jsoup.parse(input, "UTF-8");
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
                summary_error_table.put(element.text(), linkHref);
                ++count_failed;
            }

        } catch (IOException io) {
            io.printStackTrace();
            return false;
        }

        return true;
    }

    public void ReadResults() {

        section.setSectionName("Market Feature");
        section.setLine("0");
        section.setColumn("0");
        tmpTr = new Tr();
        rowTr = new Tr();
        for (Map.Entry<String, String> entry : summary_table.entrySet()) {

            tmpTd = new Td();
            rowTd = new Td();
            if ((entry.getKey().equals("Failed") || entry.getKey().equals("Errors"))) {
                switch (entry.getKey()) {
                    case "Failed":
                        if (Integer.parseInt(entry.getValue()) != 0) {
                            System.out.println(
                                    entry.getKey() + "==" + entry.getValue() + " link Error="
                                            + summary_error_table
                                            .get(entry.getKey()));
                            tmpTd.setTdValue(entry.getKey());
                            tmpTd.setBgColor("red");
                            tmpTd.setFontColor("white");
                            tmpTd.setFontAttribute("bold");
                            tmpTd.setTitle("title1");
                            rowTd.setTdValue(entry.getValue());
                            rowTd.setBgColor("red");
                            rowTd.setHref(summary_error_table
                                    .get(entry.getKey()));
                            rowTd.setFontColor("white");
                            rowTd.setFontAttribute("normal");

                        } else {
                            System.out.println(entry.getKey() + "==" + entry.getValue());

                            tmpTd.setTdValue(entry.getKey());
                            tmpTd.setBgColor("green");
                            tmpTd.setFontColor("white");
                            tmpTd.setFontAttribute("bold");
                            tmpTd.setTitle("title1");
                            rowTd.setTdValue(entry.getValue());
                            rowTd.setBgColor("green");
                            rowTd.setFontColor("white");
                            rowTd.setFontAttribute("normal");
                        }
                        break;
                    case "Errors":
                        if (Integer.parseInt(entry.getValue()) != 0) {
                            System.out.println(
                                    entry.getKey() + "==" + entry.getValue() + " link Error="
                                            + summary_error_table
                                            .get(entry.getKey()));
                            tmpTd.setTdValue(entry.getKey());
                            tmpTd.setBgColor("red");
                            tmpTd.setFontColor("white");
                            tmpTd.setFontAttribute("bold");
                            tmpTd.setTitle("title1");
                            rowTd.setTdValue(entry.getValue());
                            rowTd.setBgColor("red");
                            rowTd.setHref(summary_error_table
                                    .get(entry.getKey()));
                            rowTd.setFontColor("white");
                            rowTd.setFontAttribute("normal");
                        } else {
                            System.out.println(entry.getKey() + "==" + entry.getValue());
                            tmpTd.setTdValue(entry.getKey());
                            tmpTd.setBgColor("green");
                            tmpTd.setFontColor("white");
                            tmpTd.setFontAttribute("bold");
                            tmpTd.setTitle("title1");
                            rowTd.setTdValue(entry.getValue());
                            rowTd.setBgColor("green");
                            rowTd.setFontColor("white");
                            rowTd.setFontAttribute("normal");
                        }
                        break;
                }

            } else {
                System.out.println(entry.getKey() + "==" + entry.getValue());
                tmpTd.setTdValue(entry.getKey());
                tmpTd.setBgColor("green");
                tmpTd.setFontColor("white");
                tmpTd.setFontAttribute("bold");
                tmpTd.setTitle("title1");
//                tmpTd.setHref("links");
                tmpTd.setAlign("center");
                tmpTd.setWidth("200");
                rowTd.setTdValue(entry.getValue());
                rowTd.setBgColor("green");
                rowTd.setFontColor("white");
                rowTd.setFontAttribute("normal");


            }
            section.addObject(tmpTab);
//            tmpTab.addObject(tmpTable);
            //section.addObject(tmpTable);
            //tmpTable.addTr(tmpTr);
            tmpTr.addTd(tmpTd);
            rowTr.addTd(rowTd);

        } //for loop
        tmpTable.addTr(tmpTr);
        tmpTable.addTr(rowTr);
        section.addObject(tmpTable);
        resultat = section;
    }

}
