package login.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class UserServiceClient {

  @Value('${services.user.host}')
  String host

  @Value('${services.user.port}')
  String port

  @Value('${services.user.endpoints.findUser}')
  String findUserEndpoint

  Map findUser(String username) {
    def endpoint = findUserEndpoint.replace('{username}', username)
    def url = "http://$host:$port$endpoint"
    return [
        message: url.toString()
    ]
  }
}
