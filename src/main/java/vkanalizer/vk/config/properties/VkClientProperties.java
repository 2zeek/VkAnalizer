package vkanalizer.vk.config.properties;

import com.vk.api.sdk.client.actors.Actor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.Nullable;

/**
 * Created by Nikolay V. Petrov on 02.09.2017.
 */

@PropertySource(value = "file:${config.directory}/vk-client.properties")
@ConfigurationProperties(prefix = "vk")
@Configuration
public class VkClientProperties {

    private Logger log = LoggerFactory.getLogger(VkClientProperties.class);

    public static class User {

        private Integer id;
        private String accessToken;

        @Nullable
        private Actor actor;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        @Nullable
        public Actor getActor() {
            return actor;
        }

        public void setActor(@Nullable Actor actor) {
            this.actor = actor;
        }

    }

    public static class Group {

        private Integer id;
        private String accessToken;

        @Nullable
        private Actor actor;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        @Nullable
        public Actor getActor() {
            return actor;
        }

        public void setActor(@Nullable Actor actor) {
            this.actor = actor;
        }

    }

    private User user;
    private Group group;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
