package com.hienthai.roomexample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hienthai.roomexample.R;
import com.hienthai.roomexample.entities.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;

    private OnclickItemUser onclickItemUser;

    public UserAdapter(OnclickItemUser onclickItemUser) {
        this.onclickItemUser = onclickItemUser;
    }

    public void setData(List<User> users) {
        this.userList = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = userList.get(position);

        if (user == null) {
            return;
        }

        holder.txtName.setText(user.getName());
        holder.txtAddress.setText(user.getAddress());

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickItemUser.editUser(user);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickItemUser.deleteUser(user);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (userList != null) {
            return userList.size();
        }
        return 0;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtAddress;
        private ImageView imgEdit,imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtAddress = itemView.findViewById(R.id.txtAddress);

            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

    public interface OnclickItemUser{
        void editUser(User user);
        void deleteUser(User user);
    }
}
