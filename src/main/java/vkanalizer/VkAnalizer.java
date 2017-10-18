package vkanalizer;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import dao.PostDao;
import dao.impl.PostDaoImpl;
import model.Post;
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

import static model.Post.wallpostToPost;


/**
 * Created by Nikolay V. Petrov on 02.09.2017.
 */

@SpringBootApplication
@Import({
        VkClientConfiguration.class,
        PostDaoImpl.class
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
    void doStuff() throws ClientException, ApiException {
        GetResponse response = vkClientInstance.getWall(-152033291);
        Post post;
        Post inBase;
        for (WallpostFull wallpostFull : response.getItems()) {
            post = wallpostToPost(wallpostFull);
            inBase = postDao.findById(post.getId());
            if (inBase == null) {
                log.info("No record in base. Save new data");
                postDao.insert(post);
            } else if (!inBase.equals(post)) {
                log.info("ALARM! in base: " + inBase.toString()
                        + "; new data: " + post.toString());
                postDao.update(post);
            }

        }
    }
}
