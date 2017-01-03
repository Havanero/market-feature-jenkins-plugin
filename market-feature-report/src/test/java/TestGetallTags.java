import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Developed by:    cubanguy
 * Date:            30/12/16
 * Project:         market-feature-report
 */
public class TestGetallTags {

    private static Document doc = null;

    @Test
    public void testGetAllText() throws IOException {
        File input = new File("src/test/java/org/jenkinsci/plugins/marketfeaturereport/market_features.html");
        try {
            doc = Jsoup.parse(input, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements content = doc.getElementsByTag("h1");
        Elements ss = content.first().getAllElements().tagName("span").get(0).getElementsByAttribute("class");

        for (Element s : ss) {
            System.out.println(s.className() + "--" + s.text());
        }

        for (Element e : content) {
                   System.out.println(e.text());
            //  System.out.println(e.className());
            //   System.out.println(e.toString());
            //  System.out.println(e.getAllElements().get(0).getElementsByAttribute("class").attr("class"));
            System.out.println(e.getAllElements().tagName("span").get(0).getElementsByAttribute("class"));
        }
        Elements link_error = doc.getElementsByTag("a");
        System.out.println(link_error.text());

    }
}