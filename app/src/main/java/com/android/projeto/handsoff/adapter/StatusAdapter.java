package com.android.projeto.handsoff.adapter;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.projeto.handsoff.R;
import com.android.projeto.handsoff.domain.Status;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter {

    private List<Status> statusList;

    public StatusAdapter(List<Status> statusList) {
        this.statusList = statusList;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View card = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_card, parent, false);
        return new StatusViewHolder(card);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Status status = statusList.get(position);

        StatusViewHolder svh = (StatusViewHolder) holder;
        svh.textTitle.setText(status.getTitle());
        svh.textStatus.setText(status.getDescription());
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        TextView textStatus;
        Button deleteStatus, updateStatus;

        private StatusViewHolder(View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textTitle);
            textStatus = itemView.findViewById(R.id.textStatus);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    //Pegar a posição.
                    LayoutInflater inflater = LayoutInflater.from(view.getContext());
                    final View v = inflater.inflate(R.layout.alert_dialog_update_delete_status, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setView(v);

                    updateStatus = v.findViewById(R.id.updateStatus);
                    deleteStatus = v.findViewById(R.id.deleteStatus);

                    builder.setCancelable(true);
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.getWindow().setGravity(Gravity.CENTER);
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    alertDialog.show();

                    updateStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //PEDIR AJUDA PRO DIEGO AMANHÃ
                            //usuarioDAO.onCreateStatus(status, getActivity());
                        }
                    });

                    deleteStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //DELETAR O STATUS
                        }
                    });
                }
            });
        }
    }
}
