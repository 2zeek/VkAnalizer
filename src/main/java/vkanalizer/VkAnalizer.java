package vkanalizer;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.User;
import com.vk.api.sdk.objects.users.UserMin;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import vkanalizer.dao.PostDao;
import vkanalizer.dao.SmallUserDao;
import vkanalizer.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import vkanalizer.application.Application;
import vkanalizer.model.SmallUser;
import vkanalizer.vk.VkClientInstance;
import vkanalizer.vk.config.VkClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nikolay V. Petrov on 02.09.2017.
 */

@SpringBootApplication
@Import({
        VkClientConfiguration.class,
        PostDao.class,
        SmallUserDao.class
})
@EnableScheduling
public class VkAnalizer {

    private static Logger log = LoggerFactory.getLogger(VkAnalizer.class);

    @Autowired
    VkClientInstance vkClientInstance;

    @Autowired
    PostDao postDao;

    @Autowired
    SmallUserDao smallUserDao;

    public static void main(String... args) {
        ApplicationContext context = Application.start(VkAnalizer.class, args);
    }

    @Scheduled(fixedDelay = 1800000)
    void doStuff() throws ClientException, ApiException, InterruptedException {
        GetResponse response = vkClientInstance.getWall(-152033291);
        Post inBase;
        for (WallpostFull wallpostFull : response.getItems()) {
            Thread.sleep(1000);
            //Получаем список лайков и пользователей под записью
            List<UserMin> likesUser = vkClientInstance.getLikes(wallpostFull.getId()).getItems();
            //Получаем список репостов и пользователей под записью
            List<User> repostsUser = vkClientInstance.getReposts(wallpostFull.getId()).getProfiles();

            //Список идентификаторов лайкнувших пользователей
            List<Integer> likesUserId = new ArrayList<>();
            for (UserMin user : likesUser) {
                likesUserId.add(user.getId());
            }

            //Список идентиикаторов репостнувших пользователей
            List<Integer> repostUserIds = new ArrayList<>();
            if (!repostsUser.isEmpty()) {
                for (User user : repostsUser) {
                    repostUserIds.add(user.getId());
                }
            }

            //
            Post post = new Post();
            post.setId(wallpostFull.getId());
            post.setText(wallpostFull.getText());
            post.setLikes(wallpostFull.getLikes().getCount());
            post.setReposts(wallpostFull.getReposts().getCount());
            post.setLikesList(likesUserId);
            post.setRepostsList(repostUserIds);

            inBase = postDao.findById(post.getId());
            if (inBase == null) {
                log.info("Новая запись в сообществе");
                postDao.insert(post);
//                vkClientInstance.sendMessage(post.getId(),
//                 "Новая запись в сообществе"
//                         + "\nЛайков/репостов: " + post.getLikes() + "/" + post.getReposts());
            } else if (!inBase.equals(post)) {
                postDao.update(post);
                StringBuilder message = new StringBuilder();
                if (post.getLikes() - inBase.getLikes() != 0)
                    message
                            .append("\nЛайков было/стало: ")
                            .append(inBase.getLikes())
                            .append("/")
                            .append(post.getLikes());
                if (post.getReposts() - inBase.getReposts() != 0)
                    message
                            .append("\nРепостов было/стало: ")
                            .append(inBase.getReposts())
                            .append("/")
                            .append(post.getReposts());
                if (post.getLikesList().equals(inBase.getLikesList())) {
                    List<Integer> newLikes = new ArrayList<>(post.getLikesList());
                    newLikes.removeAll(inBase.getLikesList());
                    message
                            .append("\nНовые лайки: ");
                    for (Integer newlike : newLikes) {
                        if (smallUserDao.findById(newlike) == null) {

                        }
                        message
                                .append("\n")
                                .append(smallUserDao.findById(newlike));
                    };

                    List<Integer> oldLikes = new ArrayList<>(inBase.getLikesList());
                    oldLikes.removeAll(post.getLikesList());
                    message
                            .append("\nСнятые лайки: ");
                    for (Integer oldLike: oldLikes)
                        message
                                .append("\n")
                                .append(smallUserDao.findById(oldLike));
                }
                log.info(post.getId() + ": " + message);
                //vkClientInstance.sendMessage(post.getId(), message);
            }

        }
    }
}
