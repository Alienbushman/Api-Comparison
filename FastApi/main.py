import base64

from fastapi import FastAPI, Request, Header
import uvicorn
import secrets

from POPO.simple_request_popo import SimpleRequestPopo

app = FastAPI()

AUTHORIZED_TOKENS = set()


@app.get("/")
async def root():
    return {"message": "Hello World"}


@app.get("/greeting/{name}")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}


@app.get("/greeting")
async def say_hello(name: str):
    return {"message": f"Hello {name}"}


@app.post("/textReader")
async def text_reader(request: Request):
    # I can't seem to find a good body to actual json converter, but this is the closest that I have found
    body = await request.body()
    return {"response": body}


@app.post("/objectReader")
async def object_reader(request: Request):
    body = await request.body()
    return {"response": body}


@app.get("/accessToken")
async def access_token(authorization: str | None = Header(default=None)):
    if (decode_auth_base64(authorization) == "1234"):
        token = secrets.token_hex(8)
        AUTHORIZED_TOKENS.add(token)
        return {"Token": token}

    return {"Token": "invalid password"}


@app.post("/demoResponse")
async def return_json(popo: SimpleRequestPopo):
    return {"response": popo.content}


@app.post("/demoSecretResponse")
async def return_secret_json(popo: SimpleRequestPopo, authorization: str | None = Header(default=None)):
    if (validate_bearer(authorization)):
        return {"response": popo.content}
    return {"response": "Invalid Bearer Token"}


def validate_bearer(auth: str):
    if (auth.startswith("Bearer ")):
        decoded = str(base64.b64decode(auth[7:-2]), encoding='ascii')
        return decoded in AUTHORIZED_TOKENS
    return False


def decode_auth_base64(encoded: str):
    if (encoded.startswith("Basic ")):
        return str(base64.b64decode(encoded[6:-2]), encoding='ascii')
    return ""


if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8123)

    # Swagger available at http://127.0.0.1:8000/docs
