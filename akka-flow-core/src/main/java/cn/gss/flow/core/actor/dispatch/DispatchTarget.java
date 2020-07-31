package cn.gss.flow.core.actor.dispatch;

import lombok.Data;

@Data
public class DispatchTarget {
  private int id;
  private TargetType type;


  public enum TargetType {
    FLOW, ACTOR
  }
}