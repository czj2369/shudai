package com.example.anjian.shudai;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SousuoActivity extends Fragment {
    private EditText sousuoContent;
    private Button goSearch;
//    private Button bookShelf;
    private String sousuoName = "";
    private String url;
    private int selectWhich;
    private BookDb db_book;
    private View view;
    private RadioButton yuan1;
    private RadioButton yuan2;
    private RadioButton yuan3;
    private RadioGroup rg;

    public void setSelectWhich(int selectWhich) {
        this.selectWhich = selectWhich;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sousuo_layout,null);
        db_book = new BookDb(getActivity(), "BookShelf.db", null, 2);
        db_book.getWritableDatabase();

        url = "http://zhannei.baidu.com/cse/search?s=5334330359795686106&q=";
        sousuoContent = (EditText) view.findViewById(R.id.textContent);
        goSearch = (Button)view.findViewById(R.id.goSearch);

        //设置搜索按钮的监听事件
        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sousuoName = sousuoContent.getText().toString();//获得输入的书名
                Intent intent = new Intent(getActivity(), MuluActivity.class);
                intent.putExtra("sousuoName",sousuoName);
                intent.putExtra("url",url);
                intent.putExtra("selectWhich",selectWhich);
                startActivity(intent);
                Log.d("SousuoActivity",sousuoName);
            }
        });

        rg = (RadioGroup)view.findViewById(R.id.rg);
        yuan1 = (RadioButton)view.findViewById(R.id.yuan1);
        yuan2 = (RadioButton)view.findViewById(R.id.yuan2);
        yuan3 = (RadioButton)view.findViewById(R.id.yuan3);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(yuan1.getId() == checkedId){
                    url = "http://zhannei.baidu.com/cse/search?s=5334330359795686106&q=";
                    setSelectWhich(0);
                }else if(yuan2.getId() == checkedId){
                    url = "http://www.99lib.net/book/search.php?s=13139900387823019677&type=站内&q=";
                    setSelectWhich(1);
                }else if(yuan3.getId() == checkedId){
                    url = "https://www.xbiquge6.com/search.php?keyword=";
                    setSelectWhich(2);
                }
            }
        });




        return view;
    }





















//    //载入menu
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.targetnet,menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.targetnet, menu);
//        return true;
//    }

//    //设置menu上的信息
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.targetNet:
//                new AlertDialog.Builder(getActivity())//
//                        .setTitle("选择搜索源")// 标题
//                        .setSingleChoiceItems(//
//                                new CharSequence[]{"搜索源1", "搜索源2"},// 列表显示的项目
//                                0,// 默认选中 第一个
//                                new DialogInterface.OnClickListener() {// 设置条目
//                                    public void onClick(DialogInterface dialog, int which) {// 响应事件
//                                        if (which == 0) {
//                                            url = "http://zhannei.baidu.com/cse/search?s=5334330359795686106&q=";
//                                            setSelectWhich(0);
//                                        } else if (which == 1) {
//                                            url = "http://www.99lib.net/book/search.php?s=13139900387823019677&type=站内&q=";
//                                            setSelectWhich(1);
//                                        }else{
//                                            selectWhich = 0;
//                                        }
//                                        dialog.dismiss();
//                                    }
//                                }//
//                        )
//                        .show();
//        }
//        return true;
//    }


    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.sousuo_layout);
//        db_book = new BookDb(this, "BookShelf.db", null, 1);
//        db_book.getWritableDatabase();
//
//
//        url = "http://zhannei.baidu.com/cse/search?s=5334330359795686106&q=";
//        sousuoContent = (EditText) findViewById(R.id.textContent);
//        goSearch = (Button)findViewById(R.id.goSearch);
//
//        goSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sousuoName = sousuoContent.getText().toString();//获得输入的书名
//                Intent intent = new Intent(SousuoActivity.this, MuluActivity.class);
//                intent.putExtra("sousuoName",sousuoName);
//                intent.putExtra("url",url);
//                intent.putExtra("selectWhich",selectWhich);
//                startActivity(intent);
//                Log.d("SousuoActivity",sousuoName);
//            }
//        });
//
//        bookShelf = (Button)findViewById(R.id.bookshelf_button);
//        bookShelf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SousuoActivity.this, BookShelfActivity.class);
//                intent.putExtra("url",url);
//                intent.putExtra("selectWhich",selectWhich);
//                startActivity(intent);
//            }
//        });
//    }
}
