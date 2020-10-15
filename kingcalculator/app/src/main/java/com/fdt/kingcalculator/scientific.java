package com.fdt.kingcalculator;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class scientific extends Fragment implements View.OnClickListener  {
    private TextView mTextView;
    private TextView textview;
    private StringBuilder sb = new StringBuilder();
    private StringBuilder temp = new StringBuilder();
    private String answer=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            sb=new StringBuilder(Objects.requireNonNull(savedInstanceState.getString("sb")));
            temp=new StringBuilder(Objects.requireNonNull(savedInstanceState.getString("temp")));
            answer=savedInstanceState.getString("answer");
            state = savedInstanceState.getBoolean("state");
            sb_state =savedInstanceState.getBoolean("sb_state");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //定义控件
        View layout = inflater.inflate(R.layout.fragment_scientific, container, false);
        Button button_AC = layout.findViewById(R.id.AC);
        Button button_C = layout.findViewById(R.id.C);
        Button button_mod = layout.findViewById(R.id.mod);
        Button button_equal = layout.findViewById(R.id.equal);
        Button button_add = layout.findViewById(R.id.add);
        Button button_dec = layout.findViewById(R.id.dec);
        Button button_multi = layout.findViewById(R.id.multi);
        Button button_div = layout.findViewById(R.id.div);
        Button button_dot = layout.findViewById(R.id.dot);
        Button button_zero = layout.findViewById(R.id.zero);
        Button button_one = layout.findViewById(R.id.one);
        Button button_two = layout.findViewById(R.id.two);
        Button button_three = layout.findViewById(R.id.three);
        Button button_four = layout.findViewById(R.id.four);
        Button button_five = layout.findViewById(R.id.five);
        Button button_six = layout.findViewById(R.id.six);
        Button button_seven = layout.findViewById(R.id.seven);
        Button button_eight = layout.findViewById(R.id.eight);
        Button button_nine = layout.findViewById(R.id.nine);

        Button button_factorial = layout.findViewById(R.id.factorial);
        Button button_cube_roots = layout.findViewById(R.id.cube_roots);
        Button button_pai = layout.findViewById(R.id.pai);
        Button button_radicals = layout.findViewById(R.id.radicals);
        Button button_tan = layout.findViewById(R.id.tan);
        Button button_power = layout.findViewById(R.id.power);
        Button button_cos = layout.findViewById(R.id.cos);
        Button button_cube = layout.findViewById(R.id.cube);
        Button button_sin = layout.findViewById(R.id.sin);
        Button button_square = layout.findViewById(R.id.square);


        mTextView = layout.findViewById(R.id.text_view);
        textview = layout.findViewById(R.id.text);
        button_AC.setOnClickListener(this);
        button_C.setOnClickListener(this);
        button_mod.setOnClickListener(this);
        button_equal.setOnClickListener(this);
        button_add.setOnClickListener(this);
        button_dec.setOnClickListener(this);
        button_multi.setOnClickListener(this);
        button_div.setOnClickListener(this);
        button_dot.setOnClickListener(this);
        button_zero.setOnClickListener(this);
        button_one.setOnClickListener(this);
        button_two.setOnClickListener(this);
        button_three.setOnClickListener(this);
        button_four.setOnClickListener(this);
        button_five.setOnClickListener(this);
        button_six.setOnClickListener(this);
        button_seven.setOnClickListener(this);
        button_eight.setOnClickListener(this);
        button_nine.setOnClickListener(this);

        button_factorial.setOnClickListener(this);
        button_cube_roots.setOnClickListener(this);
        button_pai.setOnClickListener(this);
        button_radicals.setOnClickListener(this);
        button_tan.setOnClickListener(this);
        button_power.setOnClickListener(this);
        button_cos.setOnClickListener(this);
        button_cube.setOnClickListener(this);
        button_sin.setOnClickListener(this);
        button_square.setOnClickListener(this);
        if(sb!=null||temp!=null||answer!=null||state||sb_state){
            mTextView.setText(sb.toString());
            textview.setText(temp.toString());
        }

        return layout;
    }

    private boolean state = false;
    private boolean sb_state = false;

    @Override
    public void onClick(View v) {
        DecimalFormat format = new DecimalFormat("0.#############");
        if(answer!=null&&answer.equals("运算失败")){
            state = false;
            sb_state = false;
            answer=null;
            sb = new StringBuilder();
            temp = new StringBuilder();
            mTextView.setText(sb.toString());
            textview.setText(temp.toString());
        }
        switch (v.getId()) {
            case R.id.zero:
                if (sb.length() != 0) {
                    if (sb_state) {
                        sb = sb.delete(0, sb.length());
                        sb_state = false;
                    }
                    sb = sb.append("0");
                    mTextView.setText(sb.toString());
                }
                break;
            case R.id.one:
                if (sb_state) {
                    sb = sb.delete(0, sb.length());
                    sb_state = false;
                }
                sb = sb.append("1");
                mTextView.setText(sb.toString());
                break;
            case R.id.two:
                if (sb_state) {
                    sb = sb.delete(0, sb.length());
                    sb_state = false;
                }
                sb = sb.append("2");
                mTextView.setText(sb.toString());
                break;
            case R.id.three:
                if (sb_state) {
                    sb = sb.delete(0, sb.length());
                    sb_state = false;
                }
                sb = sb.append("3");
                mTextView.setText(sb.toString());
                break;
            case R.id.four:
                if (sb_state) {
                    sb = sb.delete(0, sb.length());
                    sb_state = false;
                }
                sb = sb.append("4");
                mTextView.setText(sb.toString());
                break;
            case R.id.five:
                if (sb_state) {
                    sb = sb.delete(0, sb.length());
                    sb_state = false;
                }
                sb = sb.append("5");
                mTextView.setText(sb.toString());
                break;
            case R.id.six:
                if (sb_state) {
                    sb = sb.delete(0, sb.length());
                    sb_state = false;
                }
                sb = sb.append("6");
                mTextView.setText(sb.toString());
                break;
            case R.id.seven:
                if (sb_state) {
                    sb = sb.delete(0, sb.length());
                    sb_state = false;
                }
                sb = sb.append("7");
                mTextView.setText(sb.toString());
                break;
            case R.id.eight:
                if (sb_state) {
                    sb = sb.delete(0, sb.length());
                    sb_state = false;
                }
                sb = sb.append("8");
                mTextView.setText(sb.toString());
                break;
            case R.id.nine:
                if (sb_state) {
                    sb = sb.delete(0, sb.length());
                    sb_state = false;
                }
                sb = sb.append("9");
                mTextView.setText(sb.toString());
                break;
            case R.id.dot:
                if (sb_state) {
                    sb = sb.delete(0, sb.length());
                    sb_state = false;
                }
                boolean dot_state=false;
                for (int i = 0; i < sb.length(); i++) {
                    if(sb.charAt(i)=='.')
                        dot_state=true;
                }
                if (!dot_state){
                    sb = sb.append(".");
                    mTextView.setText(sb.toString());
                }
                break;
            //全删
            case R.id.AC:
                state = false;
                sb_state = false;
                answer=null;
                sb = new StringBuilder();
                temp = new StringBuilder();
                mTextView.setText(sb.toString());
                textview.setText(temp.toString());
                break;
            //清除当前
            case R.id.C:
                sb = new StringBuilder();
                sb_state = false;
                mTextView.setText(sb.toString());
                break;
            //加法
            case R.id.add:
                if (!state) {
                    state = true;
                    temp = new StringBuilder(sb);
                    temp.append("+");
                    textview.setText(temp.toString());
                } else {
                    if ((temp.charAt(temp.length() - 1) == '-' || temp.charAt(temp.length() - 1) == '×' || temp.charAt(temp.length() - 1) == '%' || temp.charAt(temp.length() - 1) == '÷' || temp.charAt(temp.length() - 1) == '+'|| temp.charAt(temp.length() - 1) == '='|| temp.charAt(temp.length() - 1) == '^' )&&sb_state) {
                        temp.deleteCharAt(temp.length() - 1);
                        temp.append("+");
                        textview.setText(temp.toString());
                    } else {
                        answer = jishuan();
                        temp.append(sb);
                        temp.append("+");
                        textview.setText(temp.toString());
                        sb = new StringBuilder(answer);
                        mTextView.setText(sb.toString());
                    }
                }
                sb_state = true;
                break;
            //减法
            case R.id.dec:
                if (!state) {
                    state = true;
                    temp = new StringBuilder(sb);
                    temp.append("-");
                    textview.setText(temp.toString());
                } else {
                    if ((temp.charAt(temp.length() - 1) == '-' || temp.charAt(temp.length() - 1) == '×' || temp.charAt(temp.length() - 1) == '%' || temp.charAt(temp.length() - 1) == '÷' || temp.charAt(temp.length() - 1) == '+'|| temp.charAt(temp.length() - 1) == '='|| temp.charAt(temp.length() - 1) == '^' )&&sb_state) {
                        temp.deleteCharAt(temp.length() - 1);
                        temp.append("-");
                        textview.setText(temp.toString());
                    } else {
                        answer = jishuan();
                        temp.append(sb);
                        temp.append("-");
                        textview.setText(temp.toString());
                        sb = new StringBuilder(answer);
                        mTextView.setText(sb.toString());
                    }
                }
                sb_state = true;
                break;
            //乘法
            case R.id.multi:
                if (!state) {
                    state = true;
                    temp = new StringBuilder(sb);
                    temp.append("×");
                    textview.setText(temp.toString());
                } else {
                    if ((temp.charAt(temp.length() - 1) == '-' || temp.charAt(temp.length() - 1) == '×' || temp.charAt(temp.length() - 1) == '%' || temp.charAt(temp.length() - 1) == '÷' || temp.charAt(temp.length() - 1) == '+'|| temp.charAt(temp.length() - 1) == '='|| temp.charAt(temp.length() - 1) == '^' )&&sb_state) {
                        temp.deleteCharAt(temp.length() - 1);
                        temp.append("×");
                        textview.setText(temp.toString());
                    } else {
                        answer = jishuan();
                        temp.append(sb);
                        temp.append("×");
                        textview.setText(temp.toString());
                        sb = new StringBuilder(answer);
                        mTextView.setText(sb.toString());
                    }
                }
                sb_state = true;
                break;
            //求余
            case R.id.mod:
                if (!state) {
                    state = true;
                    temp = new StringBuilder(sb);
                    temp.append("%");
                    textview.setText(temp.toString());
                } else {
                    if ((temp.charAt(temp.length() - 1) == '-' || temp.charAt(temp.length() - 1) == '×' || temp.charAt(temp.length() - 1) == '%' || temp.charAt(temp.length() - 1) == '÷' || temp.charAt(temp.length() - 1) == '+'|| temp.charAt(temp.length() - 1) == '='||temp.charAt(temp.length() - 1) == '^' )&&sb_state) {
                        temp.deleteCharAt(temp.length() - 1);
                        temp.append("%");
                        textview.setText(temp.toString());
                    } else {
                        answer = jishuan();
                        temp.append(sb);
                        temp.append("%");
                        textview.setText(temp.toString());
                        sb = new StringBuilder(answer);
                        mTextView.setText(sb.toString());
                    }
                }
                sb_state = true;
                break;
            //除法
            case R.id.div:
                if (!state) {
                    state = true;
                    temp = new StringBuilder(sb);
                    temp.append("÷");
                    textview.setText(temp.toString());
                } else {
                    if ((temp.charAt(temp.length() - 1) == '-' || temp.charAt(temp.length() - 1) == '×' || temp.charAt(temp.length() - 1) == '%' || temp.charAt(temp.length() - 1) == '÷' || temp.charAt(temp.length() - 1) == '+'|| temp.charAt(temp.length() - 1) == '='|| temp.charAt(temp.length() - 1) == '^' ) &&sb_state) {
                        temp.deleteCharAt(temp.length() - 1);
                        temp.append("÷");
                        textview.setText(temp.toString());
                    } else {
                        answer = format.format(jishuan());
                        temp.append(sb);
                        temp.append("÷");
                        textview.setText(temp.toString());
                        sb = new StringBuilder(answer);
                        mTextView.setText(sb.toString());
                    }
                }
                sb_state = true;
                break;
            //等于
            case R.id.equal:
                if (!state) {
                    state = true;
                    temp = new StringBuilder(sb);
                    textview.setText(temp.toString());
                } else {
                    answer = jishuan();
                    temp.append(sb);
                    temp.append("=");
                    textview.setText(temp.toString());
                    sb = new StringBuilder(answer);
                    mTextView.setText(sb.toString());
                }
                sb_state = true;
                break;

            //阶乘
            case R.id.factorial:
                answer=gaoji_jishuan();
                temp =new StringBuilder(sb);
                temp.append("!");
                sb=new StringBuilder(answer);
                textview.setText(temp.toString());
                mTextView.setText(sb.toString());
                sb_state = true;
                break;
            //立方根
            case R.id.cube_roots:
                try{
                    answer=format.format(Math.pow(Double.parseDouble(sb.toString()), 1.0/3));
                    temp = new StringBuilder("3√" + sb);
                    sb = new StringBuilder(answer);
                }catch (Exception e) {
                    answer = "运算失败";
                    sb = new StringBuilder(answer);
                }finally {
                    sb_state = true;
                    textview.setText(temp.toString());
                    mTextView.setText(sb.toString());
                }
                break;
            //开方
            case R.id.radicals:
                try{
                    answer=format.format(Math.pow(Double.parseDouble(sb.toString()), 1.0/2));
                    temp = new StringBuilder("√" + sb);
                    sb = new StringBuilder(answer);
                }catch (Exception e) {
                    answer = "运算失败";
                    sb = new StringBuilder(answer);
                }finally {
                    sb_state = true;
                    textview.setText(temp.toString());
                    mTextView.setText(sb.toString());
                }
                break;
            //π
            case R.id.pai:
                if (sb_state) {
                    sb = sb.delete(0, sb.length());
                    sb_state = false;
                }
                sb = sb.append(Math.PI);
                mTextView.setText(sb.toString());
                break;
            //平方
            case  R.id.square:
                try{
                    answer=format.format(Math.pow(Double.parseDouble(sb.toString()), 2));
                    temp = new StringBuilder(sb+"²");
                    sb = new StringBuilder(answer);
                }catch (Exception e) {
                    answer = "运算失败";
                    sb = new StringBuilder(answer);
                }finally {
                    sb_state = true;
                    textview.setText(temp.toString());
                    mTextView.setText(sb.toString());
                }
                break;
            //立方
            case  R.id.cube:
                try{
                    answer=format.format(Math.pow(Double.parseDouble(sb.toString()), 3));
                    temp = new StringBuilder(sb+"³");
                    sb = new StringBuilder(answer);
                }catch (Exception e) {
                    answer = "运算失败";
                    sb = new StringBuilder(answer);
                }finally {
                    sb_state = true;
                    textview.setText(temp.toString());
                    mTextView.setText(sb.toString());
                }
                break;
            //n次方
            case R.id.power:
                if (!state) {
                    state = true;
                    temp = new StringBuilder(sb);
                    temp.append("^");
                    textview.setText(temp.toString());
                } else {
                    if ((temp.charAt(temp.length() - 1) == '-' || temp.charAt(temp.length() - 1) == '×' || temp.charAt(temp.length() - 1) == '%' || temp.charAt(temp.length() - 1) == '÷' || temp.charAt(temp.length() - 1) == '+'|| temp.charAt(temp.length() - 1) == '='|| temp.charAt(temp.length() - 1) == '^' )&&sb_state) {
                        temp.deleteCharAt(temp.length() - 1 );
                        temp.append("^");
                        textview.setText(temp.toString());
                    } else {
                        answer =format.format( jishuan());
                        temp.append(sb);
                        temp.append("^");
                        textview.setText(temp.toString());
                        sb = new StringBuilder(answer);
                        mTextView.setText(sb.toString());
                    }
                }
                sb_state = true;
                break;
            case R.id.cos:
                try {
                    answer = format.format(Math.cos((Double.parseDouble(sb.toString())/180)*Math.PI));
                    temp = new StringBuilder("cos" + sb);
                    sb = new StringBuilder(answer);
                }catch (Exception e) {
                    answer = "运算失败";
                    sb = new StringBuilder(answer);
                }finally {
                    sb_state = true;
                    textview.setText(temp.toString());
                    mTextView.setText(sb.toString());
                }
                break;
            case R.id.tan:
                try {
                    answer = format.format(Math.tan((Double.parseDouble(sb.toString())/180)*Math.PI));
                    temp = new StringBuilder("tan" + sb);
                    sb = new StringBuilder(answer);
                }catch (Exception e) {
                    answer = "运算失败";
                    sb = new StringBuilder(answer);
                }finally {
                    sb_state = true;
                    textview.setText(temp.toString());
                    mTextView.setText(sb.toString());
                }
                break;
            case R.id.sin:
                try {
                    answer = format.format(Math.sin((Double.parseDouble(sb.toString())/180)*Math.PI));
                    temp = new StringBuilder("sin" + sb);
                    sb = new StringBuilder(answer);
                }catch (Exception e) {
                    answer = "运算失败";
                    sb = new StringBuilder(answer);
                }finally {
                    sb_state = true;
                    textview.setText(temp.toString());
                    mTextView.setText(sb.toString());
                }
                break;
        }
    }

    public String jishuan() {
        List<String> list = new ArrayList<>();
        if (answer == null||answer == "运算失败") {
            if(temp.charAt(0)>='0'&&temp.charAt(0)<='9')
                list.add(temp.substring(0, temp.length() - 1));
            else
                return "运算失败";
        } else {
            list.add(answer);
        }

        list.add(String.valueOf(temp.charAt(temp.length() - 1)));
        list.add(sb.toString());
        BigDecimal a;
        switch (list.remove(1)) {
            case "+":
                a = new BigDecimal(list.remove(0)).add(new BigDecimal(list.remove(0)));
                list.add(a.stripTrailingZeros().toString());
                break;
            case "-":
                a = new BigDecimal(list.remove(0)).subtract(new BigDecimal(list.remove(0)));
                list.add(a.stripTrailingZeros().toString());
                break;
            case "×":
                a = new BigDecimal(list.remove(0)).multiply(new BigDecimal(list.remove(0)));
                list.add(a.stripTrailingZeros().toString());
                break;
            case "÷":
                a = new BigDecimal(list.remove(0)).divide(new BigDecimal(list.remove(0)));
                list.add(a.stripTrailingZeros().toString());
                break;
            case "%":
                a = new BigDecimal(list.remove(0)).divideAndRemainder(new BigDecimal(list.remove(0)))[1];
                list.add(a.stripTrailingZeros().toString());
                break;
            case "^":
                a = new BigDecimal(list.remove(0)).pow(new BigDecimal(list.remove(0)).intValue());
                list.add(a.stripTrailingZeros().toString());
                break;
        }
        if (list.size() == 1) {
            if (list.get(0).length() < 30) {
                BigDecimal bd = new BigDecimal(list.get(0));
                return bd.toPlainString();
            } else {
                double d = Double.parseDouble(list.get(0));
                return String.valueOf(d);
            }
        } else {
            return "运算失败";
        }

    }
    public String gaoji_jishuan() {
        int num= Integer.parseInt(sb.toString());
        long[] arr=new long[21];
        arr[0]=1;
        int last=0;
        if(num>=arr.length||num<0){
            return "运算失败";
        }
        while (last<num){
            arr[last+1]=arr[last]*(last+1);
            last++;
        }
        return String.valueOf(arr[num]);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("sb",sb.toString());
        outState.putString("temp",temp.toString());
        outState.putString("answer",answer);
        outState.putBoolean("state",state);
        outState.putBoolean("sb_state",sb_state);
        super.onSaveInstanceState(outState);
    }
}
