package cn.gss.flow.core;

import cn.gss.flow.core.flow.Flow;
import cn.gss.flow.core.util.Copyable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by JackieGao on 2020/7/10.
 */
@Getter
@ToString
public abstract class FlowContext<C extends FlowContext<C, D>, D extends Copyable<D>> {
  protected Flow flow;
  protected D data;
  private List<ActorStage> stages;
  private Map<String, String> mdc;
  private Map<String, Object> parameters;

  public C addStage(ActorStage stage) {
    getStages().add(0, stage);
    return getThis();
  }

  public Object getParameter(String key) {
    return Optional.ofNullable(parameters).orElseGet(() -> parameters = new HashMap<>()).get(key);
  }

  public void putParameter(String key, Object value) {
    Optional.ofNullable(parameters).orElseGet(() -> parameters = new HashMap<>()).put(key, value);
  }

  @JsonIgnore
  public ActorStage getCurrentStage() {
    return getStage(0);
  }

  @JsonIgnore
  public ActorStage getPreviousStage() {
    return getStage(1);
  }

  @Nullable
  public ActorStage getStage(int index) {
    try {
      return getStages().get(index);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  public List<ActorStage> getStages() {
    return Optional.ofNullable(stages).orElseGet(() -> stages = new LinkedList<>());
  }

  public C setStages(final List<ActorStage> stages) {
    this.stages = stages;
    return getThis();
  }

  public C setParameters(final Map<String, Object> parameters) {
    this.parameters = parameters;
    return getThis();
  }

  public C setData(D data) {
    this.data = data;
    return getThis();
  }

  public C setFlow(final Flow flow) {
    this.flow = flow;
    return getThis();
  }

  public C setMdc(final Map<String, String> mdc) {
    this.mdc = mdc;
    return getThis();
  }

  protected C getThis() {
    return (C) this;
  }

  protected C copy(FlowContext<C, D> c) {
    return c.setFlow(flow)
        .setParameters(Optional.ofNullable(parameters).map(HashMap::new).orElse(null))
        .setStages(Optional.ofNullable(getStages()).map(LinkedList::new).orElse(null))
        .setMdc(Optional.ofNullable(mdc).map(HashMap::new).orElse(null))
        .getThis();

  }

  public abstract C copy();

}
