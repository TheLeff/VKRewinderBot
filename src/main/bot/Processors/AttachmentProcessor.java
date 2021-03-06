package main.bot.Processors;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.messages.Message;
import main.bot.Misc;

import java.util.ArrayList;
import java.util.Random;

public class AttachmentProcessor extends Processor {

    private Random r = new Random();
    private ArrayList<String> catArray = new ArrayList() {{
        add("photo275752427_456242731");
        add("photo275752427_456242191");
        add("photo275752427_456241696");
        add("photo275752427_456241455");
        add("photo275752427_456241209");
        add("photo275752427_456240826");
        add("photo275752427_456240421");
        add("photo275752427_387553197");
    }};

    public void sendCat(VkApiClient vk, UserActor actor, Message message) {

        String cat = catArray.get(r.nextInt(catArray.size()));
        if (!History.isEmpty())
            History.add(System.lineSeparator() + Misc.getUserName(actor.getId()) + " [" + message.getBody() + ']');
        else History.add((Misc.getUserName(actor.getId()) + " [" + message.getBody() + ']').substring(1));

        try {
            int userId = message.getUserId();
            vk.messages().send(actor).userId(userId).attachment(cat).execute();
        } catch (ApiException | ClientException e) {
            e.printStackTrace();
        }
    }

    @Override
    String getHistory() {
        return null;
    }
}
