package com.example.anjian.shudai;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import javares.xsContent.XsContent;

public class MainMuluActivity extends AppCompatActivity {

    private String[] muluNum;
    private String[] muluhref;
    private Document doc;
    private String url;
    private int position;
    private String bookName;
    private int selectWhich;
    private Elements hrefList;
    private Elements muluList;
    private Button continue_button;
    //创建数据库
    private BookDb db_book;
    private ContentValues values = new ContentValues();
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_mulu_layout);
        db_book = new BookDb(this,"BookShelf.db", null ,2);
        db = db_book.getWritableDatabase();

        Intent intent = getIntent();
        url = intent.getStringExtra("href");
        selectWhich = intent.getIntExtra("selectWhich", 0);
        bookName = intent.getStringExtra("bookName");
        continue_button = (Button)findViewById(R.id.continue_button);
        Log.d("MainMuluActivity","select:"+selectWhich);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.query("book",null,null,null,null,null,null);
                if(cursor.moveToFirst()) {
                    url = cursor.getString(cursor.getColumnIndex("bookcontinue"));
                    position = cursor.getInt(cursor.getColumnIndex("position"));
                }
                Log.d("MainMuluActivity",url+"      "+position);
                Intent intentContent = new Intent(MainMuluActivity.this,XsContentActivity.class);
                intentContent.putExtra("href",url);
                intentContent.putExtra("position",position);
                intentContent.putExtra("muluhref",muluhref);
                intentContent.putExtra("bookName",bookName);
                intentContent.putExtra("selectWhich",selectWhich);
                startActivity(intentContent);
            }
        });
        if(selectWhich == 0) {
            Thread sousuoThread = new Thread() {
                @Override
                public void run() {
                    try {
                        doc = Jsoup.connect(url).get();
                        muluList = doc.select("div#list dl dd");
                        hrefList = doc.select("div#list dl dd a");
                        muluNum = new String[muluList.size()];
                        muluhref = new String[hrefList.size()];
                        for (int i = 0; i < muluList.size(); i++) {
                            muluNum[i] = muluList.get(i).text();
                            muluhref[i] = url + hrefList.get(i).attr("href");
                            //System.out.println(muluNum[i] + "    "+muluhref[i]);
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
        }else if(selectWhich == 1){
            Thread sousuoThread = new Thread() {
                @Override
                public void run() {
                    try {
                        doc = Jsoup.connect(url).get();
                        muluList = doc.select("dl#dir dd a");
                        hrefList = doc.select("dl#dir dd a");
                        muluNum = new String[muluList.size()];
                        muluhref = new String[hrefList.size()];
                        for (int i = 0; i < muluList.size(); i++) {
                            muluNum[i] = muluList.get(i).text();
                            muluhref[i] = "http://www.99lib.net" + hrefList.get(i).attr("href");
                            //System.out.println(muluNum[i] + "  http://www.99lib.net" + muluhref[i]);
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
        Toast.makeText(MainMuluActivity.this,"加载目录完成",Toast.LENGTH_SHORT).show();
    }
    private void setListAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, muluNum);
        ListView view = (ListView) findViewById(R.id.mulu2_listview);
        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(MainMuluActivity.this, muluhref[position], Toast.LENGTH_SHORT).show();
                Intent intentContent = new Intent(MainMuluActivity.this,XsContentActivity.class);
                intentContent.putExtra("href",muluhref[position]);
                intentContent.putExtra("position",position);
                intentContent.putExtra("muluhref",muluhref);
                intentContent.putExtra("bookName",bookName);
                intentContent.putExtra("selectWhich",selectWhich);
                startActivity(intentContent);
            }
        });
    }
}
