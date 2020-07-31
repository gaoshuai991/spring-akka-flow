package cn.gss.flow.core.actor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by JackieGao on 2020/7/13.
 */
@Data
@ConfigurationProperties("akka-flow.actor")
public class ActorProperties {
    private String tableName = "actor";
    private Long idleTimeToDeath = 30000L;
    private Integer nrOfRouteeInstance = 3;
    private String packageName;
}
