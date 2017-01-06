package pact

import au.com.dius.pact.consumer.PactVerified$
import au.com.dius.pact.consumer.VerificationResult
import au.com.dius.pact.consumer.groovy.PactBuilder
import login.LoginService
import login.client.UserServiceClient
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = LoginService)
class UserServicePact {

  @Autowired
  UserServiceClient userServiceClient

  @Test
  void shouldFindUserSuccessfully() {
    def loginService = new PactBuilder() // Create a new PactBuilder

    loginService {
      serviceConsumer "Login Service"
      hasPactWith "User Service"
      port 9091

      uponReceiving('a request to find a user by its username')
      withAttributes(
          method: 'get',
          path: '/user/someuser',
          headers: ['Content-Type': 'application/json']
      )
      willRespondWith(
          status: 200,
          headers: ['Content-Type': 'applicaton/json'],
          body: [
              'name'    : 'Some name',
              'lastName': 'Some last name',
              'address' : 'Some address'
          ]
      )
    }

    VerificationResult result = loginService.run() {
      def user = userServiceClient.findUser('someuser')
      assert user == [
          'name'    : 'Some name',
          'lastName': 'Some last name',
          'address' : 'Some address'
      ]
    }

    assert result == PactVerified$.MODULE$  // This means it is all good in weird Scala speak
  }

  @Test
  void shouldNotFindAUser() {
    def loginService = new PactBuilder() // Create a new PactBuilder

    loginService {
      serviceConsumer "Login Service"
      hasPactWith "User Service"
      port 9091

      uponReceiving('a request to find a user that does not exist')
      withAttributes(
          method: 'get',
          path: '/user/voiduser',
          headers: ['Content-Type': 'application/json']
      )
      willRespondWith(
          status: 404
      )
    }

    VerificationResult result = loginService.run() {
      try {
        userServiceClient.findUser('voiduser')
        assert false: 'Expected an exception to be thrown'
      } catch (all) {
        all.message == 'User not found'
      }
    }

    assert result == PactVerified$.MODULE$  // This means it is all good in weird Scala speak
  }

}