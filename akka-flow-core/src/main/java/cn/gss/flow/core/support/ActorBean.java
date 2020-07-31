package cn.gss.flow.core.support;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * <p>A generic annotation to mark custom actor as a managed spring bean</p>
 * Created by JackieGao on 2020/7/13.
 */
@Retention(RUNTIME)
@Target(ElementType.TYPE)
@Component
@Scope(SCOPE_PROTOTYPE)
public @interface ActorBean {
  /**
   * Actor's name, decided by ActorSystem if not specified.
   */
  String actorName() default "";

  /**
   * If load specified actor when ActorSystem loading complete.
   *
   * @return default is false
   */
  boolean initializeOnStart() default false;
}

