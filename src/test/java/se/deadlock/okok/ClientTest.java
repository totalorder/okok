package se.deadlock.okok;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ClientTest {
  Client client = new Client("http://httpbin.org");

  @Test
  void testGet() {
    Response response = client.get("/get");
    assertThat(response.code(), is(200));
  }

  @Test
  void testPost() {
    Response response = client.post("/post", "post-body");
    assertThat(response.code(), is(200));
    assertThat(response.json().get("data").textValue(), is("post-body"));
  }

  @Test
  void testPut() {
    Response response = client.put("/put", "put-body");
    assertThat(response.code(), is(200));
    assertThat(response.json().get("data").textValue(), is("put-body"));
  }

  @Test
  void testDelete() {
    Response response = client.delete("/delete");
    assertThat(response.code(), is(200));
  }

  @Test
  void testGetJson() {
    Response response = client.get("/get?q=value");
    Map<String, String> expectedParams = new HashMap<>();
    expectedParams.put("q", "value");
    assertThat(response.code(), is(200));
    assertThat(
        response.json(HttpBinGetResponse.class),
        is(new HttpBinGetResponse(expectedParams)));
  }

  @Test
  void testPostContentType() {
    Response response = client.execute(
        client.request("/post").post("{}", "application/json").build());
    assertThat(response.code(), is(200));
    assertThat(
        response.json().get("headers").get("Content-Type").textValue(),
        is("application/json; charset=utf-8"));
  }

  @Test
  void testPlainOkHttp() {
    okhttp3.Request okRequest = client.request("/get").okBuilder()
        .build();

    okhttp3.Response response = client.execute(new Request(okRequest)).okResponse();
    assertThat(response.code(), is(200));
  }

  @Test
  void testInterceptorLogger() {
    Client interceptedClient = new Client.Builder()
        .baseUrl("http://httpbin.org")
        .addInterceptor(chain -> {
      final okhttp3.Response response = chain.proceed(chain.request());
      return response.newBuilder().addHeader("X-Interceptor", "T-1000").build();
    }).build();

    Response response = interceptedClient.get("/get");
    assertThat(response.code(), is(200));
    assertThat(response.header("X-Interceptor"), is("T-1000"));
  }
}
