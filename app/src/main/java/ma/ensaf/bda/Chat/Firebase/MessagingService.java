package ma.ensaf.bda.Chat.Firebase;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

import ma.ensaf.bda.Chat.Activities.ChatActivity;
import ma.ensaf.bda.Models.User;
import ma.ensaf.bda.R;

import static ma.ensaf.bda.Utilities.Constants.KEY_FCM_TOKEN;
import static ma.ensaf.bda.Utilities.Constants.KEY_ID;
import static ma.ensaf.bda.Utilities.Constants.KEY_MESSAGE;
import static ma.ensaf.bda.Utilities.Constants.KEY_NAME;
import static ma.ensaf.bda.Utilities.Constants.KEY_USER;


public class MessagingService  extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        User user = new User();
        user.setId(remoteMessage.getData().get(KEY_ID));
        user.setName(remoteMessage.getData().get(KEY_NAME));
        user.setToken(remoteMessage.getData().get(KEY_FCM_TOKEN));

        int notificationId = new Random().nextInt();
        String channelId="chat_message";

        Intent intent = new Intent(this, ChatActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(KEY_USER,user);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId);
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentTitle(user.getName());
        builder.setContentText(remoteMessage.getData().get(KEY_MESSAGE));
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(
                remoteMessage.getData().get(KEY_MESSAGE)
        ));
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence channelName="Chat Message";
            String channelDescription ="This notification channel is used for chat message notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId,channelName,importance);
            channel.setDescription(channelDescription);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(notificationId,builder.build());
    }
}
