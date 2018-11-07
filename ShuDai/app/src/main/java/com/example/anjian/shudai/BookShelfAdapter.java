package com.example.anjian.shudai;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Administrator on 2018/11/5.
 */

public class BookShelfAdapter extends ArrayAdapter<Book> {
    private int resourceId;
    private Book book;
    //创建数据库
    private BookDb db_book;
    private ContentValues values = new ContentValues();
    private SQLiteDatabase db;

    public BookShelfAdapter(Context context, int textViewResourceId, List<Book> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        book = getItem(position);
        final int curPosition = position;
        View view;
        ViewHolder viewHolder;
        db_book = new BookDb(getContext(),"BookShelf.db", null ,2);
        db = db_book.getWritableDatabase();
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new BookShelfAdapter.ViewHolder();
//            viewHolder.bookImage = (WebView) view.findViewById(R.id.book_image);
            viewHolder.bookImage = (ImageView)view.findViewById(R.id.shelf_book_image);
            viewHolder.bookName = (TextView)view.findViewById(R.id.shelf_book_name);
            viewHolder.author = (TextView)view.findViewById(R.id.shelf_author);
            viewHolder.delButton = (Button)view.findViewById(R.id.shelf_delBook);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (BookShelfAdapter.ViewHolder)view.getTag();
        }
        Glide.with(book.getActivity()).load(book.getImagehref()).into(viewHolder.bookImage);
        viewHolder.bookName.setText(book.getBookName());
        viewHolder.author.setText(book.getAuthor());
        viewHolder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                book = getItem(curPosition);
                AlertDialog alertDialog2 = new AlertDialog.Builder(getContext())
                        .setTitle("删除书籍")
                        .setMessage("确定删除"+book.getBookName()+"吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.delete("book","bookname = ?",new String[] {book.getBookName()});
                            }
                        })

                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .create();
                alertDialog2.show();
            }
        });
        return view;
    }

    class ViewHolder{
        ImageView bookImage;
        Button delButton;
        //WebView bookImage;
        TextView author;
        TextView bookName;
    }

}
