package javares.mulu;

/**
 * 用以用户搜索之后得到的小说目录界面
 */
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Mulu {

    private Elements muluList;
    private Elements hrefList;

    public void showMulu(String url) throws IOException{
        url = "href";//此处修改，从搜索中传递过来的
        //Document document = Jsoup.connect(url).timeout(3000).get();
        Document doc = Jsoup.connect(url).get();

        //通过Document的select方法获取属性结点集合
        muluList = doc.select("div.listmain dl dd");
        hrefList = doc.select("div.listmain dl dd a");
        for (int i = 6; i < muluList.size()-3; i++) {
            String text = muluList.get(i).text();
            String href = hrefList.get(i).attr("href");
            System.out.println(text + "  http://www.qb5200.tw" + href);
        }

    }
}
