package org.roy.trb.tst.credit.line.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Messages {

  /** Message if there is more than three rejected credit lines requests for the same customerId */
  public static final String SALES_AGENT_MSG = "A sales agent will contact you";

  /** Message if a user reach the api rate limit */
  public static final String TOO_MANY_REQUESTS_MSG =
      "You have reached your api calls limit, please wait to make new requests";
}
