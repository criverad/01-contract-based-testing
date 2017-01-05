package login.client

import org.springframework.stereotype.Component

@Component
class UserServiceClient {
  Map findUser(String username) {
    return [
        message: "User: $username".toString()
    ]
  }
}
