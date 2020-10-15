package com.tian.splitmate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tian.splitmate.ui.db.SQLHandler;
import com.tian.splitmate.ui.entities.User;
import com.tian.splitmate.ui.home.HomeFragment;

import java.util.List;

public class AddUserActivity extends AppCompatActivity {

    /**
     * 添加用户进数据库
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        final EditText mUserName = findViewById(R.id.user_name);
        final EditText mMobNo = findViewById(R.id.phone_num);
        findViewById(R.id.finish_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLHandler handler = new SQLHandler(getApplicationContext());
                User user = new User();
                user.setUserName(mUserName.getText().toString());
                user.setPhoneNumber(mMobNo.getText().toString());
                handler.addUser(user);
                Toast.makeText(getApplicationContext(), getText(R.string.toast_adduser).toString() + mUserName.getText().toString(), Toast.LENGTH_SHORT).show();
                mUserName.setText("");
                mMobNo.setText("");
            }
        });
        findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName.setText("");
                mMobNo.setText("");
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}
