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

import com.example.cms.Leeds.LeedsUpdate;
import com.example.cms.Models.LeedsTableModel;
import com.example.cms.R;

import java.util.List;

public class LeedsTableAdapter extends RecyclerView.Adapter<LeedsTableAdapter.ViewHolder> {

    private Context mCtx;
    private List<LeedsTableModel> leedsList;

    public LeedsTableAdapter(Context mCtx, List<LeedsTableModel> leedsList) {
        this.mCtx = mCtx;
        this.leedsList = leedsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.leeds_list,null);
        return new LeedsTableAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LeedsTableModel leedsTableModel = leedsList.get(position);

        System.out.println(leedsTableModel.getAddress());


        holder.id.setText(leedsTableModel.getId());
        holder.nic.setText(leedsTableModel.getNic());
        holder.name.setText(leedsTableModel.getName());
        holder.mobile.setText(leedsTableModel.getMobile());
        holder.product.setText(leedsTableModel.getProduct());
        holder.followup.setText(leedsTableModel.getFollowup());
        holder.address.setText(leedsTableModel.getAddress());

        //click edit button open edit form
        holder.editform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, LeedsUpdate.class);
                intent.putExtra("id", leedsTableModel.getUserId());
                System.out.println("user id is ---- "+leedsTableModel.getUserId());
                intent.putExtra("leedsId", leedsTableModel.getId());
                intent.putExtra("topic", leedsTableModel.getTopic());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });

        //click layout open edit form
        holder.recyclerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mCtx, LeedsUpdate.class);
                intent.putExtra("id", leedsTableModel.getUserId());
                intent.putExtra("leedsId", leedsTableModel.getId());
                intent.putExtra("topic", leedsTableModel.getTopic());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mCtx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return leedsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout recyclerItem;
        ImageView editform;
        TextView id,nic,name,mobile,product,followup,address,pcity1,pcity2,pcity3,followupAction,mkofficerName,fullname,stage,branchValue,channel,shortName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerItem = itemView.findViewById(R.id.lvTemplate);
            id = itemView.findViewById(R.id.txtId);
            nic = itemView.findViewById(R.id.txtNic);
            name = itemView.findViewById(R.id.txtName);
            mobile = itemView.findViewById(R.id.txtMobile);
            product = itemView.findViewById(R.id.txtProduct);
            followup = itemView.findViewById(R.id.txtFollowup);
            address = itemView.findViewById(R.id.txtAddress);
            editform = itemView.findViewById(R.id.action);

        }
    }
}


