package cn.gss.flow.core.support;

import cn.gss.flow.core.RoutingType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by JackieGao on 2020/7/13.
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
public @interface ActorDefine {
  String actorName() default "";

  RoutingType routingType() default RoutingType.BALANCING;
}
