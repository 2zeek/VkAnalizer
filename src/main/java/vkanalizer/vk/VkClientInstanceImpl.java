package vkanalizer.vk;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.groups.responses.GetMembersFieldsResponse;
import com.vk.api.sdk.objects.likes.responses.GetListExtendedResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import com.vk.api.sdk.queries.users.UserField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vkanalizer.vk.config.properties.VkClientProperties;

import java.util.ArrayList;
import java.util.List;

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

    public GetListExtendedResponse getLikes(Integer id) throws ClientException, ApiException {
        return vkApiClient.likes().getListExtended((UserActor) vkClientProperties.getUser().getActor(), LikesType.POST)
                .ownerId(-vkClientProperties.getGroup().getId())
                .itemId(id)
                .execute();
    }

    public List<UserXtrCounters> getUsersInfo(List<Integer> list) throws ClientException, ApiException {
        List<String> stringList = new ArrayList<>();
        for (Integer integer : list)
            stringList.add(String.valueOf(integer));
        return vkApiClient.users().get((UserActor) vkClientProperties.getUser().getActor())
                .userIds(stringList)
                .execute();
    }

    public void sendPostMessage(Integer id, String message) throws ClientException, ApiException {
        if (!vkClientProperties.isTestmode())
            vkApiClient.messages().send((GroupActor) vkClientProperties.getGroup().getActor())
                    .userId(vkClientProperties.getUser().getId())
                    .message(message)
                    .attachment("wall-" + vkClientProperties.getGroup().getId() + "_" + id)
                    .execute();
    }

    public void sendMemberMessage(String message) throws ClientException, ApiException {
        if (!vkClientProperties.isTestmode())
            vkApiClient.messages().send((GroupActor) vkClientProperties.getGroup().getActor())
                    .userId(vkClientProperties.getUser().getId())
                    .message(message)
                    .execute();
    }

    public GetMembersFieldsResponse getMembers() throws ClientException, ApiException {
        return vkApiClient.groups().getMembers((GroupActor) vkClientProperties.getGroup().getActor(), UserField.LISTS)
                .groupId(vkClientProperties.getGroup().getId().toString())
                .execute();
    }
}