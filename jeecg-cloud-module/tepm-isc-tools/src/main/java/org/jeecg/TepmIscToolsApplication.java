
package org.jeecg;

import org.jeecg.modules.common.WebToolUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author zlf
 */
@SpringBootApplication(scanBasePackages = "org.jeecg")
public class TepmIscToolsApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(TepmIscToolsApplication.class, args);
        String ip = WebToolUtils.getLocalIP();
        System.out.println("IP is " + ip);
    }
}
