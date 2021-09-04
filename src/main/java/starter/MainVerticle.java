package starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import java.util.function.Supplier;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer().requestHandler(req -> req.response().end("Hello Vert.x!")).listen(8080);

    cacheFirst("key", () -> Future.future(h -> {
      System.out.println("Call external API");
      h.complete("Call external API");
    }))
    .onComplete(h -> System.out.println("Success"))
    .onFailure(t -> System.out.println("Failed"));
  }

  private Future<String> cacheFirst(String key, Supplier<Future<String>> task) {
    Future<String> getCache = Future.future(h -> {
      h.complete("ABCXYZ");
    });

    return getCache.onSuccess(res -> {
      System.out.println("Data from cache: " + res);
    }).onFailure(t -> {
      task.get().onSuccess(res -> {
        System.out.println("Store task result to cache");
      }).onFailure(tt -> {
        System.out.println("Do nothing");
      });
    });
  }

}
