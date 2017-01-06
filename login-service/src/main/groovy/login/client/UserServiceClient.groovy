package login.client

import groovyx.net.http.RESTClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import static groovyx.net.http.ContentType.JSON

@Component
class UserServiceClient {

  @Value('${services.user.host}')
  String host

  @Value('${services.user.port}')
  String port

  @Value('${services.user.endpoints.findUser}')
  String findUserEndpoint

  Map findUser(String username) {
    def baseUri = "http://$host:$port"
    def path = findUserEndpoint.replace('{username}', username)

    def client = new RESTClient(baseUri)
    client.handler.failure = { resp -> throw new RuntimeException("User not found") }

    def result = client.get(
        path: path,
        contentType: JSON,
        headers: ['Content-Type': JSON]
    )

    assert result.status == 200

    result.data
  }
}
