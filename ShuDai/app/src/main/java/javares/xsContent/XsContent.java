package javares.xsContent;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 用以用户查看小说章节内容的界面
 * @author Administrator
 *
 */
public class XsContent {
    private Elements textContent;
    private Elements title;

    public void showContent(String url) throws IOException{//此处修改，从目录中传递过来的章节href
        Document doc = Jsoup.connect(url).get();
        //通过Document的select方法获取属性结点集合
        textContent = doc.select("div.showtxt");
        title = doc.select("div.content h1");
        String titleName = title.text();
        System.out.println(titleName + " " + textContent.text());
    }
}
