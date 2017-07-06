package org.springframework.boot.actuate.trace;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;

/**
 * Created by wonwoolee on 2017. 7. 2..
 */
@ConfigurationProperties(prefix = "trace")
public class MongoTraceRepository extends AbstractTraceRepository implements TraceRepository {

  private final MongoOperations mongoOperations;

  public MongoTraceRepository(MongoOperations mongoOperations) {
    this.mongoOperations = mongoOperations;
  }

  @Override
  public List<Trace> findAll(HttpServletRequest request, Pageable pageable) {
    Query query = new Query().with(pageable);
    return mongoOperations.find(query, Trace.class);
  }

  @Override
  public void add(Trace trace) {
    mongoOperations.save(trace);
  }

}
