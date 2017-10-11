package vkanalizer;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import vkanalizer.application.Application;
import vkanalizer.vk.config.VkClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Created by Nikolay V. Petrov on 02.09.2017.
 */

@SpringBootApplication
@Import({
        VkClientConfiguration.class,
})
@EnableScheduling
public class VkAnalizer {

    private static Logger log = LoggerFactory.getLogger(VkAnalizer.class);

    public static void main(String... args) {
        ApplicationContext context = Application.start(VkAnalizer.class, args);

    }

}
