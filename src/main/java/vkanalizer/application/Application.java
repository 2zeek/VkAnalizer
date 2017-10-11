package vkanalizer.application;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public final class Application {

    private static final String CONFIG_DIRECTORY = "config.directory";

    public static ApplicationContext start(@NotNull Class<?> mainClass, String[] args) {

        List<Class<?>> beans = new ArrayList<>();
        beans.add(ApplicationProperties.class);
        beans.add(mainClass);

        SpringApplicationBuilder builder = new SpringApplicationBuilder();
        builder.sources(beans.stream().toArray(Class<?>[]::new));
        ConfigurableApplicationContext createdContext = builder.run(args);
        return createdContext;
    }

    static String getConfigDirectory() {
        return System.getProperty(Application.CONFIG_DIRECTORY);
    }
}
