package cn.gss.flow.core.config;

import akka.actor.ActorSystem;
import cn.gss.flow.core.AbstractActor;
import cn.gss.flow.core.AbstractRouter;
import cn.gss.flow.core.RoutingType;
import cn.gss.flow.core.actor.ActorProperties;
import cn.gss.flow.core.actor.repo.ActorRepository;
import cn.gss.flow.core.actor.repo.DefaultActorRepositoryImpl;
import cn.gss.flow.core.flow.FlowProperties;
import cn.gss.flow.core.flow.repo.DefaultFlowRepositoryImpl;
import cn.gss.flow.core.flow.repo.FlowRepository;
import cn.gss.flow.core.support.ActorDefine;
import cn.gss.flow.core.support.ActorExtension;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.util.StringUtils;

/**
 * Created by JackieGao on 2020/7/28.
 */
@Configuration
@ComponentScan()
@EnableConfigurationProperties(ActorProperties.class)
public class AkkaFlowAutoConfiguration {
  @Bean
  public ActorSystem actorSystem(ApplicationContext applicationContext, ActorProperties actorProperties) {
    ActorSystem actorSystem = ActorSystem.create("akka-flow-actor-system");
    ActorExtension.initialize(applicationContext, actorSystem);
    initializeActor(applicationContext, actorProperties);
    return actorSystem;
  }


  private void initializeActor(ApplicationContext applicationContext, ActorProperties actorProperties) {
    if (StringUtils.isEmpty(actorProperties.getPackageName())) {
      return;
    }
    final DefaultListableBeanFactory beanFactory =
        (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    final Reflections reflections = new Reflections(
        new ConfigurationBuilder()
            .filterInputsBy(new FilterBuilder().includePackage(actorProperties.getPackageName()))
            .addUrls(ClasspathHelper.forPackage(actorProperties.getPackageName()))
    );
    for (final Class<? extends AbstractActor> clazz : reflections.getSubTypesOf(AbstractActor.class)) {
      final ActorDefine routerAnnotation = clazz.getAnnotation(ActorDefine.class);
      if (routerAnnotation != null) {
        final String actorName = routerAnnotation.actorName();
        final RoutingType routingType = routerAnnotation.routingType();
        beanFactory.registerBeanDefinition(
            actorName,
            BeanDefinitionBuilder
                .genericBeanDefinition(
                    AbstractRouter.class,
                    () -> new AbstractRouter(clazz, actorProperties, routingType) {
                    }
                ).getBeanDefinition()
        );
        ActorExtension.createActor(actorName, actorName);
      }
    }
  }

  @ConditionalOnMissingBean(FlowRepository.class)
  public static class FlowRepoConfiguration {
    @Bean
    public FlowRepository flowRepository(NamedParameterJdbcTemplate jdbcTemplate, FlowProperties flowProperties) {
      return new DefaultFlowRepositoryImpl(jdbcTemplate, flowProperties);
    }
  }


  @ConditionalOnMissingBean(ActorRepository.class)
  public static class ActorRepoConfiguration {
    @Bean
    public ActorRepository actorRepository(NamedParameterJdbcTemplate jdbcTemplate, ActorProperties actorProperties) {
      return new DefaultActorRepositoryImpl(jdbcTemplate, actorProperties);
    }
  }
}
