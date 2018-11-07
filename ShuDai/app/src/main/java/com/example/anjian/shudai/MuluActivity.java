package com.example.anjian.shudai;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MuluActivity extends AppCompatActivity {
    //private ArrayList<String> list = new ArrayList<>();
    private List<Book> bookList = new ArrayList<>();
    private String[] bookName;
    private String[] author;
    private String[] bookImg;
    private String[] bookHref;
    private Document doc;
    private String url;
    private Elements booknameList;
    private Elements authorList;
    private Elements imgList;
    private int selectWhich;
    private int pageNum;
    //创建数据库
    private BookDb db_book;
    private ContentValues values = new ContentValues();
    private SQLiteDatabase db;


    public void setSelectWhich(int selectWhich) {
        this.selectWhich = selectWhich;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mulu_layout);
        Intent intent = getIntent();
        String sousuoName = intent.getStringExtra("sousuoName");
        selectWhich = intent.getIntExtra("selectWhich", 0);
        Log.d("MuluActivity","select:"+selectWhich);
        url = intent.getStringExtra("url") + sousuoName;
        if (selectWhich == 0) {
            Search1();
        }else if(selectWhich == 1){
            Search2();
        }
    }



    private void Search1() {
        Thread sousuoThread = new Thread() {
            @Override
            public void run() {
                try {
                    //爬取查询的结果
                    doc = Jsoup.connect(url).get();
                    //书名列表
                    booknameList = doc.select("div.result-game-item-detail h3 a");
                    //对应作者列表
                    authorList = doc.select("p.result-game-item-info-tag");
                    //对应小说图片
                    imgList = doc.select("div.result-game-item-pic a img");

                    bookName = new String[booknameList.size()];
                    bookHref = new String[booknameList.size()];
                    author = new String[authorList.size()];
                    bookImg = new String[booknameList.size()];

                    //用一个循环把书名按顺序放进
                    for (int i = 0,j = 4; i < booknameList.size(); i++,j=j+4) {
                        bookName[i] = booknameList.get(i).text();
                        bookHref[i] = booknameList.get(i).attr("href");
                        author[i] = authorList.get(j-4).text();
                        bookImg[i] = imgList.get(i).attr("src");
                        //list.add(bookName[i]);
                        System.out.println(bookName[i] + "   " + author[i] + "    " + bookHref[i]);
//                        System.out.println(author[j-4]);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setListAdapter();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        sousuoThread.start();
    }

    private void Search2() {
        Thread sousuoThread = new Thread() {
            @Override
            public void run() {
                try {
                    //爬取查询的结果
                    doc = Jsoup.connect(url).get();
                    //书名列表
                    booknameList = doc.select("a.Aimg");
                    int bookNum = booknameList.size();
                    //对应作者列表
                    authorList = doc.select("#right > ul > li > h4:nth-child(3) > a");
                    //对应小说图片
                    imgList = doc.select("ul.list_box li a img");

                    pageNum = new Integer(doc.select("div.title99 span strong").get(0).text());
                    Log.d("MuluActivity.class","pageNum:"+pageNum);
                    bookName = new String[pageNum];
                    bookHref = new String[pageNum];
                    author = new String[pageNum];
                    bookImg = new String[pageNum];

                    //用一个循环把书名按顺序放进
                    for (int i = 0; i < booknameList.size(); i++) {
                        bookName[i] = booknameList.get(i).attr("title");
                        bookHref[i] = "http://www.99lib.net" + booknameList.get(i).attr("href");
                        author[i] = authorList.get(i).text();
                        // bookImg[i] = imgList.get(i).attr("src");
                        //list.add(bookName[i]);
                        System.out.println(bookName[i] + "   " + author[i] + "    " + bookHref[i]);
                    }
                    if(pageNum < 15) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setListAdapter();
                            }
                        });
                    }
                    if (pageNum >= 15) {
                        try {
                            doc = Jsoup.connect(url + "&page=2").get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //书名列表
                        booknameList = doc.select("a.Aimg");
                        //对应作者列表
                        authorList = doc.select("ul.list_box li a");
                        //对应小说图片
                        imgList = doc.select("ul.list_box li a img");

                        //用一个循环把书名按顺序放进
                        for (int i = 15; i < pageNum; i++) {
                            bookName[i] = booknameList.get(i - 15).attr("title");
                            bookHref[i] = "http://www.99lib.net" + booknameList.get(i - 15).attr("href");
                            author[i] = authorList.get(i - 15).text();
                            // bookImg[i] = imgList.get(i).attr("src");
                            //list.add(bookName[i]);
                            System.out.println(bookName[i - 15] + "   " + author[i - 15] + "    " + bookHref[i - 15]);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setListAdapter();
                            }
                        });
                    }
                }catch (IOException e ){
                    e.printStackTrace();
                }
            }
        };
        sousuoThread.start();
    }


    private void setListAdapter() {
        db_book = new BookDb(this,"BookShelf.db", null ,2);
        db = db_book.getWritableDatabase();


//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bookName);
        BookitemAdapter adapter = new BookitemAdapter(this,R.layout.book_item,bookList);
        initBooks();
        ListView bookListView = (ListView) findViewById(R.id.mulu_list);
        bookListView.setAdapter(adapter);
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MuluActivity.this, bookHref[position], Toast.LENGTH_SHORT).show();
//                Intent intentMulu2 = new Intent(MuluActivity.this, MainMuluActivity.class);
                Intent intentMulu2 = new Intent(MuluActivity.this, DetailActivity.class);
                intentMulu2.putExtra("href",bookHref[position]);
                intentMulu2.putExtra("selectWhich",selectWhich);
                intentMulu2.putExtra("bookImg",bookImg[position]);
                intentMulu2.putExtra("bookAuthor",author[position]);
                intentMulu2.putExtra("bookName",bookName[position]);
                startActivity(intentMulu2);
            }
        });

//        bookListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
//                new AlertDialog.Builder(MuluActivity.this).setTitle("选择").setSingleChoiceItems(new CharSequence[]{"加入书架","其他"}, 0,
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                if(which == 0) {
//                                    //组装数据
//                                    values.put("bookname", bookName[position]);
//                                    values.put("bookhref", bookHref[position]);
//                                    values.put("bookauthor",author[position]);
//                                    values.put("bookimg",bookImg[position]);
//                                    db.insert("book", null, values);
//                                    values.clear();
//                                }
//                                if(which == 1) {
//                                    Toast.makeText(MuluActivity.this, "无其他信息", Toast.LENGTH_SHORT).show();
//                                }
//                                dialog.dismiss();
//                            }
//                        }).show();
//                return true;
//            }
//        });
    }

    private void initBooks() {
        for(int i = 0;i<bookName.length;i++){
            Book book = new Book(MuluActivity.this,"书名:"+bookName[i],author[i],bookImg[i]);
            bookList.add(book);
        }
    }
}
