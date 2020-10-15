package com.FDT.easycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public TextView result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result=findViewById(R.id.result);
        Button num0 = findViewById(R.id.num0);
        Button num1 = findViewById(R.id.num1);
        Button num2 = findViewById(R.id.num2);
        Button num3 = findViewById(R.id.num3);
        Button num4 = findViewById(R.id.num4);
        Button num5 = findViewById(R.id.num5);
        Button num6 = findViewById(R.id.num6);
        Button num7 = findViewById(R.id.num7);
        Button num8 = findViewById(R.id.num8);
        Button num9 = findViewById(R.id.num9);
        Button Add = findViewById(R.id.add);
        Button Minus = findViewById(R.id.minus);
        Button Multiply = findViewById(R.id.multiply);
        Button Divide = findViewById(R.id.Divide);
        Button Clear = findViewById(R.id.Clear);
        Button Equals = findViewById(R.id.Equals);
        Button power = findViewById(R.id.power);
        Button Sqrt =findViewById(R.id.sqrt);
        num0.setOnClickListener(this);
        num1.setOnClickListener(this);
        num2.setOnClickListener(this);
        num3.setOnClickListener(this);
        num4.setOnClickListener(this);
        num5.setOnClickListener(this);
        num6.setOnClickListener(this);
        num7.setOnClickListener(this);
        num8.setOnClickListener(this);
        num9.setOnClickListener(this);
        Add.setOnClickListener(this);
        Minus.setOnClickListener(this);
        Multiply.setOnClickListener(this);
        Divide.setOnClickListener(this);
        Clear.setOnClickListener(this);
        Equals.setOnClickListener(this);
        power.setOnClickListener(this);
        Sqrt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.num0:
                clear();
                result.setText(result.getText()+"0");
                break;
            case R.id.num1:
                clear();
                result.setText(result.getText()+"1");
                break;
            case R.id.num2:
                clear();
                result.setText(result.getText()+"2");
                break;
            case R.id.num3:
                clear();
                result.setText(result.getText()+"3");
                break;
            case R.id.num4:
                clear();
                result.setText(result.getText()+"4");
                break;
            case R.id.num5:
                clear();
                result.setText(result.getText()+"5");
                break;
            case R.id.num6:
                clear();
                result.setText(result.getText()+"6");
                break;
            case R.id.num7:
                clear();
                result.setText(result.getText()+"7");
                break;
            case R.id.num8:
                clear();
                result.setText(result.getText()+"8");
                break;
            case R.id.num9:
                clear();
                result.setText(result.getText()+"9");
                break;
            case R.id.add:
                clear();
                result.setText(result.getText()+"+");
                break;
            case R.id.minus:
                clear();
                result.setText(result.getText()+"-");
                break;
            case R.id.multiply:
                clear();
                result.setText(result.getText()+"*");
                break;
            case R.id.Divide:
                clear();
                result.setText(result.getText()+"/");
                break;
            case R.id.power:
                clear();
                result.setText(result.getText()+"^");
                break;
            case R.id.sqrt:
                clear();
                result.setText(result.getText()+"√");
                break;
            case R.id.Clear:
                result.setText("0");
                break;
            case R.id.Equals:
                String expression=result.getText().toString();
                int temp=0;
                while (temp!=-1){
                    temp=expression.indexOf("^");
                    if(temp==-1){
                        temp=0;
                        break;
                    }
                    int i,j;
                    for(i=temp-1;i>0;i--){
                        if(!(expression.charAt(i)>='0'&&expression.charAt(i)<='9')){
                            break;
                        }
                    }
                    for(j=temp+1;j<expression.length();j++) {
                        if (!(expression.charAt(j) >= '0' && expression.charAt(j) <= '9')) {
                            break;
                        }
                    }
                        if(j==expression.length()){
                            if(i==0){
                                expression="Math.pow("+expression.substring(i,temp)+","+expression.substring(temp+1,j)+")";
                            }else{
                                expression=expression.substring(0,i+1)+"Math.pow("+expression.substring(i+1,temp)+","+expression.substring(temp+1,j)+")";
                            }
                        }else{
                            if(i==0){
                                expression="Math.pow("+expression.substring(i,temp)+","+expression.substring(temp+1,j)+")"+expression.substring(j);
                            }else{
                                expression=expression.substring(0,i+1)+"Math.pow("+expression.substring(i+1,temp)+","+expression.substring(temp+1,j)+")"+expression.substring(j);
                            }
                        }
                }
                while (temp!=-1){
                    temp=expression.indexOf("√");
                    if(temp==-1){
                        break;
                    }
                    int j;
                    for(j=temp+1;j<expression.length();j++) {
                        if (!(expression.charAt(j) >= '0' && expression.charAt(j) <= '9')) {
                            break;
                        }
                    }
                    if(j==expression.length()){
                        expression=expression.substring(0,temp)+"Math.sqrt("+expression.substring(temp+1)+")";
                    }else{
                        expression=expression.substring(0,temp)+"Math.sqrt("+expression.substring(temp+1,j)+")"+expression.substring(j);
                    }
                }
                Context con=Context.enter();
                con.setOptimizationLevel(-1);
                Scriptable scope=con.initSafeStandardObjects();
                Object res=con.evaluateString(scope,expression,"<cmd>",1,null);
                result.setText(res.toString());
                break;
        }
    }
    public void clear(){
        TextView res=findViewById(R.id.result);
        if(res.getText().equals("0")){
            res.setText("");
        }
    }
}
