package starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.createHttpServer()
        .requestHandler(req -> req.response().end("Hello Vert.x!"))
        .listen(8080);

      Future<String> hello = Future.future(h -> {
        System.out.println("Call external API");
        h.complete("Call external API");
      });


      cacheFirst("key", hello)
      .onComplete(h -> System.out.println("Success"))
      .onFailure(t -> System.out.println("Failed"));
  }

  private Future<String> cacheFirst(String key, Future<String> task) {
    Future<String> getCache = Future.future(h -> {
      h.complete("ABCXYZ");
    });

    return getCache.onSuccess(res -> {
      System.out.println("Data from cache: " + res);
    }).onFailure(t -> {
        task.onSuccess(res -> {
          System.out.println("Store task result to cache");
        }).onFailure(tt -> {
          System.out.println("Do nothing");
        });
    });
  }

}
