package vkanalizer.vk;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.responses.GetListExtendedResponse;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.responses.PhotosSaveOwnerCoverPhotoResponse;
import com.vk.api.sdk.objects.photos.responses.SaveOwnerPhotoResponse;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.responses.GetRepostsResponse;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.objects.wall.responses.PostResponse;

import java.io.File;
import java.util.List;

/**
 * Created by Nikolay V. Petrov on 02.09.2017.
 */
public interface VkClientInstance {

    GetResponse getWall(int i) throws ClientException, ApiException;
    PostResponse setPhotoOnTheWall(File file) throws ClientException, ApiException;
    SaveOwnerPhotoResponse setAvatar(File file) throws ClientException, ApiException;
    SaveOwnerPhotoResponse setGroupAvatar(File file) throws ClientException, ApiException;
    PostResponse postInGroup(Photo photo) throws ClientException, ApiException;
    PhotosSaveOwnerCoverPhotoResponse setGroupCover(File file) throws ClientException, ApiException;
    void sendMessage(Integer id, String message) throws ClientException, ApiException;
    GetListExtendedResponse getLikes(Integer id) throws ClientException, ApiException;
    GetRepostsResponse getReposts(Integer id) throws ClientException, ApiException;
    List<UserXtrCounters> getUsers(List<String> ids) throws ClientException, ApiException;
}
