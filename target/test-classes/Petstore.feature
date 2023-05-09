Feature: PetStore API

  Scenario: List pets API
    Given user has petStore endpoint
    When user sends GET request to list pets
    Then status code is 200
    And response contains list of pets

  Scenario: Get non-existing pet
    Given user has petStore endpoint
    When user sends GET request to list non-existing pet by id
    Then  status code is 404
    And error message is 'Pet not found'
