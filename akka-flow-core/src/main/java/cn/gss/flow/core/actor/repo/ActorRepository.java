package cn.gss.flow.core.actor.repo;

/**
 * Created by JackieGao on 2020/7/28.
 */
public interface ActorRepository {
    ActorEntity read(int id);

    /**
     * @return Generated key
     */
    int create(ActorEntity actorEntity);

    void update(int id, ActorEntity actorEntity);

    void delete(int id);
}
