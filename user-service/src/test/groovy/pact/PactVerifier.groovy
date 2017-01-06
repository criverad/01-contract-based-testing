package pact

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import user.UserService

@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = UserService)
@WebAppConfiguration
@IntegrationTest
class PactVerifier {

  @Test
  void connect() {
    def buffer

    def socket = new Socket('localhost', 9091)
    socket.withStreams { input, output ->
      output << "echo testing ...\n"
      buffer = input.newReader().readLine()
      println "response = $buffer"
    }
  }

}
