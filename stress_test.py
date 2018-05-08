import http.client
import random

def randomWord():
    word = ""
    for _ in range(10):
        rand = random.randint(66,89)
        word += str(chr(rand))
    return word


if __name__ == "__main__":
    # conn = http.client.HTTPConnection("localhost", 8080)
    conn = http.client.HTTPConnection("54.227.151.133", 8080)

    bio = "This is a decent sized bio. It's full of words that dont have any names or cities. I like cats, dogs, pigs, sheep, cars, drinks, food, people, and all things."

    for _ in range(2):
        bio += " " + randomWord() + "."
        body = '{"uid": 4150,"image": "/img/default_profile_pic.png","bio": "' + bio + '","password": "deleteMe","email": "sara@one.com","username": "sarasue","tags": ""}'

        conn.request("PUT", "/users/update", body=body)

        print(conn.getresponse().read())
        # print(conn.getresponse().status)
