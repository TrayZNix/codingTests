import requests as req
import json

message = {"userSenderId": 1,
           "userReceiverId": 2,
           "body": "Notification received!"}

headers = {
    "Content-Type": "application/json"
}


def printt(response):
    st = f"{response.request.method} {response.url} => {response.status_code}: "
    try:
        st += f"{response.json()}"
    except:
        st += " No JSON response"
    print(st)
            


url = "http://localhost:8282"

MESSAGE_URL = "http://localhost:8181"

res = req.get(url + "/notification/all")
printt(res)

res = req.post(MESSAGE_URL + "/message", json=message, headers=headers)

res = req.get(url + "/notification/all")
printt(res)
