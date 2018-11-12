package com.example.anjian.shudai;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class XsContentActivity extends AppCompatActivity {
    private Integer[] fontSizeList = {26, 28, 30, 32, 34, 36, 38};
    private String url;
    private String bookName;
    private Document doc;
    private Elements textContent;
    private Elements title;
    private TextView content;
    private ScrollView scrollView2;
    private Button up_button;
    private Button down_button;
    private int position;
    private String[] muluHref;
    private int selectWhich;
    private LinearLayout xsLayout;
    private boolean isNight = false;
//    private int flag = 1;//单击屏幕标志
    //创建数据库
    private BookDb db_book;
    private ContentValues values = new ContentValues();
    private SQLiteDatabase db;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.fontSize:
                content = (TextView)findViewById(R.id.xsContent);
                new AlertDialog.Builder(this)//
                        .setTitle("字体大小")// 标题
                        .setSingleChoiceItems(//
                                new CharSequence[] { "12", "14", "16", "18", "20", "22", "24" ,"26", "28", "30" },// 列表显示的项目
                                0,// 默认选中 第一个
                                new DialogInterface.OnClickListener() {// 设置条目
                                    public void onClick(DialogInterface dialog, int which) {// 响应事件
                                        if(which == 0 ){
                                            content.setTextSize(12);
                                        }else if(which == 1){
                                            content.setTextSize(14);
                                        }else if(which == 2){
                                            content.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                                        }else if(which == 3){
                                            content.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                                        }else if(which == 4){
                                            content.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                                        }else if(which == 5){
                                            content.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                                        }else if(which == 6){
                                            content.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
                                        }else if(which == 7){
                                            content.setTextSize(TypedValue.COMPLEX_UNIT_SP,26);
                                        }else if(which == 8){
                                            content.setTextSize(TypedValue.COMPLEX_UNIT_SP,28);
                                        }else if(which == 9){
                                            content.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
                                        }
                                        dialog.dismiss();
                                    }
                                }//
                        )//
//                        .setPositiveButton(// "确定"按钮
//                                "确定",//
//                                new DialogInterface.OnClickListener() {//
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // 点击 "确定"按钮 之后, 执行这里的 代码
//                                    }
//                                }//
//                        )//
                        .show();
                break;
            case R.id.nightModle:
                if(!isNight) {
                    content = (TextView) findViewById(R.id.xsContent);
                    content.setBackgroundColor(Color.GRAY);
                    xsLayout = (LinearLayout) findViewById(R.id.xscontent_layout);
                    xsLayout.setBackgroundColor(Color.GRAY);
                    isNight = true;
                }else{
                    content = (TextView) findViewById(R.id.xsContent);
                    content.setBackgroundColor(Color.WHITE);
                    xsLayout = (LinearLayout) findViewById(R.id.xscontent_layout);
                    xsLayout.setBackgroundColor(Color.WHITE);
                    isNight = false;
                }
                break;
                default:
        }
        return true;
    }


    @Override
    protected void onPause() {
        super.onPause();
        db_book = new BookDb(this,"BookShelf.db", null ,2);
        db = db_book.getWritableDatabase();
        values.put("bookcontinue",url);
        values.put("position",position);
        values.put("selectwhich",selectWhich);
        db.update("book",values,"bookname = ?",new String[] {bookName});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xscontent_layout);

        xsLayout = (LinearLayout) findViewById(R.id.xscontent_layout);
        scrollView2 = (ScrollView)findViewById(R.id.scrollView2);
        Intent intent = getIntent();
        position = intent.getIntExtra("position",1);
        muluHref = intent.getStringArrayExtra("muluhref");
        url = intent.getStringExtra("href");
        bookName = intent.getStringExtra("bookName");
        Log.d("XsContentActivity",bookName);
        selectWhich = intent.getIntExtra("selectWhich", 0);
        if(selectWhich == 0) {
            select1();
        }else if(selectWhich == 1){
            select2();
        }else if(selectWhich == 2){
            select3();
        }

    }

    private void select3() {
        Thread sousuoThread = new Thread() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(url).get();
                    Document doc = Jsoup.connect(url).get();
                    //通过Document的select方法获取属性结点集合
                    textContent = doc.select("#content");
                    title = doc.select("#wrapper > div.content_read > div > div.bookname > h1");
                    Log.d("XsContentActivity",textContent.text());
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
        //上下章的按钮逻辑 此处存在逻辑错误 比如第一章没有上一章，最后一章没有下一章。2018年9月11日 22:03:46
        up_button = (Button) findViewById(R.id.up_button);
        down_button = (Button) findViewById(R.id.down_button);

        up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != 0) {
                    url = muluHref[position - 1];
                    position = position - 1;
                }
                Thread sousuoThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            doc = Jsoup.connect(url).get();
                            Document doc = Jsoup.connect(url).get();
                            //通过Document的select方法获取属性结点集合
                            textContent = doc.select("#content");
                            title = doc.select("#wrapper > div.content_read > div > div.bookname > h1");
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
                content = (TextView) findViewById(R.id.xsContent);
                String html = textContent.html();//将爬取的数据中html的标签保存下来
                content.setText(Html.fromHtml(html));
            }
        });

        down_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != muluHref.length-1) {
                    position++;
                    url = muluHref[position];
                } else {
                    content = (TextView) findViewById(R.id.xsContent);
                    content.setText("无更多章节");
                }
                Thread sousuoThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            doc = Jsoup.connect(url).get();
                            Document doc = Jsoup.connect(url).get();
                            //通过Document的select方法获取属性结点集合
                            textContent = doc.select("#content");
                            title = doc.select("#wrapper > div.content_read > div > div.bookname > h1");
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
//                content = (TextView) findViewById(R.id.xsContent);
//                String html = textContent.html();//将爬取的数据中html的标签保存下来
//                content.setText(Html.fromHtml(html));
            }
        });
    }

    private void select2() {
        Thread sousuoThread = new Thread() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(url).get();
                    Document doc = Jsoup.connect(url).get();
                    //通过Document的select方法获取属性结点集合
                    textContent = doc.select("#content");
                    title = doc.select("#content > h2");
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
        //上下章的按钮逻辑 此处存在逻辑错误 比如第一章没有上一章，最后一章没有下一章。2018年9月11日 22:03:46
        up_button = (Button) findViewById(R.id.up_button);
        down_button = (Button) findViewById(R.id.down_button);

        up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != 0) {
                    url = muluHref[position - 1];
                    position = position - 1;
                }
                Thread sousuoThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            doc = Jsoup.connect(url).get();
                            Document doc = Jsoup.connect(url).get();
                            //通过Document的select方法获取属性结点集合
                            textContent = doc.select("#content");
                            title = doc.select("#content > h2");
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
                content = (TextView) findViewById(R.id.xsContent);
                String html = textContent.html();//将爬取的数据中html的标签保存下来
                content.setText(Html.fromHtml(html));
            }
        });

        down_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                url = muluHref[position + 1];
                if (position != muluHref.length-1) {
                    position++;
                    url = muluHref[position];
                } else {
                    content = (TextView) findViewById(R.id.xsContent);
                    content.setText("无更多章节");
                }
                Thread sousuoThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            doc = Jsoup.connect(url).get();
                            Document doc = Jsoup.connect(url).get();
                            //通过Document的select方法获取属性结点集合
                            textContent = doc.select("#content");
                            title = doc.select("#content > h2");
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
//                content = (TextView) findViewById(R.id.xsContent);
//                String html = textContent.html();//将爬取的数据中html的标签保存下来
//                content.setText(Html.fromHtml(html));
            }
        });
    }

    private void select1() {
        Thread sousuoThread = new Thread() {
            @Override
            public void run() {
                try {
                    doc = Jsoup.connect(url).get();
                    //通过Document的select方法获取属性结点集合
                    textContent = doc.select("#content");
                    title = doc.select("div.bookname h1");
                    String titleName = title.text();
                    //System.out.println(titleName + " " + textContent.text());

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
        //上下章的按钮逻辑 此处存在逻辑错误 比如第一章没有上一章，最后一章没有下一章。2018年9月11日 22:03:46
        up_button = (Button) findViewById(R.id.up_button);
        down_button = (Button) findViewById(R.id.down_button);

        up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != 0) {
                    url = muluHref[position - 1];
                    position = position - 1;
                }
                Thread sousuoThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            doc = Jsoup.connect(url).get();
                            Document doc = Jsoup.connect(url).get();
                            //通过Document的select方法获取属性结点集合
                            textContent = doc.select("#content");
                            title = doc.select("div.bookname h1");
                            String titleName = title.text();
                            //System.out.println(titleName + " " + textContent.text());

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
//                content = (TextView) findViewById(R.id.xsContent);
//                String html = textContent.html();//将爬取的数据中html的标签保存下来
//                content.setText(Html.fromHtml(html));
            }
        });

        down_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position != muluHref.length-1) {
                    url = muluHref[position + 1];
                    position++;
                } else {
                    content = (TextView) findViewById(R.id.xsContent);
                    content.setText("无更多章节");
                }
                Thread sousuoThread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            doc = Jsoup.connect(url).get();
                            Document doc = Jsoup.connect(url).get();
                            //通过Document的select方法获取属性结点集合
                            textContent = doc.select("#content");
                            title = doc.select("div.bookname h1");
                            String titleName = title.text();
                            System.out.println(titleName + " " + textContent.text());

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
//                content = (TextView) findViewById(R.id.xsContent);
//                String html = textContent.html();//将爬取的数据中html的标签保存下来
//                content.setText(Html.fromHtml(html));
            }
        });
    }

    public void showContent(){
        content = (TextView)findViewById(R.id.xsContent);
        XsContentActivity.this.setTitle(title.text());
        scrollView2.scrollTo(0,0); //回到顶部
       // this.setTitle(title.text());
        String html = textContent.html();//将爬取的数据中html的标签保存下来
        content.setText(Html.fromHtml(html));//将html标签解释出来，使得在手机上看到的与实际看到的一样
        //content.setText();

    }
}
