package com.example.ruwang.myreadcontractdemo;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * 简单的读取联系人的demo
 * 运用了PermssinGen动态权限开源框架
 */
public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private List<String> contractList = new ArrayList<>();

    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.list_type);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, contractList);
        reqPermission();
        mListView.setAdapter(adapter);
    }


    public void reqPermission() {
        PermissionGen.with(this)
                .addRequestCode(100)
                .permissions(new String[]{Manifest.permission.READ_CONTACTS})
                .request();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void readContracts() {
        Cursor cursor = null;
        //查询联系人
        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String displayNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contractList.add(displayName + "\n" + displayNumber);
                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


    }


    @PermissionFail(requestCode = 100)
    public void readContractsFile() {
        Toast.makeText(this, "您拒绝了权限", Toast.LENGTH_SHORT).show();
    }
}
