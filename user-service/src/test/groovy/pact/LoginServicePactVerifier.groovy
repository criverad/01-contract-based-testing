package pact

import au.com.dius.pact.model.Interaction
import au.com.dius.pact.model.Pact
import au.com.dius.pact.model.Response
import au.com.dius.pact.provider.ProviderClient
import au.com.dius.pact.provider.ProviderInfo
import au.com.dius.pact.provider.ResponseComparison
import au.com.dius.pact.provider.junit.loader.PactBrokerLoader
import groovy.util.logging.Slf4j
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import user.UserService

@Slf4j
@RunWith(SpringJUnit4ClassRunner)
@SpringApplicationConfiguration(classes = UserService)
@WebAppConfiguration
@IntegrationTest
class LoginServicePactVerifier {

  List<Pact> pacts
  ProviderInfo serviceProvider

  @Before
  void setupProvider() {
    serviceProvider = new ProviderInfo(
        name: 'User Service',
        protocol: 'http',
        host: 'localhost',
        port: '9091'
    )

    def loader = new PactBrokerLoader('localhost', '9445', 'http')
    pacts = loader.load('User Service')
  }

  @Test
  void verifyConsumerPacts() {
    def clientResponse

    pacts.each { pact ->
      log.info("Verifying pact between '$pact.consumer.name' and '$pact.provider.name'")

      pact.interactions.each { interaction ->
        log.info("Verifying interaction '$interaction.description'")

        clientResponse = performInteraction(interaction)
        verifyInteraction(interaction, clientResponse)
      }
    }
  }

  def performInteraction(Interaction interaction) {
    def client = new ProviderClient(
        provider: serviceProvider,
        request: interaction.request
    )

    client.makeRequest() as Map
  }

  def verifyInteraction(Interaction interaction, Map clientResponse) {
    // Compare interaction response vs actual client response
    def comparisonResult = ResponseComparison.compareResponse(
        interaction.response as Response,
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
