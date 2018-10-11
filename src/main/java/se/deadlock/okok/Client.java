package se.deadlock.okok;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import lombok.experimental.Delegate;
import okhttp3.*;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Client {
  @Delegate(excludes = DelegateExcludes.class)
  private final OkHttpClient okClient;

  @Nullable
  private final String baseUrl;
  private final ObjectMapper objectMapper;

  public Client() {
    this(null);
  }

  public Client(final String baseUrl) {
    super();
    this.okClient = new OkHttpClient();
    this.baseUrl = baseUrl;
    this.objectMapper = new ObjectMapper();
  }

  Client(String baseUrl, ObjectMapper objectMapper, OkHttpClient okClient) {
    this.baseUrl = baseUrl;
    this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
    this.okClient = okClient;
  }

  public Call newCall(Request request) {
    return new Call(okClient.newCall(request.unwrap()), objectMapper);
  }

  public Response get(final String url) {
    return execute(request().url(url).get().build());
  }

  public Response post(final String url, final String body) {
    return execute(request().url(url).post(body).build());
  }

  public Response put(final String url, final String body) {
    return execute(request().url(url).put(body).build());
  }

  public Response delete(final String url) {
    return execute(request().url(url).delete().build());
  }

  public Response delete(final String url, final String body) {
    return execute(request().url(url).delete(body).build());
  }

  public Response execute(final Request request) {
    if (baseUrl != null && !request.url().toString().startsWith(baseUrl)) {
      throw new RuntimeException("Request url " + request.url().toString() + " does not match baseUrl "
          + baseUrl + ". Consider using a plain OkHttpClient if this is intended.");
    }
    return newCall(request).execute();
  }

  public Request.Builder request(final String url) {
    return request().url(url);
  }

  public Request.Builder request() {
    return new Request.Builder(baseUrl);
  }

  interface DelegateExcludes {
    okhttp3.Call newCall(okhttp3.Request request);
    okhttp3.OkHttpClient.Builder newBuilder();
  }

  public Builder newBuilder() {
    return new Builder(okClient.newBuilder());
  }

  public static class Builder {
    private OkHttpClient.Builder okBuilder;
    private String baseUrl = null;
    private ObjectMapper objectMapper = null;

    Builder(OkHttpClient.Builder okBuilder) {
      okBuilder = okBuilder;
    }

    public Builder() {
      okBuilder = new OkHttpClient.Builder();
    }

    public Builder baseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
      return this;
    }

    public Builder connectTimeout(long timeout, TimeUnit unit) {
      okBuilder = okBuilder.connectTimeout(timeout, unit);
      return this;
    }

    public Builder readTimeout(long timeout, TimeUnit unit) {
      okBuilder = okBuilder.readTimeout(timeout, unit);
      return this;

    }

    public Builder writeTimeout(long timeout, TimeUnit unit) {
      okBuilder = okBuilder.writeTimeout(timeout, unit);
      return this;

    }

    public Builder pingInterval(long interval, TimeUnit unit) {
      okBuilder = okBuilder.pingInterval(interval, unit);
      return this;

    }

    public Builder proxy(@Nullable Proxy proxy) {
      okBuilder = okBuilder.proxy(proxy);
      return this;
    }

    public Builder proxySelector(ProxySelector proxySelector) {
      okBuilder = okBuilder.proxySelector(proxySelector);
      return this;
    }

    public Builder cookieJar(CookieJar cookieJar) {
      okBuilder = okBuilder.cookieJar(cookieJar);
      return this;
    }

    public Builder cache(@Nullable Cache cache) {
      okBuilder = okBuilder.cache(cache);
      return this;
    }

    public Builder dns(Dns dns) {
      okBuilder = okBuilder.dns(dns);
      return this;
    }

    public Builder socketFactory(SocketFactory socketFactory) {
      okBuilder = okBuilder.socketFactory(socketFactory);
      return this;
    }

    public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
      okBuilder = okBuilder.sslSocketFactory(sslSocketFactory);
      return this;
    }

    public Builder sslSocketFactory(
        SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
      okBuilder = okBuilder.sslSocketFactory(sslSocketFactory, trustManager);
      return this;
    }

    public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
      okBuilder = okBuilder.hostnameVerifier(hostnameVerifier);
      return this;
    }

    public Builder certificatePinner(CertificatePinner certificatePinner) {
      okBuilder = okBuilder.certificatePinner(certificatePinner);
      return this;
    }

    public Builder authenticator(Authenticator authenticator) {
      okBuilder = okBuilder.authenticator(authenticator);
      return this;
    }

    public Builder proxyAuthenticator(Authenticator proxyAuthenticator) {
      okBuilder = okBuilder.proxyAuthenticator(proxyAuthenticator);
      return this;
    }

    public Builder connectionPool(ConnectionPool connectionPool) {
      okBuilder = okBuilder.connectionPool(connectionPool);
      return this;
    }

    public Builder followSslRedirects(boolean followProtocolRedirects) {
      okBuilder = okBuilder.followSslRedirects(followProtocolRedirects);
      return this;
    }

    public Builder followRedirects(boolean followRedirects) {
      okBuilder = okBuilder.followRedirects(followRedirects);
      return this;
    }

    public Builder retryOnConnectionFailure(boolean retryOnConnectionFailure) {
      okBuilder = okBuilder.retryOnConnectionFailure(retryOnConnectionFailure);
      return this;
    }

    public Builder dispatcher(Dispatcher dispatcher) {
      okBuilder = okBuilder.dispatcher(dispatcher);
      return this;
    }

    public Builder protocols(List<Protocol> protocols) {
      okBuilder = okBuilder.protocols(protocols);
      return this;
    }

    public Builder connectionSpecs(List<ConnectionSpec> connectionSpecs) {
      okBuilder = okBuilder.connectionSpecs(connectionSpecs);
      return this;
    }

    public List<Interceptor> interceptors() {
      return okBuilder.interceptors();
    }

    public Builder addInterceptor(Interceptor interceptor) {
      okBuilder = okBuilder.addInterceptor(interceptor);
      return this;
    }

    public List<Interceptor> networkInterceptors() {
      return okBuilder.networkInterceptors();
    }

    public Builder addNetworkInterceptor(Interceptor interceptor) {
      okBuilder = okBuilder.addNetworkInterceptor(interceptor);
      return this;
    }

    public Builder eventListener(EventListener eventListener) {
      okBuilder = okBuilder.eventListener(eventListener);
      return this;
    }

    public Builder eventListenerFactory(EventListener.Factory eventListenerFactory) {
      okBuilder = okBuilder.eventListenerFactory(eventListenerFactory);
      return this;
    }

    public Client build() {
      return new Client(baseUrl, objectMapper, okBuilder.build());
    }
  }
}
