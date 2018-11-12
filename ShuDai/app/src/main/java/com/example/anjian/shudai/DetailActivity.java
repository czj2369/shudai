package com.example.anjian.shudai;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {
    private String url;
    private String bookImg;
    private String bookAuthor;
    private String bookName;
    private int selectWhich;
    private Document doc;
    private Elements detailList;
    private String[] detail_content;
    private ImageView bookImgView;
    private TextView detailTextView;
    private TextView bookNameTextView;
    private TextView authorTextView;
    private Button addShelf;
    private Button read;
    //创建数据库
    private BookDb db_book;
    private ContentValues values = new ContentValues();
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        Intent intent = getIntent();
        url = intent.getStringExtra("href");
        selectWhich = intent.getIntExtra("selectWhich", 0);
        bookImg = intent.getStringExtra("bookImg");
        bookAuthor = intent.getStringExtra("bookAuthor");
        bookName = intent.getStringExtra("bookName");
        addShelf = (Button)findViewById(R.id.detail_shelf);
        read = (Button)findViewById(R.id.detail_read);
        Log.d("DetailActivity","select:"+selectWhich);
        if(selectWhich == 0) {
            selectWhich0();
        }else if(selectWhich == 1){
            selectWhich1();
        }else if(selectWhich == 2) {
            selectWhich2();
        }
        addShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                values.put("bookname", bookName);
                values.put("bookhref", url);
                values.put("bookauthor",bookAuthor);
                values.put("bookimg",bookImg);
                values.put("selectwhich",selectWhich);
                db.insert("book", null, values);
                values.clear();
                Toast.makeText(DetailActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startRead = new Intent(DetailActivity.this,MainMuluActivity.class);
                startRead.putExtra("href",url);
                startRead.putExtra("bookName",bookName);
                startRead.putExtra("selectWhich",selectWhich);
                startActivity(startRead);
            }
        });
    }

    private void selectWhich2() {
        Thread sousuoThread = new Thread() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(url).get();
                    detailList = doc.select("#intro > p:nth-child(1)");
                    detail_content = new String[detailList.size()];
                    for (int i = 0; i < detailList.size(); i++) {
                        detail_content[i] = detailList.get(i).text();
                        Log.d("DetailActivity","detail_content:"+detail_content[i]);
                        //System.out.println(muluNum[i] + "    "+muluhref[i]);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showContent();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        sousuoThread.start();
    }

    private void selectWhich1() {
        Thread sousuoThread = new Thread() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(url).get();
                    detailList = doc.select("#book_info > div.intro");
                    detail_content = new String[detailList.size()];
                    for (int i = 0; i < detailList.size(); i++) {
                        detail_content[i] = detailList.get(i).text();
                        //System.out.println(muluNum[i] + "  http://www.99lib.net" + muluhref[i]);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showContent();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        sousuoThread.start();
    }

    private void selectWhich0() {
        Thread sousuoThread = new Thread() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(url).get();
                    detailList = doc.select("#intro > p");
                    detail_content = new String[detailList.size()];
                    for (int i = 0; i < detailList.size(); i++) {
                        detail_content[i] = detailList.get(i).text();
                        Log.d("DetailActivity","detail_content:"+detail_content[i]);
                        //System.out.println(muluNum[i] + "    "+muluhref[i]);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showContent();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        sousuoThread.start();
    }

    public void showContent(){
        db_book = new BookDb(this,"BookShelf.db", null ,2);
        db = db_book.getWritableDatabase();
        bookNameTextView = (TextView)findViewById(R.id.detail_bookname);
        authorTextView = (TextView)findViewById(R.id.detail_author);
        detailTextView = (TextView)findViewById(R.id.detail);
        bookImgView = (ImageView)findViewById(R.id.detail_image);
        bookNameTextView.setText(bookName);
        authorTextView.setText(bookAuthor);
        detailTextView.setText("简介:"+detail_content[0]);
        Glide.with(DetailActivity.this).load(bookImg).into(bookImgView);
    }

}
