package com.example.wordbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

    ActionBar actionBar;

    ContentResolver contentResolver;
    Button insert=null;
    Button search=null;
    Button delete=null;
    Button showAll=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        actionBar=getActionBar();
//        actionBar.setDisplayShowHomeEnabled(false);
//        actionBar.hide();

        contentResolver=this.getContentResolver();

        insert=findViewById(R.id.insert);
        search=findViewById(R.id.search);
        delete=(Button)findViewById(R.id.delete);
        showAll=findViewById(R.id.SHOW_ALL);
        delete.setText("清空单词本");

        showAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                String key=((EditText)findViewById(R.id.search_edittext)).getText().toString();
                Cursor cursor=contentResolver.query(Words.Word.DICT_CONTENT_URI, null, "word like ? or detail like ?", new String[]{"%","%"},null);
                Bundle bundle=new Bundle();
                bundle.putSerializable("data", converCursorToList(cursor));
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, com.example.wordbook.Result.class);
                intent.putExtras(bundle);
                startActivity(intent);
                ((EditText)findViewById(R.id.search_edittext)).setText("");
            }
        });
        insert.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String word=((EditText)findViewById(R.id.word)).getText().toString();
                String detail=((EditText)findViewById(R.id.detail)).getText().toString();
                if(word.equals("")||detail.equals("")){
                    Toast.makeText(MainActivity.this, "输入无效", Toast.LENGTH_SHORT).show();
                }
                else{
                    ContentValues values=new ContentValues();
                    values.put(Words.Word.WORD, word);
                    values.put(Words.Word.DETAIL, detail);
                    contentResolver.insert(Words.Word.DICT_CONTENT_URI, values);
                    Toast.makeText(MainActivity.this, "添加单词成功", Toast.LENGTH_LONG).show();
                    ((EditText)findViewById(R.id.word)).setText("");
                    ((EditText)findViewById(R.id.detail)).setText("");
                }



            }
        });
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String key=((EditText)findViewById(R.id.search_edittext)).getText().toString();
                Cursor cursor=contentResolver.query(Words.Word.DICT_CONTENT_URI, null, "word like ? or detail like ?", new String[]{"%"+key+"%","%"+key+"%"},null);
                Bundle bundle=new Bundle();
                bundle.putSerializable("data", converCursorToList(cursor));
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, com.example.wordbook.Result.class);
                intent.putExtras(bundle);
                startActivity(intent);
                ((EditText)findViewById(R.id.search_edittext)).setText("");
            }
        });

        delete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                AlertDialog.Builder builder=new Builder(MainActivity.this);
                builder.setTitle("确定删除所有单词？");

                builder.setPositiveButton("确定",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        contentResolver.delete(Words.Word.DICT_CONTENT_URI, null, null);
                        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });

                builder.show();

            }
        });



    }

    protected ArrayList<Map<String, String>> converCursorToList(Cursor cursor) {
        // TODO Auto-generated method stub
        ArrayList<Map<String, String>> result=new ArrayList<Map<String,String>>();
        while(cursor.moveToNext()){
            Map<String, String> map=new HashMap<String, String>();
            map.put(Words.Word.WORD, cursor.getString(1));
            map.put(Words.Word.DETAIL, cursor.getString(2));
            result.add(map);
        }
        return result;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_exit:
                finish();
                return true;
        }
        return  super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}