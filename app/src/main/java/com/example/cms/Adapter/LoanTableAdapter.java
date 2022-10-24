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

import com.example.cms.Leeds.SanctionLetterDisplay;
import com.example.cms.Models.LeaseTableModel;
import com.example.cms.Models.LoanTableModel;
import com.example.cms.R;

import java.util.List;

public class LoanTableAdapter extends RecyclerView.Adapter<LoanTableAdapter.ViewHolder> {

    private Context mCtx;
    private List<LoanTableModel> loanList;

    public LoanTableAdapter(Context mCtx, List<LoanTableModel> loanList) {
        this.mCtx = mCtx;
        this.loanList = loanList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.loan_list,null);
        return new LoanTableAdapter.ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LoanTableModel loanTableModel = loanList.get(position);

        //set visibility of edit option
        if (loanTableModel.getStatus().equals("in progress")){
            holder.imagedit.setVisibility(View.VISIBLE);
        }



        holder.applicationNo.setText(loanTableModel.getLoanNo());
        holder.proposal.setText(loanTableModel.getProposal());
        holder.customer.setText(loanTableModel.getLoanCustomer());
        holder.product.setText(loanTableModel.getLoanProduct());
        holder.action.setText(loanTableModel.getLastAction());
        holder.status.setText(loanTableModel.getStatus());

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
                intent.putExtra("userid", loanTableModel.getUserId());
                System.out.println("useId is ----"+ loanTableModel.getUserId());
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
        return loanList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout recyclerItem;
        ImageView imagedit,imageSanction;
        TextView applicationNo,proposal,customer,product,action,status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerItem = itemView.findViewById(R.id.lvTemplate);
            applicationNo = itemView.findViewById(R.id.txtApplicationNo);
            proposal = itemView.findViewById(R.id.txtProposal);
            customer = itemView.findViewById(R.id.txtCustomer);
            product = itemView.findViewById(R.id.txtProduct);
            action = itemView.findViewById(R.id.txtLastAction);
            status = itemView.findViewById(R.id.txtStatus);
            imagedit = itemView.findViewById(R.id.imgEdit);
            imageSanction = itemView.findViewById(R.id.imgSanction);

        }
    }
}


