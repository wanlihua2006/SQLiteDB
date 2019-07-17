package com.example.wlh.sqlitedb;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Button insert;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = SQLiteDatabase.openOrCreateDatabase(
                this.getFilesDir().toString() + "/my.db3", null);
        listView = findViewById(R.id.show);
        insert = findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((EditText)findViewById(R.id.title)).getText().toString();
                String content = ((EditText)findViewById(R.id.content)).getText().toString();
                try{
                    insertData(db,title,content);
                    Cursor cursor = db.rawQuery("select * from news_inf", null);
                    inflateList(cursor);
                }catch (SQLException e){
                    //e.printStackTrace();
                    db.execSQL("create table news_inf(_id integer primary key autoincrement,"
                            + " news_title varchar(50),"
                            + " news_content varchar(255))");
                    insertData(db,title,content);
                    Cursor cursor = db.rawQuery("select * from news_inf", null);
                    inflateList(cursor);
                }

            }
        });
    }

    private void inflateList(Cursor cursor) {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this,
                R.layout.line, cursor,  new String[]{"news_title", "news_content"},
                new int[]{R.id.my_title, R.id.my_content});
        listView.setAdapter(adapter);
    }

    private void insertData(SQLiteDatabase db, String title, String content) {
        //执行插入语句
        db.execSQL("insert into news_inf values(null, ?, ?)", new String[]{title,content});
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出程序关闭SQLitedatabase
        if (db !=null && db.isOpen()){
            db.close();
        }
    }
}
