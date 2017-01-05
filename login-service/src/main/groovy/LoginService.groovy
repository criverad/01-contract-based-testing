import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

@EnableAutoConfiguration
class LoginService {

  static void main(String... args) {
    SpringApplication.run(LoginService, args)
  }

}
