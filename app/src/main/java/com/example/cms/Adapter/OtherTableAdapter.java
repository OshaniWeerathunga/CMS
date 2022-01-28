package com.example.cms.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cms.Leeds.SanctionLetterDisplay;
import com.example.cms.Models.OtherTableModel;
import com.example.cms.R;

import java.util.List;

public class OtherTableAdapter extends RecyclerView.Adapter<OtherTableAdapter.ViewHolder> {

    private Context mCtx;
    private List<OtherTableModel> tableList;

    public OtherTableAdapter(Context mCtx, List<OtherTableModel> tableList) {
        this.mCtx = mCtx;
        this.tableList = tableList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.datatable_list,null);
        return new OtherTableAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OtherTableModel otherTableModel = tableList.get(position);

        holder.id.setText(otherTableModel.getId());
        holder.customer.setText(otherTableModel.getCustomer());
        holder.asset.setText(otherTableModel.getAsset());
        holder.product.setText(otherTableModel.getProduct());
        holder.rate.setText(otherTableModel.getRate());
        holder.tenor.setText(otherTableModel.getTenor());
        holder.amt.setText(otherTableModel.getFinanceAmt());
        holder.status.setText(otherTableModel.getStatus());


        holder.letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, SanctionLetterDisplay.class);
                intent.putExtra("userid", otherTableModel.getUserId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });

        /*
        holder.moredata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, MoreDataLoad.class);
                intent.putExtra("id",tableDataModel.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });

         */

    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout recyclerItem;
        TextView id,customer,asset,product,amt,rate,tenor,status;
        ImageView letter,moredata;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerItem = itemView.findViewById(R.id.datatableList);
            id = itemView.findViewById(R.id.txtId);
            customer = itemView.findViewById(R.id.txtCustomer);
            asset = itemView.findViewById(R.id.txtAssest);
            product = itemView.findViewById(R.id.txtProduct);
            amt = itemView.findViewById(R.id.txtAmt);
            rate = itemView.findViewById(R.id.txtRate);
            tenor = itemView.findViewById(R.id.txtTenor);
            status = itemView.findViewById(R.id.txtStatus);
            letter = itemView.findViewById(R.id.letter);
            moredata = itemView.findViewById(R.id.moreData);


        }
    }
}


