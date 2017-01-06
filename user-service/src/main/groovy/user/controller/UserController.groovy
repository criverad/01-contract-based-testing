package user.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.GET

@RestController
@RequestMapping('/user')
class UserController {

  @RequestMapping(method = GET, value = '/{username}', produces = 'application/json', consumes = 'application/json')
  ResponseEntity findUser(@PathVariable String username) {
    def user = [
        'name'    : 'Some name',
        'lastName': 'Some last name',
        'address' : 'Some address'
    ]
    ResponseEntity.ok(user)
  }

}
