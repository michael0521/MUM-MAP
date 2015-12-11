package com.yangxiaoming.fragmenttest01;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends Activity implements BookListFragment.Callbacks{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // 加载/res/layout目录下的activity_book_twopane.xml布局文件
        setContentView(R.layout.activity_book_twopane);
    }

    // 实现Callbacks接口必须实现的方法
    @Override
    public void onItemSelected(Integer id){
        Bundle arguments = new Bundle();
        arguments.putInt(BookDetailFragment.ITEM_ID, id);
        BookDetailFragment fragment = new BookDetailFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction()
                .replace(R.id.book_detail_container, fragment)
                .commit();
    }

}
