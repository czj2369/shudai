package com.example.anjian.shudai;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * 搜索完后显示的Listview中的bookList的适配器
 * Created by Administrator on 2018/10/24.
 */

public class BookitemAdapter extends ArrayAdapter<Book> {
    private int resourceId;
    private Book book;
    public BookitemAdapter(Context context, int textViewResourceId, List<Book> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        book = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            viewHolder = new ViewHolder();
//            viewHolder.bookImage = (WebView) view.findViewById(R.id.book_image);
            viewHolder.bookImage = (ImageView)view.findViewById(R.id.book_image);
            viewHolder.bookName = (TextView)view.findViewById(R.id.book_name);
            viewHolder.author = (TextView)view.findViewById(R.id.author);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
//        viewHolder.bookImage.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView webview, String url) {
//                webview.loadUrl(book.getImagehref());
//                return true;
//            }
//        });
//        viewHolder.bookImage.setImageResource(R.drawable.picture);
        Glide.with(book.getActivity()).load(book.getImagehref()).into(viewHolder.bookImage);
        viewHolder.bookName.setText(book.getBookName());
        viewHolder.author.setText(book.getAuthor());
    return view;
    }

    class ViewHolder{
        ImageView bookImage;
        //WebView bookImage;
        TextView author;
        TextView bookName;
    }
}
