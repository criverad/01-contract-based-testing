package login.model

import javax.validation.constraints.NotNull

class Credentials {

  @NotNull
  String username

  @NotNull
  String password

}
