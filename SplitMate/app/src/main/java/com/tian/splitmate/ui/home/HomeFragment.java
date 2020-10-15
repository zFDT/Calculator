package com.tian.splitmate.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.tian.splitmate.AddUserActivity;
import com.tian.splitmate.R;
import com.tian.splitmate.ui.db.SQLHandler;
import com.tian.splitmate.ui.entities.Bill;
import com.tian.splitmate.ui.entities.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 我就要舔狗
 * 用于展示每个人的消费情况
 */
public class HomeFragment extends Fragment {
    private Spinner spinner;
    private TextView username;
    private List<User> users;
    private PieChart pieChart;
    private List<Bill> bills;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        spinner = root.findViewById(R.id.user_spinner);
        username = root.findViewById(R.id.user_name);
        pieChart = root.findViewById(R.id.pieChart);
        users = getUserList();
        bills = new ArrayList<>();
        //初始化如果Bill为则显示
        if (bills.isEmpty()) {
            pieChart.setNoDataText("我还没开始花钱舔呢");
        }
        String[] ids = new String[users.size()];
        int i = 0;
        for (User us : users) {
            ids[i++] = "" + us.getUserName();
        }
        ArrayAdapter<String> user_adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ids);
        user_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(user_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bills = getUList();
                if (bills.isEmpty()) {
                    pieChart.setNoDataText("我还没开始花钱舔呢");
                } else {
                    //Bill不为空时设置饼图的属性
                    List<PieEntry> pieEntris = new ArrayList<>();
                    List<Integer> color = new ArrayList<>();
                    color.add(Color.BLUE);
                    color.add(Color.RED);
                    color.add(Color.GREEN);
                    color.add(Color.YELLOW);
                    color.add(Color.DKGRAY);
                    double sum = 0;
                    for (int j = 0; j < bills.size(); j++) {
                        sum += bills.get(j).getBill_Money();
                    }
                    for (int j = 0; j < bills.size(); j++) {
                        pieEntris.add(new PieEntry((float) ((bills.get(j).getBill_Money() / sum) * 100), bills.get(j).getBill_Type()));
                    }
                    //设置饼图的数据和颜色
                    PieDataSet pieDataSet = new PieDataSet(pieEntris, "");
                    pieDataSet.setColors(color);
                    PieData pieData = new PieData(pieDataSet);
                    pieChart.setData(pieData);
                    pieData.setValueFormatter(new PercentFormatter());
                    pieData.setValueTextColor(Color.parseColor("#FFFFFF"));
                    pieData.setValueTextSize(10f);
                    Description description = new Description();
                    description.setText("");
                    pieChart.setDescription(description);
                    pieChart.setHoleRadius(0.0F);
                    pieChart.setTransparentCircleAlpha(0);
                    pieChart.setDrawEntryLabels(true);
                    pieChart.invalidate();
                    username.setText("舔她我总共花了： " + sum);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(1);
            }
        });
        //按 再舔一个 时打开AddUserActivity进行添加用户进数据库
        root.findViewById(R.id.adduser_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddUserActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    public List<User> getUserList() {
        return new SQLHandler(getContext()).getAllUser();
    }

    public List<Bill> getUList() {
        return new SQLHandler(getContext()).getBills(spinner.getSelectedItem().toString());
    }
}
