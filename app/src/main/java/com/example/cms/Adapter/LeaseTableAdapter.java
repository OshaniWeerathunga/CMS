package com.example.cms.Adapter;

import android.annotation.SuppressLint;
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

import com.example.cms.Leasing.AddEditLeasing;
import com.example.cms.Leeds.LeedsUpdate;
import com.example.cms.Leeds.SanctionLetterDisplay;
import com.example.cms.Models.LeaseTableModel;
import com.example.cms.Models.LeedsTableModel;
import com.example.cms.R;

import java.util.List;

public class LeaseTableAdapter extends RecyclerView.Adapter<LeaseTableAdapter.ViewHolder> {

    private Context mCtx;
    private List<LeaseTableModel> leaseList;

    public LeaseTableAdapter(Context mCtx, List<LeaseTableModel> leaseList) {
        this.mCtx = mCtx;
        this.leaseList = leaseList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.leasing_list,null);
        return new LeaseTableAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LeaseTableModel leaseTableModel = leaseList.get(position);

        //set visibility of edit option
        if (leaseTableModel.getLeaseStatus().equals("in progress")){
            holder.imagedit.setVisibility(View.VISIBLE);
        }



        holder.applicationNo.setText(leaseTableModel.getLeaseApplicationNo());
        holder.customer.setText(leaseTableModel.getLeaseCustomer());
        holder.product.setText(leaseTableModel.getLeaseProduct());
        holder.asset.setText(leaseTableModel.getLeaseAsset());
        holder.action.setText(leaseTableModel.getLeaseAction());
        holder.status.setText(leaseTableModel.getLeaseStatus());

        /*
        //click edit button open edit form
        holder.imagedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, AddEditLeasing.class);
                intent.putExtra("id", leaseTableModel.getUserid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });

         */

        //click sanction button open edit form
        holder.imageSanction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, SanctionLetterDisplay.class);
                intent.putExtra("userid", leaseTableModel.getUserid());
                System.out.println("useId is ----"+ leaseTableModel.getUserid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });


        /*/click layout open edit form
        holder.recyclerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, SanctionLetterDisplay.class);
                intent.putExtra("id", leaseTableModel.getUserid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });

         */
    }

    @Override
    public int getItemCount() {
        return leaseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout recyclerItem;
        ImageView imagedit,imageSanction;
        TextView applicationNo,customer,product,asset,action,status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerItem = itemView.findViewById(R.id.lvTemplate);
            applicationNo = itemView.findViewById(R.id.txtApplication);
            customer = itemView.findViewById(R.id.txtCustomer);
            product = itemView.findViewById(R.id.txtProduct);
            asset = itemView.findViewById(R.id.txtAsset);
            action = itemView.findViewById(R.id.txtLastAction);
            status = itemView.findViewById(R.id.txtStatus);
            imagedit = itemView.findViewById(R.id.imgEdit);
            imageSanction = itemView.findViewById(R.id.imgSanction);

        }
    }
}


