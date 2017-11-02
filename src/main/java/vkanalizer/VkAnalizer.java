package vkanalizer;

import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.users.User;
import com.vk.api.sdk.objects.users.UserMin;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import vkanalizer.dao.LikeDao;
import vkanalizer.dao.MemberDao;
import vkanalizer.dao.PostDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import vkanalizer.application.Application;
import vkanalizer.dao.RepostDao;
import vkanalizer.model.Like;
import vkanalizer.model.Member;
import vkanalizer.model.Post;
import vkanalizer.model.Repost;
import vkanalizer.vk.VkClientInstance;
import vkanalizer.vk.config.VkClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static vkanalizer.model.Member.parseUserXtrCountersToMember;
import static vkanalizer.model.Member.parseUserXtrRoleToMember;
import static vkanalizer.model.Post.wallpostToPost;


/**
 * Created by Nikolay V. Petrov on 02.09.2017.
 */

@SpringBootApplication
@Import({
        VkClientConfiguration.class,
        PostDao.class,
        MemberDao.class,
        LikeDao.class,
        RepostDao.class
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

    @Autowired
    LikeDao likeDao;

    @Autowired
    RepostDao repostDao;

    public static void main(String... args) {
        ApplicationContext context = Application.start(VkAnalizer.class, args);
    }

    @Scheduled(fixedDelay = 1800000)
    void doStuff() throws ClientException, ApiException, InterruptedException {

        checkMembers();
        getLikes();

    }

    void checkMembers() throws ClientException, ApiException {
        StringBuilder membersMessage = new StringBuilder();

        List<Member> membersList = parseUserXtrRoleToMember(vkClientInstance.getMembers().getItems());
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
                membersMessage
                        .append(mem.toString())
                        .append("\n");
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
                membersMessage
                        .append(mem.toString())
                        .append("\n");
            }
        }

        if (membersMessage.length() != 0) {
            log.info(membersMessage.toString());
            vkClientInstance.sendMemberMessage(membersMessage.toString());
        }
    }

    void getLikes() throws ClientException, ApiException, InterruptedException {
        GetResponse response = vkClientInstance.getWall();
        Post post;
        Post inBase;
        for (WallpostFull wallpostFull : response.getItems()) {
            post = wallpostToPost(wallpostFull);
            inBase = postDao.findById(post.getId());

            Thread.sleep(1000);

            List<Integer> list = new ArrayList<>();
            for (UserMin user : vkClientInstance.getLikes(wallpostFull.getId()).getItems()) {
                list.add(user.getId());
            }

            Like like = new Like(wallpostFull.getId(), list);
            Like likeInBase = likeDao.findById(like.getId());

            List<Integer> repostsList = new ArrayList<>();
            for (User user : vkClientInstance.getReposts(wallpostFull.getId()).getProfiles()) {
                repostsList.add(user.getId());
            }

            Repost repost = new Repost(wallpostFull.getId(), repostsList);
            Repost repostInBase = repostDao.findById(repost.getId());

            StringBuilder message = new StringBuilder();

            if (inBase == null) {
                String newMessage = "Новая запись в сообществе: id=" + post.getId() +
                        ", лайки = " + like.getLikes() +
                        ", репосты = " + repost.getReposts();
                log.info(newMessage);
                postDao.insert(post);
                likeDao.insert(like);
                repostDao.insert(repost);
                vkClientInstance.sendPostMessage(post.getId(), newMessage);
            } else {

                postDao.update(post);

                if (!like.equals(likeInBase)) {

                    List<Integer> newLikes = new ArrayList<>();
                    for (Integer id : like.getLikes()) {
                        if (!likeInBase.getLikes().contains(id)) {
                            newLikes.add(id);
                        }
                    }

                    if (newLikes.size() != 0) {

                        Thread.sleep(1000);

                        message
                                .append("Новые лайки:")
                                .append("\n");
                        List<Member> members = parseUserXtrCountersToMember(vkClientInstance.getUsersInfo(newLikes));
                        for (Member member : members) {
                            message
                                    .append(member.toString())
                                    .append("\n");
                        }
                    }

                    List<Integer> lostLikes = new ArrayList<>();
                    for (Integer id : likeInBase.getLikes()) {
                        if (!like.getLikes().contains(id)) {
                            lostLikes.add(id);
                        }
                    }

                    if (lostLikes.size() != 0) {

                        Thread.sleep(1000);

                        message
                                .append("Снятые лайки:")
                                .append("\n");
                        List<Member> members = parseUserXtrCountersToMember(vkClientInstance.getUsersInfo(lostLikes));
                        for (Member member : members) {
                            message
                                    .append(member.toString())
                                    .append("\n");
                        }
                    }

                    message
                            .append("Лайков было/стало: ")
                            .append(likeInBase.getLikes().size())
                            .append("/")
                            .append(like.getLikes().size())
                            .append("\n");

                    likeDao.update(like);
                }


                if (!repost.equals(repostInBase)) {

                    List<Integer> newReposts = new ArrayList<>();
                    for (Integer id : repost.getReposts()) {
                        if (!repostInBase.getReposts().contains(id)) {
                            newReposts.add(id);
                        }
                    }

                    if (newReposts.size() != 0) {

                        Thread.sleep(1000);

                        message
                                .append("Новые репосты:")
                                .append("\n");
                        List<Member> members = parseUserXtrCountersToMember(vkClientInstance.getUsersInfo(newReposts));
                        for (Member member : members) {
                            message
                                    .append(member.toString())
                                    .append("\n");
                        }
                    }

                    List<Integer> lostReposts = new ArrayList<>();
                    for (Integer id : repostInBase.getReposts()) {
                        if (!repost.getReposts().contains(id)) {
                            lostReposts.add(id);
                        }
                    }

                    if (lostReposts.size() != 0) {

                        Thread.sleep(1000);

                        message
                                .append("Снятые репосты:")
                                .append("\n");
                        List<Member> members = parseUserXtrCountersToMember(vkClientInstance.getUsersInfo(lostReposts));
                        for (Member member : members) {
                            message
                                    .append(member.toString())
                                    .append("\n");
                        }
                    }

                    message
                            .append("Репостов было/стало: ")
                            .append(repostInBase.getReposts().size())
                            .append("/")
                            .append(repost.getReposts().size())
                            .append("\n");

                    repostDao.update(repost);
                }

                if (!Objects.equals(post.getComments(), inBase.getComments()))
                    message
                            .append("Комментариев было/стало:")
                            .append(inBase.getComments())
                            .append("/")
                            .append(post.getComments());
            }

            if (!message.toString().isEmpty()) {
                log.info(wallpostFull.getId()+ ":" + message.toString());
                vkClientInstance.sendPostMessage(wallpostFull.getId(), message.toString());
            }
        }
    }
}
