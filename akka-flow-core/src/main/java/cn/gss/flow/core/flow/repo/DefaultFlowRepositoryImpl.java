package cn.gss.flow.core.flow.repo;

import cn.gss.flow.core.exception.KeyGenerateException;
import cn.gss.flow.core.flow.FlowProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.util.Optional;

/**
 * Created by JackieGao on 2020/7/28.
 */
@RequiredArgsConstructor
public class DefaultFlowRepositoryImpl implements FlowRepository {
  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final FlowProperties flowProperties;

  @Override
  public FlowEntity read(int id) {
    String sql = String.format(
        "select id, name, entrance from %s where id = :id",
        flowProperties.getTableName()
    );
    return jdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", id), FlowEntity.class);
  }

  @Override
  public int create(FlowEntity flowEntity) throws DataAccessException {
    String sql = String.format(
        "insert into %s (name, entrance) values (?, ?)",
        flowProperties.getTableName()
    );
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("name", flowEntity.getName());
    sqlParameterSource.addValue("entrance", flowEntity.getEntrance());
    GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(sql, sqlParameterSource, generatedKeyHolder);
    return Optional.ofNullable(generatedKeyHolder.getKey())
        .map(Number::intValue)
        .orElseThrow(KeyGenerateException::new);
  }

  @Override
  public void update(int id, FlowEntity flowEntity) {
    StringBuilder sql = new StringBuilder(String.format("update %s set name = ?", flowProperties.getTableName()));
    MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
    sqlParameterSource.addValue("id", id);
    sqlParameterSource.addValue("name", flowEntity.getName());
    if (flowEntity.getEntrance() != null) {
      sqlParameterSource.addValue("entrance", flowEntity.getEntrance());
      sql.append(", entrance = ?");
    }
    sql.append(" where id = ?");
    jdbcTemplate.update(sql.toString(), sqlParameterSource);
  }

  @Override
  public void delete(int id) {
    String sql = String.format("delete from %s where id = ?", flowProperties.getTableName());
    jdbcTemplate.update(sql, new MapSqlParameterSource("id", id));
  }
}
