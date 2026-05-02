# Ping Service to retrieve articles microservice status

Create an api service tro retrieve this microservice status based on PingStatusDto object according to the following premises:

  - There will be an optional request param "fullMode" boolean with the following usage:

    - true: to retrieve full dto info including dependencies info.
    - false: to retrieve only root attributes info and dependencies list should be empty.

  - Root fields will describe the status info for articles microservice. Ensure that version is aligned with version from maven pom.xml.
  - dependencies field will be developed with the status of depending subsystems for this microservice. For the moment
    it will include only Postgresql status info.
  - Ping service will be public accessible with no security authentication.