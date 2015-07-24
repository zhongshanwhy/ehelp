from http.server import HTTPServer,BaseHTTPRequestHandler
import cgi
import requests

class RequestHandler(BaseHTTPRequestHandler):

  def _writeheaders(self):
    #print(self.path)
    #print(self.headers)
    self.send_response(200);
    self.send_header('Content-type','text/html');
    self.end_headers()
  def do_Head(self):
    self._writeheaders()
  def do_GET(self):
    self._writeheaders()

  def vertify_sms_code(self,zone,phone,code,debug=False):
    if debug:
      return 200
    
    data = {"appkey":"8eca9657a56c","phone":phone,"zone":zone,"code":code}
    req = requests.post("https://api.sms.mob.com/sms/verify",data=data,verify=False)
    print("send")
    if req.status_code ==200:
      print("respond receive")
      j=req.json()
      return j.get("status",500)
      
    return 500
    
  def do_POST(self):
    self._writeheaders()
    #length = self.headers.get('content-length');
    #print(length)
    #nbytes = int(length)
    #print("receive the request")
    #data = str(self.rfile.read(nbytes))
    #print(data)
    form = cgi.FieldStorage(
            fp = self.rfile,headers=self.headers,
            environ = {'REQUEST_METHOD':'POST',
                'CONTENT_TYPE':self.headers["Content-type"]})
    phone = form.getvalue("phone")
    print(phone)
    print(self.vertify_sms_code(86,13632459709,"1234"))
addr = ('',9334)
print("server running")
server = HTTPServer(addr,RequestHandler)
server.serve_forever()