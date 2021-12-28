package ma.ensaf.bda.Utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ma.ensaf.bda.Chat.Listeners.UserListeners;
import ma.ensaf.bda.Models.User;
import ma.ensaf.bda.databinding.UserDisplayedLayoutBinding;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private static Context context;
    private List<User> userList;
    private static UserListeners userListeners;


    public UserAdapter(Context context, List<User> userList,UserListeners userListeners) {
        this.context = context;
        this.userList = userList;
        this.userListeners=userListeners;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserDisplayedLayoutBinding userDisplayedLayoutBinding = UserDisplayedLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext())
                ,parent,
                false
        );
        return new ViewHolder(userDisplayedLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setUserData(userList.get(position));

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView userProfileImage;
        public TextView type,userName,userEmail,phoneNumber,bloodGroup;
        public Button contactmeButton;

        UserDisplayedLayoutBinding binding;


        public ViewHolder(@NonNull UserDisplayedLayoutBinding userDisplayedLayoutBinding) {
            super(userDisplayedLayoutBinding.getRoot());

            binding = userDisplayedLayoutBinding;

        }

        public void setUserData(User user) {

            binding.type.setText(user.getType());

            if(user.getType().equals("donor")){
                binding.contactmeButton.setVisibility(View.VISIBLE);
            }
            binding.bloodgroup.setText(user.getBloodgroup());
            binding.phoneNumber.setText(user.getPhonenumber());
            binding.userEmail.setText(user.getEmail());
            binding.userName.setText(user.getName());
            Glide.with(context).load(user.getProfilepictureurl()).into(binding.userPorfileImage);

            binding.contactmeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle("Contact this Person?")
                            .setMessage("Contact " + user.getName() + "?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    userListeners.OnUserClicked(user);

                                }})
                            .setNegativeButton("No", null)
                            .show();

                }
            });


        }
    }
}
