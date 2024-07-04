import random
from locust import task, FastHttpUser, stats

class CouponIssueV1(FastHttpUser):
    connection_timeout = 10.0
    network_timeout = 10.0

    @task
    def issue(self):
        payload = {
            "userId": random.randint(1, 10),
            "shopItemId": random.randint(1, 10)
        }
        with self.rest("POST", "/api/v2/raffle", json=payload):
            pass