from http.server import HTTPServer,BaseHTTPRequestHandler
import cgi
import json

REQ_LOGIN=0
REQ_SIGN = 1
REQ_FRIENDING = 2
REQ_DEL_FRIEND = 3
REQ_GET_PINFO = 4
REQ_GET_FRIENDLIST = 5
REQ_FIRST_AID = 6
REQ_FOR_HELP = 7
REQ_RAISE_QUESTION = 8
REQ_GET_QUESTIONLIST = 9
REQ_APPRAISE = 10

RES_LOGIN_OK = 0
RES_LOGIN_FAIL = 1
RES_SIGN_OK = 2

userlist={}

#define the class EHelpHTTPHandler which deals with http request from clients 
class EHelpHTTPHandler(BaseHTTPRequestHandler):

    #write the http head for preparing to send a respond to a client
        
    def _writerheaders(self):
        print(self.path)
        print(self.headers)
        self.send_response(200)
        self.send_header('Content-type','application/json')
        self.end_headers()
    
    #deal with http request of GET method
    def do_GET(self):
        buf = 'it works'

    def handleLogin(self,form):
        print("login")
        self._writerheaders()
        for item in userlist.keys():
            print("index:"+item+"name:"+userlist[item]["name"]+"password:"+userlist[item]["password"])
            if form.getvalue("phone") == item and form.getvalue("password") == userlist[item]["password"]:
                respond={"respondType":RES_LOGIN_OK,"phone":item,"name":userlist[item]["name"]}
                self.finish(respond)
                #self.wfile.write(json.dumps(respond).encode())
                return
        respond={"respondType":RES_LOGIN_FAIL}
        self.finish(respond)
        self.wfile.write(json.dumps(respond).encode())
 
    def handleSign(self,form):
        print("sign")
        print(form.getvalue("phone"))
        print(form.getvalue("name"))
        print(form.getvalue("password"))
        self._writerheaders()
        userlist[form.getvalue("phone")]={"name":form.getvalue("name"),"password":form.getvalue("password")}
        respond={"respondType":RES_SIGN_OK}
        self.finish(respond)
        #self.wfile.write(json.dumps(respond).encode())

    def handleFriending(self,form):
        print("friending")
    def handleDelFriend(self,form):
        print("delete friend")
    def handleGetPersonInfo(self,form):
        print("Get person infomation")
        self._writerheaders()
    def handleGetFriendList(self,form):
        print("get friend list")
    def handleFirstAid(self,form):
        print("first aid")
    def handleForHelp(self,form):
        print("for help")
    def handleRaiseQuestion(self,form):
        print("raise question")
    def handleGetQuestionList(self,form):
        print("get question")
    def handleAppraise(self,form):
        print("appraise")

    def vertify_sms_code(self,zone,phone,code,debug=False):
        if debug:
            return 200
        data = {"appkey":"8f22c61e85e0","phone":phone,"zone":zone,"code":code}
        req = requests.post("https://api.sms.mob.com/sms/verify",data=data,verify=False)
        print("send")
        if req.status_code ==200:
            print("respond receive")
            j=req.json()
            return j.get("status",500)
        return 500

    #deal with http request of POST method
    def do_POST(self):
        print("http request")
        form = cgi.FieldStorage(
            fp = self.rfile,headers=self.headers,
            environ = {'REQUEST_METHOD':'POST',
                'CONTENT_TYPE':self.headers["Content-type"]})
        handlerDic={
            REQ_LOGIN:self.handleLogin,
            REQ_SIGN :self.handleSign,
            REQ_FRIENDING:self.handleFriending,
            REQ_DEL_FRIEND:self.handleDelFriend,
            REQ_GET_PINFO:self.handleGetPersonInfo,
            REQ_GET_FRIENDLIST:self.handleGetFriendList,
            REQ_FIRST_AID:self.handleFirstAid,
            REQ_FOR_HELP:self.handleForHelp,
            REQ_RAISE_QUESTION:self.handleRaiseQuestion,
            REQ_GET_QUESTIONLIST:self.handleGetQuestionList,
            REQ_APPRAISE:self.handleAppraise
        }
        requestType = form.getvalue("requestType")
        type = int(requestType)
        handlerDic[type](form)

        #print(self.vertify_sms_code(86,13632459709,code))
        #password=form.getvalue("password")
        #print(password)
            #print("request:"+form.list[0].name+" "+form.list[0].value)
        
        #self._writerheaders()
        #respond={"respondType":1,"msg":"login ok"}
        #self.wfile.write(json.dumps(respond).encode())

http_server=HTTPServer(('',9334),EHelpHTTPHandler)
http_server.serve_forever()
print("running")