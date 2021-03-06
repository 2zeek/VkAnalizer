package vkanalizer;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import vkanalizer.dao.MemberDao;
import vkanalizer.dao.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import vkanalizer.application.Application;
import vkanalizer.model.Member;
import vkanalizer.model.Post;
import vkanalizer.vk.VkClientInstance;
import vkanalizer.vk.config.VkClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static vkanalizer.model.Member.parseToMember;
import static vkanalizer.model.Post.wallpostToPost;


/**
 * Created by Nikolay V. Petrov on 02.09.2017.
 */

@SpringBootApplication
@Import({
        VkClientConfiguration.class,
        PostDao.class,
        MemberDao.class
})
@EnableScheduling
public class VkAnalizer {

    private static Logger log = LoggerFactory.getLogger(VkAnalizer.class);

    @Autowired
    VkClientInstance vkClientInstance;

    @Autowired
    PostDao postDao;

    @Autowired
    MemberDao memberDao;

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
                log.info("Новая запись в сообществе: id=" + post.getId());
                postDao.insert(post);
                vkClientInstance.sendPostMessage(post.getId(), "Новая запись в сообществе");
            } else if (!inBase.equals(post)) {

                Thread.sleep(1000);

                postDao.update(post);
                StringBuilder postMessage = new StringBuilder();
                if (!Objects.equals(post.getLikes(), inBase.getLikes()))
                    postMessage
                            .append("Лайков было/стало: ")
                            .append(inBase.getLikes())
                            .append("/")
                            .append(post.getLikes())
                            .append("\n");
                if (!Objects.equals(post.getReposts(), inBase.getReposts()))
                    postMessage
                            .append("Репостов было/стало: ")
                            .append(inBase.getReposts())
                            .append("/").
                            append(post.getReposts())
                            .append("\n");
                if (!Objects.equals(post.getComments(), inBase.getComments()))
                    postMessage
                            .append("Комментариев было/стало:")
                            .append(inBase.getComments())
                            .append("/")
                            .append(post.getComments());
                log.info(post.getId() + ": " + postMessage.toString());
                vkClientInstance.sendPostMessage(post.getId(), postMessage.toString());
            }
        }

        StringBuilder membersMessage = new StringBuilder();

        List<Member> membersList = parseToMember(vkClientInstance.getMembers().getItems());
        Member memberInBase;
        List<Member> newUsers = new ArrayList<>();
        for (Member member : membersList) {
            memberInBase = memberDao.findById(member.getId());
            if (memberInBase == null) {
                newUsers.add(member);
                memberDao.insert(member);
            }
        }

        if (newUsers.size() != 0) {
            membersMessage.append("Новые пользователи:\n");
            for (Member mem : newUsers)
                membersMessage.append(mem.toString()).append("\n");
        }

        List<Member> leaved = new ArrayList<>();
        List<Member> listInBase = memberDao.getAllMembers();
        for (Member mem : listInBase) {
            if (!membersList.contains(mem)) {
                leaved.add(mem);
                memberDao.delete(mem.getId());
            }
        }

        if (leaved.size() != 0) {
            membersMessage.append("Ушли пользователи:\n");
            for (Member mem : leaved) {
                membersMessage.append(mem.toString()).append("\n");
            }
        }

        if (membersMessage.length() != 0) {
            log.info(membersMessage.toString());
            vkClientInstance.sendMemberMessage(membersMessage.toString());
        }
    }
}
