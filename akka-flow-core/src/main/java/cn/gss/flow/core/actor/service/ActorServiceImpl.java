package cn.gss.flow.core.actor.service;

import static cn.gss.flow.core.actor.repo.ActorEntity.convert;

import cn.gss.flow.core.actor.Actor;
import cn.gss.flow.core.actor.repo.ActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by JackieGao on 2020/7/28.
 */
@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService {
  private final ActorRepository actorRepository;
  @Override
  public Actor read(int id) {
    return actorRepository.read(id).convert();
  }

  @Override
  public int create(Actor actor) {
    return actorRepository.create(convert(actor));
  }

  @Override
  public void update(int id, Actor actor) {
    actorRepository.update(id, convert(actor));
  }

  @Override
  public void delete(int id) {
    actorRepository.delete(id);
  }
}
