package io.vertx.servicediscovery.polyglot;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.EventBus;
import io.vertx.reactivex.core.eventbus.MessageConsumer;
import io.vertx.reactivex.core.http.HttpClient;
import io.vertx.reactivex.ext.jdbc.JDBCClient;
import io.vertx.reactivex.ext.mongo.MongoClient;
import io.vertx.reactivex.ext.web.client.WebClient;
import io.vertx.reactivex.redis.RedisClient;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.reactivex.servicediscovery.ServiceReference;
import io.vertx.reactivex.servicediscovery.service.HelloService;
import io.vertx.reactivex.servicediscovery.types.*;

/**
 * @author <a href="http://escoffier.me">Clement Escoffier</a>
 */
public class MyRX2Verticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx);
    EventBus eb = vertx.eventBus();

    eb.consumer("http-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-http-service"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            HttpClient client = reference.getAs(HttpClient.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }
      }));

    eb.consumer("http-sugar", message -> {
      JsonObject result = new JsonObject();
      HttpEndpoint.getClient(discovery, record -> record.getName().equalsIgnoreCase("my-http-service"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            HttpClient client = ar.result();
            result.put("client", client.getClass().toString());
            ServiceDiscovery.releaseServiceObject(discovery, client);
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        });
    });

    eb.consumer("web-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-http-service"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            WebClient client = reference.getAs(WebClient.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }
      }));

    eb.consumer("web-sugar", message -> {
      JsonObject result = new JsonObject();
      HttpEndpoint.getWebClient(discovery, record -> record.getName().equalsIgnoreCase("my-http-service"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            WebClient client = ar.result();
            result.put("client", client.getClass().toString());
            ServiceDiscovery.releaseServiceObject(discovery, client);
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        });
    });

    eb.consumer("service-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-service"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            HelloService client = reference.getAs(HelloService.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }
      }));

    eb.consumer("service-sugar", message -> {
      JsonObject result = new JsonObject();
      EventBusService.getServiceProxy(discovery,
        record -> record.getName().equalsIgnoreCase("my-service"),
        HelloService.class,
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            HelloService client = ar.result();
            result.put("client", client.getClass().toString());
            ServiceDiscovery.releaseServiceObject(discovery, client);
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        });
    });

    eb.consumer("ds-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-data-source"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            JDBCClient client = reference.getAs(JDBCClient.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }
      }));

    eb.consumer("ds-sugar", message -> {
      JsonObject result = new JsonObject();
      JDBCDataSource.getJDBCClient(discovery, record -> record.getName().equalsIgnoreCase("my-data-source"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            JDBCClient client = ar.result();
            result.put("client", client.getClass().toString());
            ServiceDiscovery.releaseServiceObject(discovery, client);
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        });
    });

    eb.consumer("redis-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-redis-data-source"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            RedisClient client = reference.getAs(RedisClient.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }
      }));

    eb.consumer("redis-sugar", message -> {
      JsonObject result = new JsonObject();
      RedisDataSource.getRedisClient(discovery, record -> record.getName().equalsIgnoreCase("my-redis-data-source"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            RedisClient client = ar.result();
            result.put("client", client.getClass().toString());
            ServiceDiscovery.releaseServiceObject(discovery, client);
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        });
    });

    eb.consumer("mongo-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-mongo-data-source"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            MongoClient client = reference.getAs(MongoClient.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }
      }));

    eb.consumer("mongo-sugar", message -> {
      JsonObject result = new JsonObject();
      MongoDataSource.getMongoClient(discovery,
        record -> record.getName().equalsIgnoreCase("my-mongo-data-source"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            MongoClient client = ar.result();
            result.put("client", client.getClass().toString());
            ServiceDiscovery.releaseServiceObject(discovery, client);
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        });
    });

    eb.consumer("source1-ref", message ->
      discovery.getRecord(rec -> rec.getName().equalsIgnoreCase("my-message-source-1"), ar -> {
        if (ar.failed()) {
          message.reply("FAIL - No service");
        } else {
          JsonObject result = new JsonObject();
          ServiceReference reference = discovery.getReference(ar.result());
          if (reference == null) {
            message.reply("FAIL - reference is null");
          } else {
            MessageConsumer<String> client = reference.getAs(MessageConsumer.class);
            result.put("client", client.getClass().toString());
            reference.release();
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        }
      }));

    eb.consumer("source1-sugar", message -> {
      JsonObject result = new JsonObject();
      MessageSource.getConsumer(discovery, record -> record.getName().equalsIgnoreCase("my-message-source-1"),
        ar -> {
          if (ar.failed()) {
            message.reply("FAIL - no service");
          } else {
            MessageConsumer client = ar.result();
            result.put("client", client.getClass().toString());
            ServiceDiscovery.releaseServiceObject(discovery, client);
            result.put("bindings", getBindings(discovery));
            message.reply(result);
          }
        });
    });
  }

  private synchronized JsonArray getBindings(ServiceDiscovery discovery) {
    JsonArray array = new JsonArray();
    for (ServiceReference ref : discovery.bindings()) {
      array.add(ref.toString());
    }
    return array;
  }
}
