package org.jenkinsci.plugins.marketfeaturereport;

import org.jenkinsci.plugins.marketfeaturereport.report.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ParseCss {

    private LinkedHashMap<String, String> summary_table = new LinkedHashMap<>();
    private LinkedHashMap<String, String> summary_error_table = new LinkedHashMap<>();
    private LinkedHashMap<String, String> summary = new LinkedHashMap<>();
    private static Logger LOGGER = Logger.getLogger(ParseCss.class.getName());
    private static Section resultAt;
    private File inputFile;
    private Section section;
    private Table tmpTable;
    private Tr tmpTr;
    private Tab tmpTab;
    private static Document doc = null;
    private Elements all_fields = null;

    ParseCss() {
        super();
        section = new Section();
        tmpTable = new Table();
        tmpTr = new Tr();
        tmpTab = new Tab();

    }

    public Section result() {
        return resultAt;
    }

    public Boolean parse(String cssFile) {
        try {

            inputFile = new File(cssFile);
            doc = Jsoup.parse(inputFile, "UTF-8");

            Elements content = doc.getElementsByTag("h1");
            all_fields = content.first().getAllElements().
                    tagName("span").get(0).getElementsByAttribute("class");

            int count_failed = 0, count = 0;

            for (Element element : all_fields) {
//                summary_table.put(element.text(), rows.get(count).text());
//                ++count;
                String strNum = element.text().replaceAll("\\D+", "");
                summary_table.put(element.className(), strNum);
                if (element.text().contains("total"))
                    summary_error_table.put("link", "#Expand");
                summary.put(element.className(), element.text());
            }
//            Elements link_error = content.getElementsByTag("a");
//            for (Element element : failedHeader) {
//                summary_table.put(element.text(), rows_failed.get(count_failed).text());
//                String linkHref = link_error.get(count_failed).attr("href");
//                summary_error_table.put(element.text(), linkHref);
//                ++count_failed;
//            }

        } catch (Exception ex) {
            LOGGER.warning(ex.getMessage());
            return false;
        }

        return true;
    }

    void ReadResults() {

        section.setSectionName(doc.title());
        section.setLine("0");
        section.setColumn("0");
        section.setFontColor("#65c400");

        //new stuff
//        section.setLine("1");
//        section.setSectionName("8 total, 2 failed 1 passed 1 error 4 skip");

        for (Map.Entry<String, String> m : summary.entrySet()) {
            Field field = new Field();
            field.setFieldName(m.getKey());
            field.setFieldValue(m.getValue());
            if (m.getKey().contains("total"))
                field.setHref("#Expand");
            section.addObject(field);

        }
        Field field = new Field();
        field.setCdata(all_fields.text());
        section.addObject(field);


        // new stuff ends here

//        tmpTr = new Tr();
//        Tr rowTr = new Tr();
//        for (Map.Entry<String, String> entry : summary_table.entrySet()) {
//
//            Td tmpTd = new Td();
//            Td rowTd = new Td();
//            if ((entry.getKey().equals("failed") || entry.getKey().equals("ignored"))) {
//                switch (entry.getKey()) {
//                    case "failed":
//                        if (Integer.parseInt(entry.getValue()) != 0) {
//                            tmpTd.setTdValue(entry.getKey());
//                            tmpTd.setBgColor("red");
//                            tmpTd.setFontColor("white");
//                            tmpTd.setFontAttribute("bold");
//                            tmpTd.setTitle("title1");
//                            rowTd.setTdValue(entry.getValue());
//                            rowTd.setBgColor("red");
//                            rowTd.setHref("artifact/" + inputFile.getName() + summary_error_table
//                                    .get("link"));
//                            rowTd.setFontColor("white");
//                            rowTd.setFontAttribute("normal");
//
//                        } else {
//                            tmpTd.setTdValue(entry.getKey());
//                            tmpTd.setBgColor("#65c400");
//                            tmpTd.setFontColor("white");
//                            tmpTd.setFontAttribute("bold");
//                            tmpTd.setTitle("title1");
//                            rowTd.setTdValue(entry.getValue());
//                            rowTd.setBgColor("#65c400");
//                            rowTd.setFontColor("white");
//                            rowTd.setFontAttribute("normal");
//                        }
//                        break;
//                    case "ignored":
//                        if (Integer.parseInt(entry.getValue()) != 0) {
//                            tmpTd.setTdValue(entry.getKey());
//                            tmpTd.setBgColor("red");
//                            tmpTd.setFontColor("white");
//                            tmpTd.setFontAttribute("bold");
//                            tmpTd.setTitle("title1");
//                            rowTd.setTdValue(entry.getValue());
//                            rowTd.setBgColor("red");
//                            rowTd.setHref("artifact/" + inputFile.getName() + summary_error_table
//                                    .get("link"));
//                            rowTd.setFontColor("white");
//                            rowTd.setFontAttribute("normal");
//                        } else {
//                            tmpTd.setTdValue(entry.getKey());
//                            tmpTd.setBgColor("#65c400");
//                            tmpTd.setFontColor("white");
//                            tmpTd.setFontAttribute("bold");
//                            tmpTd.setTitle("title1");
//                            rowTd.setTdValue(entry.getValue());
//                            rowTd.setBgColor("#65c400");
//                            rowTd.setFontColor("white");
//                            rowTd.setFontAttribute("normal");
//                        }
//                        break;
//                }
//
//            } else {
//                tmpTd.setTdValue(entry.getKey());
//                tmpTd.setBgColor("#65c400");
//                tmpTd.setFontColor("white");
//                tmpTd.setFontAttribute("bold");
//                tmpTd.setTitle("title1");
//                tmpTd.setAlign("center");
//                tmpTd.setWidth("200");
//                rowTd.setTdValue(entry.getValue());
//                rowTd.setBgColor("#65c400");
//                rowTd.setFontColor("white");
//                rowTd.setFontAttribute("normal 11px");
//
//            }
//            section.addObject(tmpTab);
//            tmpTr.addTd(tmpTd);
//            rowTr.addTd(rowTd);
//
//        }
//        tmpTable.addTr(tmpTr);
//        tmpTable.addTr(rowTr);
//        section.addObject(tmpTable);
        resultAt = section;
    }

}
