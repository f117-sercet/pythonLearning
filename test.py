# Python
# 生成签名
import hashlib
import hmac
import base64
import json

# Python
# 生成签名
import hashlib
import hmac
import base64
import json


def makeSign(key, message):
    j = hmac.new(key.encode(), message.encode(), digestmod=hashlib.sha256)
    return (base64.b64encode(j.digest())).decode()


def main():
    data = {
        "countryCode": "+86",
        "phoneNumber": "+8613935311357",
        "password": "weihu123"
    }
    message = json.dumps(data)  # 此处会排序
    Sign = makeSign(key='aUjqBBt0Jtfw0WxXImfFeejvL8ufq6CR', message=message)
    print(Sign)
    # cE/Wl57Ithy21Elieq5wFsYwJWl2IrkBxlmuCnwI73c=


if __name__ == "__main__":
    main()
