package agroscience.fields;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Component
public class HttpSteps {

  @Autowired
  private TestRestTemplate testRestTemplate;
  private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3Mjk0OTQzNTQsImV4cCI6MTcyOTQ5Nzk1NCwic3ViIjoiYzUzNmQyODEtNTM5MC00YzViLWIxYTMtNjIwNjEzOWZjZTFlIiwicm9sZSI6Im9yZ2FuaXphdGlvbiIsImVtYWlsIjoidGVzdEB0ZXN0LnJ1Iiwib3JnIjoiYzUzNmQyODEtNTM5MC00YzViLWIxYTMtNjIwNjEzOWZjZTFlIn0.0pXuoEo35KiJVYStWFz7cM2UMSbwe19vyZNtIoLDpHs";

  public  <T> ResponseEntity<T> sendPostRequest(Object entityDTO, String url, Class<T> responseType){
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(TOKEN);
    HttpEntity<Object> request = new HttpEntity<>(entityDTO, headers);
    return testRestTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            responseType
    );
  }

  public  <T> ResponseEntity<List<T>> sendGetRequest(String url, ParameterizedTypeReference<List<T>> responseType) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(TOKEN);
    HttpEntity<Object> request = new HttpEntity<>(headers);

    return testRestTemplate.exchange(
            url,
            HttpMethod.GET,
            request,
            responseType
    );

  }

  public  ResponseEntity<Void> sendDeleteRequest(UUID soilCompositionId, String url, String queryParamName) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(TOKEN);
    HttpEntity<Object> request = new HttpEntity<>(headers);
    String urlWithParams = UriComponentsBuilder.fromPath(url)
            .queryParam(queryParamName, soilCompositionId)
            .toUriString();
    return testRestTemplate.exchange(
            urlWithParams,
            HttpMethod.DELETE,
            request,
            Void.class
    );
  }

  public  ResponseEntity<Void> sendPutRequest(UUID soilCompositionId, Object entityDTO, String url, String queryParamName) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth(TOKEN);
    HttpEntity<Object> request = new HttpEntity<>(entityDTO, headers);
    String urlWithParams = UriComponentsBuilder.fromPath(url)
            .queryParam(queryParamName, soilCompositionId)
            .toUriString();
    return testRestTemplate.exchange(
            urlWithParams,
            HttpMethod.PUT,
            request,
            Void.class
    );
  }

}
