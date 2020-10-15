package com.tian.splitmate.ui.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.tian.splitmate.R;
import com.tian.splitmate.ui.db.SQLHandler;
import com.tian.splitmate.ui.entities.Bill;
import com.tian.splitmate.ui.entities.User;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * 再舔一次
 * 添加Bill内容到数据库
 */
public class DashboardFragment extends Fragment implements View.OnClickListener, DatePicker.OnDateChangedListener {
    private int year, month, day;
    private AlertDialog dialog2;
    private Spinner bill_kind;
    private String[] user_name;
    private StringBuffer date, username;
    private LinearLayout llDate, ll_user;
    private TextView BillDate, BillUser;
    private EditText BillName, BillMoney, BillContent;
    private Button OK_button, realser_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        llDate = root.findViewById(R.id.ll_date);
        ll_user = root.findViewById(R.id.ll_user);
        BillDate = root.findViewById(R.id.Bill_date);
        BillUser = root.findViewById(R.id.Bill_user);
        BillName = root.findViewById(R.id.Bill_name);
        BillMoney = root.findViewById(R.id.Bill_money);
        BillContent = root.findViewById(R.id.Bill_Content);
        OK_button = root.findViewById(R.id.OK_button);
        realser_button = root.findViewById(R.id.realse_button);
        bill_kind = root.findViewById(R.id.Bill_kind);
        //设置监听，在OnClick实现
        llDate.setOnClickListener(this);
        ll_user.setOnClickListener(this);
        OK_button.setOnClickListener(this);
        realser_button.setOnClickListener(this);
        getvalues();
        return root;
    }

    /**
     * 实现当点击时得类
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_date:
                //通过自定义AlertDialog来实现弹出选项
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setPositiveButton("对，就是这一天", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (date.length() > 0) {
                            date.delete(0, date.length());
                        }
                        BillDate.setText(date.append(year).append("年").append(month).append("月").append(day).append("日"));
                        dialog.dismiss();

                    }
                });
                builder.setNegativeButton("算了吧，她约了别人", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog dialog = builder.create();
                //此AlertDialog的内容指定为dtalog_date
                View dialogView = View.inflate(getContext(), R.layout.dialog_date, null);
                final DatePicker datePicker = dialogView.findViewById(R.id.datePicker);
                dialog.setTitle("我鼓起了勇气约了她");
                dialog.setView(dialogView);
                dialog.show();
                datePicker.init(year, month - 1, day, this);
                break;
            case R.id.ll_user:
                final Boolean[] tempbollean = new Boolean[user_name.length];
                Arrays.fill(tempbollean, false);
                AlertDialog.Builder builder_user = new AlertDialog.Builder(getContext());
                builder_user.setTitle("谁说一次只能舔一个？");
                //此AlertDialog的内容为List《User》生成
                builder_user.setMultiChoiceItems(user_name, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        tempbollean[which] = isChecked;
                    }
                });
                //判断勾选了哪些人，并组成新的字符串显示在TextView上
                builder_user.setNegativeButton("舔了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (username.length() > 0) {
                            BillUser.setText(username.delete(0, username.length()));
                        }
                        for (int i = 0; i < tempbollean.length; i++) {
                            if (username.length() == 0 && tempbollean[i]) {
                                username.append(user_name[i]);
                            } else if (tempbollean[i]) {
                                username.append(",").append(user_name[i]);
                            }
                            tempbollean[i] = false;
                        }
                        BillUser.setText(username);
                        dialog2.dismiss();
                    }
                });
                dialog2 = builder_user.create();
                dialog2.show();
                break;
            //读取页面上的数据，并存入数据库
            //注意：如果是多人AA消费，会生成对应数量的Bill数据，但是只会生成同一个Bill_ID，用来唯一识别这一个BIll
            case R.id.OK_button:
                String[] userss = BillUser.getText().toString().split(",");
                int temp_id = radomID();
                for (String use : userss) {
                    SQLHandler handler = new SQLHandler(getContext());
                    Bill bill = new Bill();
                    bill.setBill_ID(temp_id);
                    bill.setUser_Name(use);
                    bill.setBill_content(BillContent.getText().toString());
                    bill.setBill_Type(bill_kind.getSelectedItem().toString());
                    bill.setBill_Name(BillName.getText().toString());
                    bill.setBill_Date(BillDate.getText().toString());
                    bill.setBill_Money((Double.parseDouble(BillMoney.getText().toString()) / userss.length));
                    handler.addBill(bill);
                }
                Toast.makeText(getContext(), R.string.toast_adduBill, Toast.LENGTH_SHORT).show();
                BillContent.setText("");
                bill_kind.setSelection(0);
                BillName.setText("");
                BillDate.setText("");
                BillMoney.setText("");
                BillUser.setText("");
                break;
            case R.id.realse_button:
                BillContent.setText("");
                bill_kind.setSelection(0);
                BillName.setText("");
                BillDate.setText("");
                BillMoney.setText("");
                BillUser.setText("");
                break;
        }
    }

    //生成一个0-10000之间不在数据库中的id，用来唯一识别每一个Bill
    private int radomID() {
        SQLHandler handler = new SQLHandler(getContext());
        int temp_id = (int) (Math.random() * 100000);
        if (!handler.getNewBillId(temp_id)) {
            return temp_id;
        } else {
            return radomID();
        }
    }


    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        this.year = year;
        this.month = monthOfYear;
        this.day = dayOfMonth;
    }

    private void getvalues() {
        Calendar calendar = Calendar.getInstance();
        List<User> users = new SQLHandler(getContext()).getAllUser();
        user_name = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            user_name[i] = users.get(i).getUserName();
        }
        date = new StringBuffer();
        username = new StringBuffer();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }
}
