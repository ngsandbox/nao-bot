import base64
import requests
import json

# read image file
def ReadImage(strImageFilename):

    try:
        image = open(strImageFilename, 'rb')
        image_read = image.read()

    except Exception as e:
        print("Error! " + str(e))
        image_read = False

    return image_read

# encode data to base64
def EncodeImage (image_read):

    try:
        image_64_encode = base64.encodebytes(image_read)

    except Exception as e:
        print("Error! " + str(e))
        image_64_encode = False

    return image_64_encode

contextId =  ''
# post base64 data
def POSTImage(filename, image_64_encode):
    global contextId
    try:
        # post image code
        url = 'http://localhost:8080/requests/base64'
        data = [
                ('id', contextId),
                ('fileName', filename),
                ('fileContent', image_64_encode)
               ]
        r = requests.post(url, data=data)
        print(r.text)
        contextId = json.loads(r.text)['id']
        result = True
    except Exceprion as e:
        print("Error! " + str(e))
        result = False

    return result

# post phrase
def POSTPhrase(strPhrase):
    global contextId
    try:
        url = 'http://localhost:8080/requests/ask'
        data = [
                ('id', contextId),
                ('question', strPhrase)
               ]
        r = requests.post(url, data=data)
        print(r.text)
        contextId = json.loads(r.text)['contextInfo']['id']
        result = True
    except Exception as e:
        print("Error! " + str(e))
        result = False

    return result

# wait for answer from server
def WaitForAnswer(strAnswer):

    try:
        # waiting for answer
        strAnswer = "Server answer"
    except Exception as e:
        print("Error! " + str(e))
        strAnswer = "ERROR"

    return



