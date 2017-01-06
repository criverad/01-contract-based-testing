package pact

import au.com.dius.pact.model.Pact
import au.com.dius.pact.model.PactReader
import au.com.dius.pact.model.Response
import au.com.dius.pact.provider.ConsumerInfo
import au.com.dius.pact.provider.ProviderClient
import au.com.dius.pact.provider.ProviderInfo
import au.com.dius.pact.provider.ResponseComparison
import org.junit.Before
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
class LoginServicePactVerifier {

  Pact pact
  ProviderInfo serviceProvider

  @Before
  void setupProvider() {
    serviceProvider = new ProviderInfo(
        name: 'User Service',
        protocol: 'http',
        host: 'localhost',
        port: '9091'
    )

    ConsumerInfo serviceConsumer = new ConsumerInfo(
        name: 'Login Service',
        pactFile: new File('/Users/camilo-nab/Documents/01-contract-based-testing/login-service/target/pacts/Login Service-User Service.json')
    )

    serviceProvider.consumers.add(serviceConsumer)
    pact = new PactReader().loadPact(serviceConsumer.pactFile)
  }

  @Test
  void verifyConsumerPacts() {
    def firstInteraction = pact.interactions.first()

    def client = new ProviderClient(
        provider: serviceProvider,
        request: firstInteraction.request
    )

    def clientResponse = client.makeRequest() as Map

    def comparisonResult = ResponseComparison.compareResponse(
        firstInteraction.response as Response,
        clientResponse as Map,
        clientResponse.statusCode as int,
        clientResponse.headers as Map,
        clientResponse.data as String
    )

    // Method matches
    assert comparisonResult.method == true

    // Headers match
    if (comparisonResult.headers.size() > 0) {
      comparisonResult.headers.each() { k, v ->
        assert v == true
      }
    }

    // Empty list of mismatches
    assert comparisonResult.body.size() == 0
  }

}
