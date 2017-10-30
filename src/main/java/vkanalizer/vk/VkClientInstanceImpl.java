package vkanalizer.vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vkanalizer.vk.config.properties.VkClientProperties;

/**
 * Created by Nikolay V. Petrov on 28.08.2017.
 */

public class VkClientInstanceImpl implements VkClientInstance {

    private static Logger log = LoggerFactory.getLogger(VkClientInstanceImpl.class);

    private VkClientProperties vkClientProperties;
    private VkApiClient vkApiClient;

    private VkClientInstanceImpl(VkClientProperties vkClientProperties) {
        this.vkClientProperties = vkClientProperties;
        this.vkClientProperties.getUser().setActor(new UserActor(vkClientProperties.getUser().getId(),
                        vkClientProperties.getUser().getAccessToken()));
        this.vkClientProperties.getGroup().setActor(new GroupActor(vkClientProperties.getGroup().getId(),
                        vkClientProperties.getGroup().getAccessToken()));
        TransportClient transportClient = HttpTransportClient.getInstance();
        this.vkApiClient = new VkApiClient(transportClient);
    }

    public static VkClientInstance createNewClient(VkClientProperties vkClientProperties) {
        return new VkClientInstanceImpl(vkClientProperties);
    }

    public GetResponse getWall() throws ClientException, ApiException {
        return vkApiClient.wall().get((UserActor) vkClientProperties.getUser().getActor())
                .ownerId(-vkClientProperties.getGroup().getId())
                .count(100)
                .offset(0)
                .execute();
    }

    public void sendMessage(Integer id, String message) throws ClientException, ApiException {
        vkApiClient.messages().send((GroupActor) vkClientProperties.getGroup().getActor())
                .userId(vkClientProperties.getUser().getId())
                .message(message)
                .attachment("wall-" + vkClientProperties.getGroup().getId() + "_" + id)
                .execute();
    }
}