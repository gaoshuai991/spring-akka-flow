package cn.gss.flow.core;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Terminated;
import akka.routing.ActorRefRoutee;
import akka.routing.BalancingRoutingLogic;
import akka.routing.ConsistentHashingRoutingLogic;
import akka.routing.RandomRoutingLogic;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import akka.routing.RoutingLogic;
import cn.gss.flow.core.actor.ActorProperties;
import cn.gss.flow.core.support.ActorExtension;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

/**
 * Created by JackieGao on 2020/7/10.
 */
@Slf4j
public abstract class AbstractRouter extends BaseActor implements DisposableBean, ApplicationContextAware {

  private ApplicationContext applicationContext;

  private Router router;
  private final Class<? extends AbstractActor> routeeClass;
  private final ActorProperties actorProperties;
  private final RoutingType routingType;

  private long lastMsgTime;
  private Cancellable deathScheduler;

  public AbstractRouter(
      final Class<? extends AbstractActor> routeeClass,
      final ActorProperties actorProperties,
      final RoutingType routingType
  ) {
    this.routeeClass = routeeClass;
    this.actorProperties = actorProperties;
    this.routingType = routingType;
  }

  @Override
  public void setApplicationContext(final ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @PostConstruct
  private void initRoutees() {
    registerRouteeBeanDefinition();
    final List<Routee> routees = IntStream
        .range(0, actorProperties.getNrOfRouteeInstance())
        .mapToObj(n -> {
          ActorRef actorRef = context().actorOf(ActorExtension.createProps(routeeBeanName()));
          context().watch(actorRef);
          return new ActorRefRoutee(actorRef);
        })
        .map(Routee.class::cast)
        .collect(Collectors.toList());
    RoutingLogic routingLogic;
    switch (routingType) {
      case RANDOM:
        routingLogic = new RandomRoutingLogic();
        break;
      case ROUND_ROBIN:
        routingLogic = new RoundRobinRoutingLogic();
        break;
      case CONSISTENT_HASHING:
        routingLogic = new ConsistentHashingRoutingLogic(context().system());
        break;
      case BALANCING:
      default:
        routingLogic = new BalancingRoutingLogic();
        break;
    }
    router = new Router(routingLogic, routees);
  }

  @Override
  public void preStart() throws Exception {
    super.preStart();
    log.info("{} has started ...", self());
  }

  @Override
  public void postStop() throws Exception {
    Optional.ofNullable(deathScheduler).ifPresent(Cancellable::cancel);
    super.postStop();
    log.info("{} has stopped ...", self());
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(FlowContext.class, flowContext -> {
          lastMsgTime = System.currentTimeMillis();
          router.route(flowContext, sender());
        })
        .match(Terminated.class, message -> {
          router = router.removeRoutee(message.actor());
          ActorRef actorRef = context().actorOf(ActorExtension.createProps(routeeBeanName()));
          context().watch(actorRef);
          router = router.addRoutee(new ActorRefRoutee(actorRef));
        })
        .matchAny(m -> log.error("Unknown message: {}", m))
        .build();
  }

  @Override
  public void destroy() {
    startDeathCountdown();
  }

  private void startDeathCountdown() {
    deathScheduler = context().system().scheduler().scheduleAtFixedRate(
        Duration.ZERO,
        Duration.ofSeconds(5L),
        () -> {
          if (System.currentTimeMillis() - lastMsgTime >= actorProperties.getIdleTimeToDeath()) {
            context().stop(self());
          }
        },
        context().system().dispatcher()
    );
  }

  private void registerRouteeBeanDefinition() {
    final DefaultListableBeanFactory beanFactory =
        (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    if (!beanFactory.containsBeanDefinition(routeeBeanName())) {
      beanFactory.registerBeanDefinition(
          routeeBeanName(),
          BeanDefinitionBuilder.genericBeanDefinition(routeeClass).setScope(SCOPE_PROTOTYPE).getBeanDefinition()
      );
    }
  }

  private String routeeBeanName() {
    return StringUtils.uncapitalize(routeeClass.getSimpleName());
  }

}