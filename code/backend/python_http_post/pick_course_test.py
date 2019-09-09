from urllib import request
import json

for i in range(3, 33):
    body = {
        "username": "studen%d" % i,
        "password": "%d" % (111110+i),
    }
    req = request.Request("http://202.120.40.8:30382/online-edu/api/auth/signin")
    req.add_header("Content-Type", "application/json")
    req.method = 'POST'
    with request.urlopen(req, json.dumps(body).encode("utf-8")) as f:
        head = "Bearer "
        data = f.read()
        data = data.decode('utf-8')
        data = json.loads(data)
        Token = head+data['accessToken']
        new_req = request.Request("http://202.120.40.8:30382/online-edu/api/courses/14/pick")
        new_req.add_header("Authorization", Token)
        new_req.method = 'POST'
        with request.urlopen(new_req, None) as nf:
            new_data = nf.read()
            print(new_data.decode('utf-8'))

