package cn.gss.flow.core.flow;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by JackieGao on 2020/7/28.
 */
@Data
@ConfigurationProperties("akka-flow.flow")
public class FlowProperties {
  private String tableName = "flow";
}
