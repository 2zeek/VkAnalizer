package vkanalizer.vk;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.groups.responses.GetMembersFieldsResponse;
import com.vk.api.sdk.objects.likes.responses.GetListExtendedResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.responses.GetRepostsResponse;
import com.vk.api.sdk.objects.wall.responses.GetResponse;

import java.util.List;

/**
 * Created by Nikolay V. Petrov on 02.09.2017.
 */
public interface VkClientInstance {

    GetResponse getWall() throws ClientException, ApiException;
    GetListExtendedResponse getLikes(Integer id) throws ClientException, ApiException;
    GetRepostsResponse getReposts(Integer id) throws ClientException, ApiException;
    List<UserXtrCounters> getUsersInfo(List<Integer> list) throws ClientException, ApiException;
    void sendPostMessage(Integer id, String message) throws ClientException, ApiException;
    void sendMemberMessage(String message) throws ClientException, ApiException;
    GetMembersFieldsResponse getMembers() throws ClientException, ApiException;
}
