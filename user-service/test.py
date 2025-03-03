import requests as req
import json


users = [
    {
        "name": "Hector",
        "email": "hector@gmail.com",
        "age": 18
    },
    {
        "name": "Paloma",
        "email": "paloma@gmail.com",
        "age": 38
    },
    {
        "name": "Marta",
        "email": "marta@gmail.com",
        "age": 32
    },
    {
        "name": "Paco",
        "email": "paco@gmail.com",
        "age": 85
    },
]

def printt(response):
    st = f"{response.request.method} {response.url} => {response.status_code}: "
    try:
        st += f"{response.json()}"
    except:
        st += " No JSON response"
    print(st)
            


url = "http://localhost:8080"

res = req.delete(url + "/user/all" )

res = req.get(url + "/user/all")
printt(res)

res = req.get(url + "/user/1")
printt(res)


headers = {
    "Content-Type": "application/json"
}

for user in users:
    res = req.post(url + "/user", json=user, headers=headers)

res = req.get(url + "/user/all")
printt(res)

res = req.delete(url + "/user", json=users[2], headers=headers)
printt(res)

res = req.get(url + "/user/all")
printt(res)

# res = req.delete(url + "/user/all")
# printt(res)

# res = req.get(url + "/user/all")
# printt(res)

