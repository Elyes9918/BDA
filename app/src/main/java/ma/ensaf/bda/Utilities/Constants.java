package ma.ensaf.bda.Utilities;

import java.util.HashMap;

public class Constants {
    public static final String KEY_TABLE="users";
    public static final String KEY_ID="id";
    public static final String KEY_NAME="name";
    public static final String KEY_EMAIL="email";
    public static final String KEY_ID_NUMBER="idnumber";
    public static final String KEY_PHONE_NUMBER="phonenumber";
    public static final String KEY_BLOOD_GROUP="bloodgroup";
    public static final String KEY_TYPE="type";
    public static final String KEY_SEARCH="search";
    public static final String KEY_PROFILE_PIC="profilepictureurl";
    public static final String KEY_USER="user";
    public static final String KEY_FCM_TOKEN ="fcmToken";

    public static final String KEY_TABLE_CHAT="chat";
    public static final String KEY_SENDER_ID="senderId";
    public static final String KEY_RECEIVER_ID="receiverId";
    public static final String KEY_MESSAGE="message";
    public static final String KEY_TIMESTAMP="timestamp";
    public static final String KEY_SENDER_NAME="senderName";
    public static final String KEY_RECEIVER_NAME="receiverName";
    public static final String KEY_SENDER_IMAGE="senderImage";
    public static final String KEY_RECEIVER_IMAGE="receiverImage";

    public static final String KEY_TABLE_CONVERSATIONS="conversations";
    public static final String KEY_LAST_MESSAGE="lastMessage";
    public static final String KEY_AVAILABILITY="availability";

    public static final String REMOTE_MSG_AUTHORIZATION="Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE="Content-Type";
    public static final String REMOTE_MSG_DATA = "data";
    public static final String REMOTE_MSG_REGISTRATION_IDS="registration_ids";


    public static HashMap<String,String> remoteMsgHeaders = null;
    public static HashMap<String,String> getRemoteMsgHeaders(){
        if(remoteMsgHeaders==null){
            remoteMsgHeaders=new HashMap<>();
            remoteMsgHeaders.put(
                    REMOTE_MSG_AUTHORIZATION,
                    "key=AAAAqsKtA_Y:APA91bH88PUdu86AcJ32WIKf1E1Zi3sTHQl1cLCm-Uv-gFwzsKMUyBF_Oaqm4A3ukb71bD_Xl5uBIu8gmbcfDsrSl3e6CimX7TuAr5SR7kwsQkqw-930gy09cYIZMWC8deKc4-_8z-NV"

            );
            remoteMsgHeaders.put(
                    REMOTE_MSG_CONTENT_TYPE,
                    "application/json"
            );
        }
        return  remoteMsgHeaders;
    }


}
