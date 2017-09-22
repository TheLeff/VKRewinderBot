package main.bot;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;

import java.util.ArrayList;
import java.util.Random;

class AttachmentProcessor {

    private static Random r = new Random();
    private static ArrayList<String> catArray = new ArrayList() {{
        add("photo275752427_456242731");
        add("photo275752427_456242191");
        add("photo275752427_456241696");
        add("photo275752427_456241455");
        add("photo275752427_456241209");
        add("photo275752427_456240826");
        add("photo275752427_456240421");
        add("photo275752427_387553197");
    }};

    AttachmentProcessor() {
    }

    static void sendCat(VkApiClient vk, UserActor actor, Message message) {


        try {
            int userId = message.getUserId();
            vk.messages().send(actor).userId(userId).attachment(catArray.get(r.nextInt(catArray.size()))).execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }



}
