import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class LoginController {

  @RequestMapping('/login')
  String login() {
    'Hello world'
  }

}
