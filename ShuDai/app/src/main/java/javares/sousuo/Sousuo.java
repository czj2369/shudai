package javares.sousuo;

/**
 * 用以用户搜索小说的界面
 */
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Sousuo {
    private String bookname[];
    private String href[];
    private String author[];
    private String bookImg[];
    private Elements booknameList;
    private Elements authorList;
    private Elements imgList;
    private String url;

    public void setBookname(String[] bookname) {
        this.bookname = bookname;
    }

    public void setAuthor(String[] author) {
        this.author = author;
    }

    public String[] getAuthor() {
        return author;
    }

    public String[] getBookname() {
        return bookname;
    }

    public String[] getHref() {
        return href;
    }

    public void setHref(String[] href) {
        this.href = href;
    }

    public Elements getBooknameList() {
        return booknameList;
    }

    public void setBooknameList(Elements booknameList) {
        this.booknameList = booknameList;
    }

    public Elements getAuthorList() {
        return authorList;
    }

    public void setAuthorList(Elements authorList) {
        this.authorList = authorList;
    }

    public Elements getImgList() {
        return imgList;
    }

    public String[] getBookImg() {
        return bookImg;
    }

    public void sousuo(String xs_name){

        url = "http://www.qb5200.tw/s.php?ie=gbk&s=11637699871618729356&q=" + xs_name;
        //Document document = Jsoup.connect(url).timeout(3000).get();
        //url = "http://www.qb5200.tw/s.php?ie=gbk&s=11637699871618729356&q=%D0%DE%D5%E6%C1%C4%CC%EC%C8%BA";
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(url).get();
                    //通过Document的select方法获取属性结点集合 //2018年9月10日 12:39:32 这里遇到问题，爬到的数据没有存进去
                    booknameList = doc.select("h4.bookname a");
                    authorList = doc.select("div.author");
                    imgList = doc.select("div.bookimg a img");
                    bookname = new String[booknameList.size()];
                    for (int i = 0; i < booknameList.size(); i++) {
                        bookname[i] = booknameList.get(i).text();
                        href[i] = "http://www.qb5200.tw" + booknameList.get(i).attr("href");
                        author[i] = authorList.get(i).text();
                        bookImg[i] = "http://www.qb5200.tw" + imgList.get(i).attr("src");
                        System.out.println(bookname[i] + "   " + author[i] + "    " + href[i]);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //得到节点的第一个对象
        //Element element = elements.get(0);

        //System.out.println(elements)
    }


}

