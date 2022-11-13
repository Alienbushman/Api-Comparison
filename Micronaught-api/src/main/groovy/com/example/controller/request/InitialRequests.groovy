package com.example.controller.request

import com.example.controller.POGO.AuthPogo
import com.example.controller.POGO.SimpleRequestPogo
import com.example.controller.POGO.SimpleResponsePogo
import com.fasterxml.jackson.databind.ObjectMapper

import okhttp3.Call
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody

class InitialRequest {

//    private static domain = "http://localhost:5000"
//    private static domain = "http://localhost:5002"
//    private static domain = "http://localhost:8008"
    private static domain = "http://localhost:8888"
//    private static domain = "http://localhost:8080"
//    private static domain = "http://localhost:8123"



    /**
     * An implementation of the authorization method that will be used throughout the system
     * @return a string which is the authorization token
     */
    static String getAuth(String auth) {
        String url = domain + "/accessToken"
        OkHttpClient client = new OkHttpClient().newBuilder().build()
        String encodedAuth = new String(Base64.encoder.encode(auth.getBytes()))

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Authorization", "Basic $encodedAuth==")
                .build()
        Call call = client.newCall(request)
        Response response = call.execute()
        ResponseBody responseBody = response.body()
        ObjectMapper objectMapper = new ObjectMapper()

        AuthPogo responsePogo = objectMapper.readValue(responseBody.string(), AuthPogo.class)
        println(responsePogo.toString())
        return responsePogo.token

    }


    static void simplePost() {
        String url = domain + "/objectReader"
        SimpleRequestPogo request =
                new SimpleRequestPogo(id: 42, content: "Meaning of life")

        String jsonResponse = doPost(request, url)
        ObjectMapper objectMapper = new ObjectMapper()
        SimpleResponsePogo responsePogo = objectMapper.readValue(jsonResponse, SimpleResponsePogo.class)
        println(responsePogo.toString())
    }

    static void simpleSecretPost() {
        String url = domain + "/demoSecretResponse"
        SimpleRequestPogo request =
                new SimpleRequestPogo(id: 42, content: "Meaning of life")
        String bearer = getAuth("1234")
        String jsonResponse = doPost(request, url, bearer)
        ObjectMapper objectMapper = new ObjectMapper()
        SimpleResponsePogo responsePogo = objectMapper.readValue(jsonResponse, SimpleResponsePogo.class)
        println(responsePogo.toString())
    }

    static void simpleGet(boolean encrypt) {
        String name = "Jimmy"
        String url = domain + "/greeting?name=" + name
        String jsonResponse
        if (encrypt) {
            jsonResponse = doGet(url,getAuth("1234"))

        } else {
            jsonResponse = doGet(url)

        }

        ObjectMapper objectMapper = new ObjectMapper()
        SimpleResponsePogo responsePogo = objectMapper.readValue(jsonResponse, SimpleResponsePogo.class)
        println(responsePogo.toString())
    }


    /**
     *  The standard method for calling any get method. This also returns out the JSON that it receives
     * @param request The body of the project
     * @param url the URL where the request gets sent
     * @param: The access token for accessing a service with encryption
     */
    static String doPost(Object request, String url, String accessToken) {
        RequestBody requestBody = makeRequest(request)
        String bearerToken = new String(Base64.encoder.encode(accessToken.getBytes()))

        Request requestPayload = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Authorization", "Bearer $bearerToken==")
                .header("Content-Type", "application/json")
                .build()

        Response response = createCall(requestPayload)
        String result = response.body().string()

        return result
    }

    /**
     *  The standard method for calling any post method. This also returns out the JSON that it receives
     * @param request The body of the project
     * @param url the URL where the request gets sent
     */
    static String doPost(Object request, String url) {

        RequestBody requestBody = makeRequest(request)

        Request requestPayload = new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Content-Type", "application/json")
                .build()

        Response response = createCall(requestPayload)
        String result = response.body().string()

        return result
    }

    /**
     * The standard method for calling any get method. This also returns out the JSON that it receives
     * @param url the URL where the request gets sent
     */
    static String doGet(String url) {


        Request requestPayload = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build()

        Response response = createCall(requestPayload)
        String result = response.body().string()

        return result
    }

    static String doGet(String url, String accessToken) {

        String bearerToken = new String(Base64.encoder.encode(accessToken.getBytes()))

        Request requestPayload = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $bearerToken==")
                .method("GET", null)
                .build()

        Response response = createCall(requestPayload)
        String result = response.body().string()

        return result
    }


    static RequestBody makeRequest(Object request) {
        ObjectMapper mapper = new ObjectMapper()
        String jsonValue = mapper.writeValueAsString(request)
        RequestBody body = RequestBody.create(
                jsonValue,
                MediaType.parse("application/json charset=utf-8")
        )
        return body
    }

    static Response createCall(Request requestPayload) {
        OkHttpClient client = new OkHttpClient().newBuilder().build()

        Call call = client.newCall(requestPayload)
        Response response = call.execute()
        return response
    }
    static void getPlain(){
        String url = domain + "/greeting"
        String jsonResponse
        jsonResponse = doGet(url)
        println(jsonResponse)

    }

    static void main(String[] args) {
//        simpleGet(false)
//        simplePost()
        println(getAuth("1234"))
//        simpleSecretPost()
    }
}