package com.example.libraryapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libraryapp.R;
import com.example.libraryapp.models.Member;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder> {

    private final Context context;
    private List<Member> memberList;
    private OnMemberClickListener listener;

    public interface OnMemberClickListener {
        void onMemberClick(Member member);
    }

    public MemberAdapter(Context context, List<Member> memberList, OnMemberClickListener listener) {
        this.context = context;
        this.memberList = memberList;
        this.listener = listener;
    }

    public void updateList(List<Member> newList) {
        this.memberList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = memberList.get(position);
        holder.tvName.setText(member.getName());
        holder.tvPhone.setText(member.getPhone());
        holder.tvEmail.setText(member.getEmail());
        holder.tvCode.setText(member.getMemberIdNumber());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMemberClick(member);
            }
        });
    }

    @Override
    public int getItemCount() {
        return memberList != null ? memberList.size() : 0;
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPhone, tvEmail, tvCode;

        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvMemberName);
            tvPhone = itemView.findViewById(R.id.tvMemberPhone);
            tvEmail = itemView.findViewById(R.id.tvMemberEmail);
            tvCode = itemView.findViewById(R.id.tvMemberCode);
        }
    }
}
