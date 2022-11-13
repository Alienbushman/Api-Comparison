package com.example.controller.POGO

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.ToString

@ToString(includeSuper = false, includePackage = false, includeNames = true)
@JsonIgnoreProperties(ignoreUnknown = true)
class SimpleRequestPogo {
    int id
    String content
}
