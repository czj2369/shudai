package com.example.anjian.shudai;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BookShelfActivity extends Fragment {
    private String[] booknameList;
    private String[] bookhrefList;
    private String[] bookimgList;
    private String[] bookauthorList;
    private int selectWhich;
    private List<Book> bookList = new ArrayList<>();
    private int sum;//书架总数

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_shelf_layout,null);

//        创建数据库
        SQLiteDatabase db = new BookDb(getActivity(), "BookShelf.db", null, 2).getWritableDatabase();
        Cursor cursor = db.query("book",null,null,null,null,null,null);

        Intent intent = getActivity().getIntent();
        selectWhich = intent.getIntExtra("selectWhich", 0);
        Log.d("BookShelfActivity","select:"+selectWhich);

        booknameList = new String[cursor.getCount()];
        bookhrefList = new String[cursor.getCount()];
        bookimgList = new String[cursor.getCount()];
        bookauthorList = new String[cursor.getCount()];
        sum = 0;
        if(cursor.moveToFirst()){
            do{
                booknameList[sum] = cursor.getString(cursor.getColumnIndex("bookname"));
                bookhrefList[sum] = cursor.getString(cursor.getColumnIndex("bookhref"));
                bookauthorList[sum] = cursor.getString(cursor.getColumnIndex("bookauthor"));
                bookimgList[sum] = cursor.getString(cursor.getColumnIndex("bookimg"));
                selectWhich = cursor.getInt(cursor.getColumnIndex("selectwhich"));
                Log.d("BookShelfActivity","bookname:"+booknameList[sum]);
                Log.d("BookShelfActivity","bookhref:"+bookhrefList[sum]);
                Log.d("BookShelfActivity","bookhref:"+bookauthorList[sum]);
                Log.d("BookShelfActivity","bookhref:"+bookimgList[sum]);
                sum++;
            }while (cursor.moveToNext());
        }

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, booknameList);
        BookShelfAdapter adapter = new BookShelfAdapter(getContext(),R.layout.book_shelf_item,bookList);
        initBooks();
        ListView views = (ListView) view.findViewById(R.id.bookShelf);
        views.setAdapter(adapter);

        views.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MainMuluActivity.class);
                intent.putExtra("selectWhich",selectWhich);
                intent.putExtra("href",bookhrefList[position]);
                intent.putExtra("bookName",booknameList[position]);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initBooks() {
        for(int i = 0;i<sum;i++){
            Book book = new Book(getActivity(),booknameList[i],bookauthorList[i],bookimgList[i]);
            bookList.add(book);
        }
    }

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.book_shelf_layout);
//        //创建数据库
//        SQLiteDatabase db = new BookDb(this, "BookShelf.db", null, 1).getWritableDatabase();
//        Cursor cursor = db.query("book",null,null,null,null,null,null);
//
//        Intent intent = getIntent();
//        selectWhich = intent.getIntExtra("selectWhich", 0);
//        Log.d("MuluActivity","select:"+selectWhich);
//
//        booknameList = new String[cursor.getCount()];
//        bookhrefList = new String[cursor.getCount()];
//        int i = 0;
//        if(cursor.moveToFirst()){
//            do{
//                booknameList[i] = cursor.getString(cursor.getColumnIndex("bookname"));
//                bookhrefList[i] = cursor.getString(cursor.getColumnIndex("bookhref"));
//                Log.d("BookShelfActivity","bookname:"+booknameList[i]);
//                Log.d("BookShelfActivity","bookhref:"+bookhrefList[i]);
//                i++;
//            }while (cursor.moveToNext());
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, booknameList);
//        ListView view = (ListView) findViewById(R.id.bookShelf);
//        view.setAdapter(adapter);
//
//        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(BookShelfActivity.this, MainMuluActivity.class);
//                intent.putExtra("selectWhich",selectWhich);
//                intent.putExtra("href",bookhrefList[position]);
//                startActivity(intent);
//            }
//        });
//    }
}
