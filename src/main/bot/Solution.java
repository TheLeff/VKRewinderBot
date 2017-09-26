package main.bot;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
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

        startTime = System.currentTimeMillis();
        ChatBot chatBot = new ChatBot();

        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        FileInputStream fileInputStream = new FileInputStream(new File("src/main/resources/config.properties"));
        Properties properties = new Properties();
        properties.load(fileInputStream);

        UserActor actor = new UserActor(Integer.parseInt(properties.getProperty("userId")), properties.getProperty("accessToken"));

        vk.messages().send(actor).
                userId(275752427).message("BOT ONLINE").execute();



        while (true) {
            Thread.sleep(1000);
            List<Message> messageList = vk.messages().get(actor).execute().getItems();
            for (Message message : messageList) {
                if (!message.isReadState()) {
                    int userId = message.getUserId();
                    System.out.println(message.getBody());
                    try {

                        if (message.getBody().contains("cat") || message.getBody().contains("кот") || message.getBody().contains("кошка"))
                            AttachmentProcessor.sendCat(vk, actor, message);
                        else
                            System.out.println(vk.messages().send(actor).
                                    userId(userId).message(chatBot.sayInReturn(message.getBody())).execute());
                    } catch (HardResetException e) {
                        System.out.println("BOT TURNED OFF BY " + userId);
                        exit(1);
                    }
                }
            }
        }
    }
}
