import requests
import base64

# domain = "http://localhost:8123"
domain = "http://localhost:8888"

def hello_get():
    response = requests.get(url=domain)
    data = response.json()
    return data
def simple_get():
    name = "Jimmy"
    url = domain + "/greeting?name=" + name
    response = requests.get(url=url)
    data = response.json()
    return data


def simple_post():
    url = domain + "/textReader"
    request = {'id': 42, 'content': "Meaning of life"}
    response = requests.post(url, data=request)
    data = response.json()
    return data


def simple_secret_post():
    url = domain + "/demoSecretResponse"
    request = '{"id": 42, "content": "Meaning of life"}'
    bearer = encode_base64(get_auth("1234"))

    headers = {"Authorization": f"Bearer {bearer}==", "Content-Type": "application/json"}
    response = requests.post(url, data=request, headers=headers)
    data = response.json()
    return data


def encode_base64(unencoded):
    return str(base64.b64encode(unencoded.encode('ascii')), encoding='ascii')


def get_auth(auth: str):
    url = domain + "/accessToken"
    # usually you can use HTTPBasicAuth, but in this case we need a == at the end, while HTTPBasicAuth only produces =
    encoded_auth = encode_base64(auth)
    headers = {'Authorization': f'Basic {encoded_auth}'}
    response = requests.get(url, headers=headers)
    data = response.json()
    return data['Token']

print(simple_post())
print(get_auth("1234"))
print(simple_secret_post())