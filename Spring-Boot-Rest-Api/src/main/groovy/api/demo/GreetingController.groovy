package api.demo

import api.demo.POGO.AuthPogo
import api.demo.POGO.SimpleRequestPogo
import api.demo.POGO.SimpleResponsePogo
import org.apache.commons.lang3.RandomStringUtils

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class GreetingController {

    //in production you would want a hashmap with a counter when it should expire
    private static Set<String> AUTHORIZED_TOKENS = new HashSet<String>();


    @GetMapping("/greeting")
    SimpleResponsePogo greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new SimpleResponsePogo(response: String.format("Hello, %s!", name));
    }

    @RequestMapping(value = "/demoResponse", method = RequestMethod.POST)
    SimpleResponsePogo returnContent(@RequestBody SimpleRequestPogo simpleMapping) {
        SimpleResponsePogo response = new SimpleResponsePogo(response: simpleMapping.getAt("content"))
        return response

    }

    @RequestMapping(value = "/demoSecretResponse", method = RequestMethod.POST)
    SimpleResponsePogo returnSecretJson(@RequestBody SimpleRequestPogo simpleMapping, @RequestHeader(value = "Authorization") String bearToken) {
        SimpleResponsePogo response = new SimpleResponsePogo()
        if (validateBearer(bearToken)) {
            response.response = simpleMapping["content"]
        } else {
            response.response = "Invalid Bearer Token"
        }
        return response

    }

    @RequestMapping(value = "/objectReader", method = RequestMethod.POST)
    SimpleResponsePogo returnObject(@RequestBody Object simpleMapping) {
        SimpleResponsePogo response = new SimpleResponsePogo(response: simpleMapping.toString())
        println(simpleMapping.toString())
        return response
    }

    @RequestMapping(value = "/textReader", method = RequestMethod.POST, consumes = "*/*")
    SimpleResponsePogo returnObjectText(@RequestBody String text) {
        SimpleResponsePogo response = new SimpleResponsePogo(response: text)
        return response

    }

    @RequestMapping(value = "/accessToken", method = RequestMethod.GET)
    AuthPogo getAccessToken(@RequestHeader(value = "Authorization") String auth) {
        String bearerToken = parseJwt(auth)
        String passcode = "invalid password"
        if (bearerToken == "1234") {
            passcode = RandomStringUtils.randomAlphanumeric(8).toUpperCase()
            AUTHORIZED_TOKENS.add(passcode)
        }
        AuthPogo response = new AuthPogo(token: passcode)
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