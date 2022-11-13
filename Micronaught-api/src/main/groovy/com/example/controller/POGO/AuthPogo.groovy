package com.example.controller.POGO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString

@ToString(includeSuper = false, includePackage = false, includeNames = true)
@JsonIgnoreProperties(ignoreUnknown = true)
class AuthPogo {

    @JsonProperty("Token")
    String token

}
