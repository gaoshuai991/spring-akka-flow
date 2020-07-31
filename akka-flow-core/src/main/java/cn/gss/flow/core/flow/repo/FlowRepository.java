package cn.gss.flow.core.flow.repo;

/**
 * Created by JackieGao on 2020/7/28.
 */
public interface FlowRepository {
    FlowEntity read(int id);

    /**
     * @return Generated key
     */
    int create(FlowEntity flowEntity);

    void update(int id, FlowEntity flowEntity);

    void delete(int id);
}
