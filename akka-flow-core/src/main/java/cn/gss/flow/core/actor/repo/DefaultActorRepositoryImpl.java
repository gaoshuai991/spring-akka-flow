package cn.gss.flow.core.actor.repo;

import cn.gss.flow.core.actor.ActorProperties;
import cn.gss.flow.core.exception.KeyGenerateException;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.Optional;

/**
 * Created by JackieGao on 2020/7/28.
 */
@RequiredArgsConstructor
public class DefaultActorRepositoryImpl implements ActorRepository {
  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final ActorProperties actorProperties;

  @Override
  public ActorEntity read(int id) {
    String sql = String.format(
        "select id, name, type, config, dispatch from %s where id = :id",
        actorProperties.getTableName()
    );
    return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", id), ActorEntity.class);
  }

  @Override
  public int create(ActorEntity actorEntity) {
    String sql = String.format(
        "insert into %s (name, type, config, dispatch) values (?, ?, ?, ?)",
        actorProperties.getTableName()
    );
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("name", actorEntity.getName());
    sqlParameterSource.addValue("type", actorEntity.getType());
    sqlParameterSource.addValue("config", actorEntity.getConfig());
    sqlParameterSource.addValue("dispatch", actorEntity.getDispatch());
    GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(sql, sqlParameterSource, generatedKeyHolder);
    return Optional.ofNullable(generatedKeyHolder.getKey())
        .map(Number::intValue)
        .orElseThrow(KeyGenerateException::new);
  }

  @Override
  public void update(int id, ActorEntity actorEntity) {
    StringBuilder sql = new StringBuilder(String.format("update %s set name = ?", actorProperties.getTableName()));
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("id", id);
    sqlParameterSource.addValue("name", actorEntity.getName());
    if (actorEntity.getType() != null) {
      sqlParameterSource.addValue("type", actorEntity.getType());
      sql.append(", type = ?");
    }
    if (actorEntity.getConfig() != null) {
      sqlParameterSource.addValue("config", actorEntity.getConfig());
      sql.append(", config = ?");
    }
    if (actorEntity.getDispatch() != null) {
      sqlParameterSource.addValue("dispatch", actorEntity.getDispatch());
      sql.append(", dispatch = ?");
    }
    sql.append(" where id = ?");
    jdbcTemplate.update(sql.toString(), sqlParameterSource);
  }

  @Override
  public void delete(int id) {
    String sql = String.format("delete from %s where id = ?", actorProperties.getTableName());
    jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
  }
}
