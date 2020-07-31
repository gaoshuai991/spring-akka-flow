package cn.gss.flow.core.flow.service;

import static cn.gss.flow.core.flow.repo.FlowEntity.convert;

import cn.gss.flow.core.flow.Flow;
import cn.gss.flow.core.flow.repo.FlowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by JackieGao on 2020/7/29.
 */
@Service
@RequiredArgsConstructor
public class FlowServiceImpl implements FlowService {
  private final FlowRepository flowRepository;

  @Override
  public Flow read(int id) {
    return flowRepository.read(id).convert();
  }

  @Override
  public int create(Flow flow) {
    return flowRepository.create(convert(flow));
  }

  @Override
  public void update(int id, Flow flow) {
    flowRepository.update(id, convert(flow));
  }

  @Override
  public void delete(int id) {
    flowRepository.delete(id);
  }
}
