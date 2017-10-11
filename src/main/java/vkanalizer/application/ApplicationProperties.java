package vkanalizer.application;


import com.google.common.base.MoreObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;

@PropertySource(value= "file:${config.directory}/app.properties")
@ConfigurationProperties(prefix = "vkanalizer/application")
@Configuration
public class ApplicationProperties {

    private static Logger log = LoggerFactory.getLogger(ApplicationProperties.class);

    private String version;
    private String name;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigDirectory() {
        return Application.getConfigDirectory();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", getName())
                .add("version", getVersion())
                .toString();
    }

    @PostConstruct
    private void postConstruct() {
        log.info("Application: " +
                        "name={}, " +
                        "version={}",
                getName(), getVersion());
    }
}
