package com.example.cms.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cms.Models.CalDataModel;
import com.example.cms.Models.LeedsTableModel;
import com.example.cms.Models.StructuredRecordList;
import com.example.cms.R;

import java.util.List;

public class CalDataAdapter extends RecyclerView.Adapter<CalDataAdapter.ViewHolder>{

    private Context mCtx;
    private List<CalDataModel> calList;

    private List<StructuredRecordList> list;

    public CalDataAdapter(Context mCtx, List<CalDataModel> calList) {
        this.mCtx = mCtx;
        this.calList = calList;
    }


    @NonNull
    @Override
    public CalDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.trialcal_list,null);
        return new CalDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalDataAdapter.ViewHolder holder, int position) {

        CalDataModel calDataModel = calList.get(position);

        holder.capital.setText(calDataModel.getCapital());
        holder.month.setText(calDataModel.getMonth());
        holder.intrest.setText(calDataModel.getIntrest());
        holder.outstanding.setText(calDataModel.getOutstanding());
        holder.installment.setText(calDataModel.getInstallment());
    }

    @Override
    public int getItemCount() {
        return calList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout recyclerItem;
        TextView capital,outstanding,intrest,month,installment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerItem = itemView.findViewById(R.id.calRecyclerList);
            capital = itemView.findViewById(R.id.txtCapital);
            month = itemView.findViewById(R.id.txtMonth);
            outstanding = itemView.findViewById(R.id.txtOutstanding);
            intrest = itemView.findViewById(R.id.txtInterest);
            installment = itemView.findViewById(R.id.txtInstallment);

        }
    }
}
