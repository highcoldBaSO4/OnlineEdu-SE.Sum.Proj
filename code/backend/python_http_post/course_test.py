from urllib import request

authorization = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTU2MzM1MDEyOCwiZXhwIjoxNTYzNDM2NTI4fQ.5g_Roob2OE_p8ZHsPOiUoGaBHp1qty2Mj4xPaffp6WXU14s0z4vCjZi5JUip6gaEML_qVL47V2C228z3GcgplA"

for i in range(55,200):
    tem='%d' %i
    req = request.Request("http://localhost:8080/api/courses/"+tem+"/start?decision=approval")
    req.add_header("Authorization",authorization);
    req.method = 'POST'
    with request.urlopen(req,None) as f:    
        data = f.read()
        print('Data:', data.decode('utf-8'))