from urllib import request
import json
import time
import random
import datetime

start_time = (2019, 7, 1, 0, 0, 0, 0, 0, 0)
end_time = (2019, 9, 8, 23, 59, 59, 0, 0, 0)
start = time.mktime(start_time)
end = time.mktime(end_time)


def random_time():

    t = random.randint(start, end)
    date_tuple = time.localtime(t)
    date = time.strftime("%Y-%m-%d %H:%M:%S", date_tuple)
    return date


for(j) in range(1, 20):
    for i in range(3, 100):
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

            new_req = request.Request("http://202.120.40.8:30382/online-edu/api/courses/18/studyRecord/submit")
            new_req.add_header("Authorization", Token)
            new_req.add_header("Content-Type", "application/json")
            new_req.method = 'POST'

            study_time = random.randint(0, 720)
            time_delta = datetime.timedelta(minutes=study_time)
            start_study = random_time()
            end_study = datetime.datetime.strptime(start_study, "%Y-%m-%d %H:%M:%S")+time_delta
            end_study = end_study.strftime("%Y-%m-%d %H:%M:%S")
            body2 = {
                "action": "START_PLAY",
                "time": start_study
            }

            body3 = {
                "action": "FINISH_WATCH",
                "time": end_study
            }

            request.urlopen(new_req, json.dumps(body2).encode("utf-8"))
            request.urlopen(new_req, json.dumps(body3).encode("utf-8"))

