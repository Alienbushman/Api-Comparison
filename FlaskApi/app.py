import base64
import secrets

from flask import Flask, jsonify, request
from gevent.pywsgi import WSGIServer


app = Flask(__name__)

AUTHORIZED_TOKENS = set()


@app.route('/')
def hello_world():  # put application's code here
    return jsonify({'response': 'Hello World!'})


@app.route("/greeting/<name>")
def say_hello_variable(name: str):
    return jsonify({"message": f"Hello {name}"})


@app.route("/greeting/")
def say_hello():
    name = request.args.get('name')
    return jsonify({"message": f"Hello {name}"})


@app.route("/textReader", methods=["POST"])
def text_reader():
    body = request.get_json()
    return jsonify({"response": body})


@app.route("/objectReader", methods=["POST"])
def object_reader():
    body = str(request.get_json())
    return jsonify({"response": body})


@app.route("/accessToken")
def access_token():
    authorization = request.headers.get('authorization')
    if (decode_auth_base64(authorization) == "1234"):
        token = secrets.token_hex(8)
        AUTHORIZED_TOKENS.add(token)
        return jsonify({"Token": token})

    return jsonify({"Token": "invalid password"})


@app.route("/demoResponse")
def return_json():
    body = request.get_json()
    return {"response": body['content']}


@app.route("/demoSecretResponse", methods=["POST"])
def return_secret_json():
    body = request.get_json()
    authorization = request.headers.get('authorization')
    if (validate_bearer(authorization)):

        return jsonify({"response": body['content']})
    return jsonify({"response": "Invalid Bearer Token"})


def validate_bearer(auth: str):
    if (auth.startswith("Bearer ")):
        decoded = str(base64.b64decode(auth[7:]), encoding='ascii')
        return decoded in AUTHORIZED_TOKENS
    return False


def decode_auth_base64(encoded: str):
    if (encoded.startswith("Basic ")):
        return str(base64.b64decode(encoded[6:]), encoding='ascii')
    return ""


if __name__ == '__main__':
    # For debug
    # app.run(debug=True,host='127.0.0.1',  port=5002)
    # For production
    http_server = WSGIServer(('', 5002), app)
    http_server.serve_forever()
