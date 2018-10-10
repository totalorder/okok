package se.deadlock.okok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Getter(AccessLevel.NONE)
public class HttpBinGetResponse {
  public final Map<String, String> args;
}
