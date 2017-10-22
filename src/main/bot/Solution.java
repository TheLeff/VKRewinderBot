package main.bot;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiParamException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.Message;
import main.Exceptions.HardResetException;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;

import static java.lang.System.exit;

public class Solution {

    static long startTime;

    public static void main(String[] args) throws Exception {

        int creatorID = 275752427;





        startTime = System.currentTimeMillis();
        ChatBot chatBot = new ChatBot();

        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/config.properties"));
        Properties properties = new Properties();
        properties.load(fileInputStream);

        UserActor actor = new UserActor(Integer.parseInt(properties.getProperty("userId")), properties.getProperty("accessToken"));

        vk.messages().send(actor).
                userId(creatorID).message("BOT ONLINE").execute();

        while (true) {
            Thread.sleep(1000); // todo: "do we make another thread to sleep and sync them or it's fine?" fix
            List<Message> messageList = vk.messages().get(actor).execute().getItems();
            for (Message message : messageList) {
                if (!message.isReadState()) {
                    int userId = message.getUserId();
                    System.out.println(message.getBody());
                    try {

                        if (message.getBody().contains("cat") || message.getBody().contains("кот") || message.getBody().contains("кошка"))
                            chatBot.getATTACH().sendCat(vk, actor, message);
                        else
                            System.out.println(vk.messages().send(actor).
                                    userId(userId).message(chatBot.sayInReturn(message.getBody())).execute());
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
}
