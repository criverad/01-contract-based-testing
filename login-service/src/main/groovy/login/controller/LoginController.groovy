package login.controller

import login.client.UserServiceClient
import login.model.Credentials
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

import static org.springframework.web.bind.annotation.RequestMethod.POST

@RestController
@RequestMapping('/login')
class LoginController {

  @Autowired
  UserServiceClient userServiceClient

  @RequestMapping(method = POST)
  ResponseEntity login(@Valid @RequestBody Credentials credentials) {
    def user = userServiceClient.findUser(credentials.username)
    ResponseEntity.ok(user.message)
  }

}
