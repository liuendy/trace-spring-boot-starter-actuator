package org.springframework.boot.actuate.trace;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

/**
 * Created by wonwoo on 2017-07-03.
 */
public class MongoTraceRepositoryTests {

  private MongoTraceRepository repository;
  private MongoOperations mongoOperations;

  @Before
  public void setup() {
    mongoOperations = mock(MongoOperations.class);
    repository = new MongoTraceRepository(mongoOperations);
  }

  @Test
  public void findAllTest() {
    Date timestamp = new Date();
    Map<String,Object> info = new HashMap<String, Object>();
    info.put("request", "foo");
    info.put("response", "bar");

    given(mongoOperations.findAll(Mockito.<Class<Trace>>any()))
        .willReturn(Collections.singletonList(new Trace(timestamp, info)));
    List<Trace> traces = repository.findAll();
    assertThat(traces).hasSize(1);
    assertThat(traces.get(0).getInfo()).isEqualTo(info);
    assertThat(traces.get(0).getTimestamp()).isEqualTo(timestamp);

    then(mongoOperations).should(atLeastOnce()).findAll(Trace.class);
  }

  @Test
  public void addTest() {
    doNothing().when(mongoOperations).save(Matchers.any());
    Map<String,Object> info = new HashMap<String, Object>();
    info.put("request", "foo");
    info.put("response", "bar");
    repository.add(info);
    then(mongoOperations).should(atLeastOnce()).save(Matchers.any());
  }
}