package main.bot;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiParamException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import main.Exceptions.HardResetException;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import static java.lang.System.exit;


public class Solution {

    public static long startTime;
    private static boolean DEBUG_STATE = false;
    private static String DEBUG_MESSAGE = null;
    private static int DEBUGID = 275752427;

    public static void main(String[] args) throws Exception {

        startTime = System.currentTimeMillis();

        ChatBot chatBot = new ChatBot(0);

        /*
        0 - Yandex Base (one word translation)
        1 - Yandex Extended (broken atm)
         */

        FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/config.properties"));
        Properties properties = new Properties();
        properties.load(fileInputStream);


        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        UserActor actor = new UserActor(Integer.parseInt(properties.getProperty("userId")), properties.getProperty("accessToken"));

//        UserActor actor = new UserActor(275752427, null); // id + accesstoken

        vk.messages().send(actor).
                userId(DEBUGID).message("BOT ONLINE").execute();

        while (true) {
            Thread.sleep(1000);
            List<Message> messageList = vk.messages().get(actor).execute().getItems(); //todo: create thread here
            for (Message message : messageList) {
                if (!message.isReadState()) {
                    int userId = message.getUserId();
                    System.out.println(message.getBody());
                    try {
                        if (message.getBody().toLowerCase().contains("cat") || message.getBody().toLowerCase().contains("кот") || message.getBody().toLowerCase().contains("кошка"))
                            chatBot.getATTACH().sendCat(vk, actor, message);
                        else
                            System.out.println(vk.messages().send(actor).
                                    userId(userId).message(checkDebug() + chatBot.sayInReturn(userId, message.getBody())).execute());
                    } catch (HardResetException e) {
                        System.out.println("BOT TURNED OFF BY " + userId);
                        exit(1);
                    } catch (ApiParamException e) {
                        System.out.println(vk.messages().send(actor).
                                userId(userId).message("And what do you want me to do with it?").execute());
                    }
                }
            }
        }
    }

    private static String checkDebug() {
        if (DEBUG_STATE)
            if (StringUtils.isNotEmpty(DEBUG_MESSAGE) && StringUtils.isNotBlank(DEBUG_MESSAGE))
                return "DEBUG IN PROGRESS - " + DEBUG_MESSAGE + ":" + System.lineSeparator();
            else return "DEBUG IN PROGRESS: " + System.lineSeparator();
        return StringUtils.EMPTY;
    }
}
