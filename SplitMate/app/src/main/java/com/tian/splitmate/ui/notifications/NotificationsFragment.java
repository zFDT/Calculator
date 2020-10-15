package com.tian.splitmate.ui.notifications;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tian.splitmate.R;
import com.tian.splitmate.ui.db.SQLHandler;
import com.tian.splitmate.ui.entities.Bill;

import java.util.ArrayList;
import java.util.List;

/**
 * 舔狗大全
 * 数据库中Bill展示
 */
public class NotificationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private BillAdapter mAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mAdapter.notifyDataSetChanged();
            updateData();
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = root.findViewById(R.id.Bill_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateData();
        return root;
    }

    //读取数据库数据进行更新。
    //这里如果有几条数据的Bill_ID是一样的，那么会将他们的钱和名字合并，确保为存入时数据一样
    private void updateData() {
        SQLHandler handler = new SQLHandler(getContext());
        List<Bill> bb = handler.getAllBill();
        List<Bill> temp_b = new ArrayList<>();
        for (int i = 0; i < bb.size(); i++) {
            int j = i + 1;
            if (j < bb.size() && bb.get(i).getBill_ID() == bb.get(j).getBill_ID()) {
                for (; j < bb.size(); j++) {
                    if (bb.get(i).getBill_ID() != bb.get(j).getBill_ID()) {
                        temp_b.add(bb.get(i));
                        i = j - 1;
                        break;
                    } else {
                        bb.get(i).setBill_Money(bb.get(i).getBill_Money() + bb.get(j).getBill_Money());
                        bb.get(i).setUser_Name(bb.get(i).getUser_Name() + "," + bb.get(j).getUser_Name());
                        if (j + 1 == bb.size()) {
                            temp_b.add(bb.get(i));
                            i = j - 1;
                            break;
                        }
                    }
                }
            }else if (j < bb.size() && bb.get(i).getBill_ID() != bb.get(j).getBill_ID() ||
                    (j == bb.size()
                            &&(
                            temp_b.isEmpty()||
                            (bb.get(i) != temp_b.get(temp_b.size() - 1))))
            ){
                temp_b.add(bb.get(i));
            }
        }
        bb = temp_b;
        mAdapter = new BillAdapter(bb);
        recyclerView.setAdapter(mAdapter);
    }

    private static class BillHolder extends RecyclerView.ViewHolder {
        private TextView bill_name;
        private TextView bill_kind;
        private TextView money;
        private TextView bill_date;
        private TextView bill_user;
        private Button context;
        private Bill mbill;

        public BillHolder(LayoutInflater inflater, ViewGroup group) {
            super(inflater.inflate(R.layout.fragment_one_bill_fragment, group, false));
            bill_name = itemView.findViewById(R.id.Bill_name);
            bill_kind = itemView.findViewById(R.id.Bill_kind);
            bill_user = itemView.findViewById(R.id.Bill_user);
            bill_date = itemView.findViewById(R.id.Bill_date);
            money = itemView.findViewById(R.id.money);
            context = itemView.findViewById(R.id.button_context);
        }

        void bind(Bill bill) {
            mbill = bill;
            bill_name.setText(bill.getBill_Name());
            bill_kind.setText(bill.getBill_Type());
            bill_date.setText(bill.getBill_Date());
            bill_user.setText(bill.getUser_Name());
            money.setText(bill.getBill_Money().toString());
        }

    }

    private class BillAdapter extends RecyclerView.Adapter<BillHolder> {
        private List<Bill> mBillList;

        public BillAdapter(List<Bill> mBillList) {
            this.mBillList = mBillList;
        }

        @NonNull
        @Override
        public BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new BillHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull BillHolder holder, final int position) {
            holder.bind(mBillList.get(position));
            holder.context.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(mBillList.get(position).getBill_content());
                    builder.setCancelable(true);
                    builder.setPositiveButton("好的，我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mBillList.size();
        }
    }
}
