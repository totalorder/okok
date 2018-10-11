package se.deadlock.okok;

import java.net.URL;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.experimental.Delegate;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@AllArgsConstructor
public class Request {
  @Delegate
  private final okhttp3.Request okRequest;

  public okhttp3.Request unwrap() {
    return okRequest;
  }

  public static class Builder {
    private okhttp3.Request.Builder okBuilder;

    @Nullable
    private final String baseUrl;

    public Builder(@Nullable final String baseUrl) {
      this.okBuilder = new okhttp3.Request.Builder();
      this.baseUrl = baseUrl;
    }

    public Builder url(String url) {
      if (baseUrl != null) {
        okBuilder = okBuilder.url(baseUrl + url);
      } else {
        okBuilder = okBuilder.url(url);
      }
      return this;
    }

    public Builder url(HttpUrl url) {
      okBuilder = okBuilder.url(url);
      return this;
    }

    public Builder url(URL url) {
      okBuilder = okBuilder.url(url);
      return this;
    }

    public Builder header(String name, String value) {
      okBuilder = okBuilder.header(name, value);
      return this;
    }

    public Builder addHeader(String name, String value) {
      okBuilder = okBuilder.addHeader(name, value);
      return this;
    }

    public Builder removeHeader(String name) {
      okBuilder = okBuilder.removeHeader(name);
      return this;
    }

    public Builder headers(Headers headers) {
      okBuilder = okBuilder.headers(headers);
      return this;
    }

    public Builder cacheControl(CacheControl cacheControl) {
      okBuilder = okBuilder.cacheControl(cacheControl);
      return this;
    }

    public Builder get() {
      okBuilder = okBuilder.get();
      return this;
    }

    public Builder head() {
      okBuilder = okBuilder.head();
      return this;
    }

    public Builder post(RequestBody body) {
      okBuilder = okBuilder.post(body);
      return this;
    }

    public Builder delete(@Nullable RequestBody body) {
      okBuilder = okBuilder.delete(body);
      return this;
    }

    public Builder delete() {
      okBuilder = okBuilder.delete();
      return this;
    }

    public Builder put(RequestBody body) {
      okBuilder = okBuilder.post(body);
      return this;
    }

    public Builder patch(RequestBody body) {
      okBuilder = okBuilder.patch(body);
      return this;
    }

    public Builder method(String method, @Nullable RequestBody body) {
      okBuilder = okBuilder.method(method, body);
      return this;
    }

    public Builder tag(@Nullable Object tag) {
      okBuilder = okBuilder.tag(tag);
      return this;
    }

    public <T> Builder tag(Class<? super T> type, @Nullable T tag) {
      okBuilder = okBuilder.tag(type, tag);
      return this;
    }

    public Request build() {
      return new Request(okBuilder.build());
    }

    // Add-ons
    public Builder post(String body) {
      return post(body, null);
    }

    public Builder delete(String body, @Nullable String contentType) {
      okBuilder = okBuilder.delete(RequestBody.create(contentType != null ? MediaType.parse(contentType) : null, body));
      return this;
    }

    public Builder delete(String body) {
      return delete(body, null);
    }


    public Builder post(String body, @Nullable String contentType) {
      okBuilder = okBuilder.post(RequestBody.create(contentType != null ? MediaType.parse(contentType) : null, body));
      return this;
    }

    public Builder put(String body) {
      return put(body, null);
    }

    public Builder put(String body, @Nullable String contentType) {
      okBuilder = okBuilder.put(RequestBody.create(contentType != null ? MediaType.parse(contentType) : null, body));
      return this;
    }

    public Builder patch(String body) {
      return patch(body, null);
    }

    public Builder patch(String body, @Nullable String contentType) {
      okBuilder = okBuilder.patch(RequestBody.create(contentType != null ? MediaType.parse(contentType) : null, body));
      return this;
    }

    public Builder method(String method, String body) {
      return method(method, body, null);
    }

    public Builder method(String method, String body, @Nullable String contentType) {
      okBuilder = okBuilder.put(RequestBody.create(contentType != null ? MediaType.parse(contentType) : null, body));
      return this;
    }

    public okhttp3.Request.Builder okBuilder() {
      return okBuilder;
    }
  }
}
