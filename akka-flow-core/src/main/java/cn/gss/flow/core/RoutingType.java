package cn.gss.flow.core;

/**
 * Created by JackieGao on 2020/7/13.
 */
public enum RoutingType {
  /**
   * {@link akka.routing.RoundRobinRoutingLogic}
   */
  ROUND_ROBIN,
  /**
   * {@link akka.routing.ConsistentHashingRoutingLogic}
   */
  CONSISTENT_HASHING,
  /**
   * {@link akka.routing.RandomRoutingLogic}
   */
  RANDOM,
  /**
   * {@link akka.routing.BalancingRoutingLogic}
   */
  BALANCING
}
