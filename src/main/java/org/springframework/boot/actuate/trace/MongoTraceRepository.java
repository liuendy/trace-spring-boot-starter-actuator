package org.springframework.boot.actuate.trace;

import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wonwoolee on 2017. 7. 2..
 */
public class MongoTraceRepository implements TraceRepository {

  private final MongoOperations mongoOperations;

  public MongoTraceRepository(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public List<Trace> findAll() {
    return mongoOperations.findAll(Trace.class);
  }

  @Override
  public void add(Map<String, Object> traceInfo) {
    mongoOperations.save(new Trace(new Date(), traceInfo));
  }
}
