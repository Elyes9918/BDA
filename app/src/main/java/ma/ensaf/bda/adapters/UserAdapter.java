package ma.ensaf.bda.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import ma.ensaf.bda.R;
import ma.ensaf.bda.databinding.UserDisplayedLayoutBinding;
import ma.ensaf.bda.listeners.UserListener;
import ma.ensaf.bda.listeners.UserListenerBeta;
import ma.ensaf.bda.models.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;
    private static Context context;
    private UserListenerBeta userListenerBeta;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserDisplayedLayoutBinding userDisplayedLayoutBinding = UserDisplayedLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserViewHolder(userDisplayedLayoutBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {

        holder.setUserData(userList.get(position % userList.size()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        UserDisplayedLayoutBinding binding;

        public UserViewHolder(UserDisplayedLayoutBinding userDisplayedLayoutBinding) {
            super(userDisplayedLayoutBinding.getRoot());
            binding = userDisplayedLayoutBinding;
        }


        void setUserData(User user) {
            binding.type.setText(user.getType());
            binding.userName.setText(user.getName());
            binding.userEmail.setText(user.getEmail());
            binding.bloodGroup.setText(user.getBloodGroup());

            if(user.getProfilePictureUrl() != null)
            {
                Glide.with(binding.getRoot())
                        .load(user.getProfilePictureUrl())
                        .override(130,130)
                        .into(binding.userProfileImage);
            }

            if (user.getType().equals("donor")) {

                if (user.isAnonymous()) {
                    binding.userName.setText(R.string.anonymous_donor);
                    binding.userEmail.setText("Anonymous Email");
                    Glide.with(binding.getRoot()).load(R.drawable.profile_image).into(binding.userProfileImage);
                }
            }

            final String receiverName = user.getName();
            final String receiverId = user.getId();

        }

    }


}
