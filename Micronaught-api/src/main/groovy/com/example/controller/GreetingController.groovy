package com.example.controller

import com.example.controller.POGO.AuthPogo
import com.example.controller.POGO.SimpleRequestPogo
import com.example.controller.POGO.SimpleResponsePogo

import groovy.transform.CompileStatic

//import org.apache.commons.lang3.RandomStringUtils


import io.micronaut.core.util.CollectionUtils
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import org.apache.commons.lang3.RandomStringUtils


@CompileStatic
@Controller("/")
class GreetingController {
    private static Set<String> AUTHORIZED_TOKENS = new HashSet<String>();

    @Get(uri = "basic-get", produces = MediaType.APPLICATION_JSON)
    static SimpleResponsePogo greetings() {
        return new SimpleResponsePogo(response: "Hello world")
    }

    @Get(uri = "greeting-objectless", produces = MediaType.APPLICATION_JSON)
    HttpResponse<Map<String, Object>> getCandyObjectless(@QueryValue String name) {
        return HttpResponse.ok(
                CollectionUtils.mapOf("response", "Hello " + name)
        )
    }

    @Get(uri = "greeting", produces = MediaType.APPLICATION_JSON)
    static SimpleResponsePogo greetings(@QueryValue String name) {
        return new SimpleResponsePogo(response: String.format("Hello %s", name));
    }

    @Post(value = "textReader", consumes = "*/*", produces = MediaType.APPLICATION_JSON)
    SimpleResponsePogo returnObjectText(@Body String text) {
        SimpleResponsePogo response = new SimpleResponsePogo(response: text)
        return response
    }

    @Post(value = "objectReader")
    SimpleResponsePogo returnObject(@Body Object simpleMapping) {
        SimpleResponsePogo response = new SimpleResponsePogo(response: simpleMapping.toString())
        println(simpleMapping.toString())
        return response
    }

    @Post(value = "demoResponse", produces = MediaType.APPLICATION_JSON)
    SimpleResponsePogo returnContent(@Body SimpleRequestPogo simpleMapping) {
        SimpleResponsePogo response = new SimpleResponsePogo(response: simpleMapping.getAt("content"))
        return response
    }

    @Post(value = "demoSecretResponse")
    SimpleResponsePogo returnSecretJson(@Body SimpleRequestPogo simpleMapping, @Header(value = "Authorization") String bearToken) {
        SimpleResponsePogo response = new SimpleResponsePogo()
        if (validateBearer(bearToken)) {
            response.response = simpleMapping["content"]
        } else {
            response.response = "Invalid Bearer Token"
        }
        return response

    }

    @Get(value = "accessToken")
    AuthPogo getAccessToken(@Header(value = "Authorization") String auth) {
        String password = parseJwt(auth)
        String token = "invalid password"
        if (password == "1234") {
            token = RandomStringUtils.randomAlphanumeric(8).toUpperCase()
            AUTHORIZED_TOKENS.add(token)
        }
        AuthPogo response = new AuthPogo(token: token)
        return response

    }


    static String parseJwt(String auth) {
        if (auth.startsWith("Basic ")) {
            String substring = auth.substring(6, auth.length() - 2)
            String decoded = new String(Base64.decoder.decode(substring.getBytes()))
            return decoded
        }
        return null
    }

    static boolean validateBearer(String auth) {
        if (auth.startsWith("Bearer ")) {
            String token = auth.substring(7, auth.length() - 2)
            String decoded = new String(Base64.decoder.decode(token.getBytes()))
            return (decoded in AUTHORIZED_TOKENS)
        }
        return false
    }

}
