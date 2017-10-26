package vkanalizer;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import vkanalizer.dao.PostDao;
import vkanalizer.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import vkanalizer.application.Application;
import vkanalizer.vk.VkClientInstance;
import vkanalizer.vk.config.VkClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import static vkanalizer.model.Post.wallpostToPost;


/**
 * Created by Nikolay V. Petrov on 02.09.2017.
 */

@SpringBootApplication
@Import({
        VkClientConfiguration.class,
        PostDao.class
})
@EnableScheduling
public class VkAnalizer {

    private static Logger log = LoggerFactory.getLogger(VkAnalizer.class);

    @Autowired
    VkClientInstance vkClientInstance;

    @Autowired
    PostDao postDao;

    public static void main(String... args) {
        ApplicationContext context = Application.start(VkAnalizer.class, args);
    }

    @Scheduled(fixedDelay = 1800000)
    void doStuff() throws ClientException, ApiException, InterruptedException {
        GetResponse response = vkClientInstance.getWall();
        Post post;
        Post inBase;
        for (WallpostFull wallpostFull : response.getItems()) {
            post = wallpostToPost(wallpostFull);
            inBase = postDao.findById(post.getId());
            if (inBase == null) {
                log.info("Новая запись в сообществе");
                postDao.insert(post);
                vkClientInstance.sendMessage(post.getId(), "Новая запись в сообществе");
            } else if (!inBase.equals(post)) {

                Thread.sleep(1000);

                postDao.update(post);
                String message = "";
                if (post.getLikes() - inBase.getLikes() != 0)
                    message += "Лайков было/стало: " + inBase.getLikes() + "/" + post.getLikes() + "\n";
                if (post.getReposts() - inBase.getReposts() != 0)
                    message += "Репостов было/стало: " + inBase.getReposts() + "/" + post.getReposts() + "\n";
                if (post.getComments() - inBase.getComments() !=0)
                    message += "Комментариев было/стало:" + inBase.getComments() + "/" + post.getComments();
                log.info(post.getId() + ": " + message);
                vkClientInstance.sendMessage(post.getId(), message);
            }

        }
    }
}
