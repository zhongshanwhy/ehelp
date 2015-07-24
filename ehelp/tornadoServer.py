import tornado.ioloop
import tornado.web
import cgi
import json
import time

KEEP_ALIVE=-1
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
RES_GET_PINFO_OK = 3
RES_USER_NOT_EXIT = 4


userlist={}
flag = 0

#define the class EHelpHTTPHandler which deals with http request from clients 
class MainHandler(tornado.web.RequestHandler):
    #write the http head for preparing to send a respond to a client
    def _writerheaders(self):
        self.set_header('Content-type','application/json')

    @tornado.web.asynchronous   
    def wait(self):
        global flag
        print("waiting")
        #print(flag)
        if flag == 1:
            print("finish")
            flag = 0
            self.finish()
        elif flag == 0:
            print("keep waiting")
            tornado.ioloop.IOLoop.instance().add_timeout(time.time()+2,callback = self.wait)
    
    real_loginHandler = '''def handleLogin(self):
       print("login")
        self._writerheaders()
        for item in userlist.keys():
            print("index:"+item+"name:"+userlist[item]["name"]+"password:"+userlist[item]["password"])
            if self.get_argument("phone") == item and self.get_argument("password") == userlist[item]["password"]:
                respond={"respondType":RES_LOGIN_OK,"phone":item,"name":userlist[item]["name"]}
                self.finish(respond)
                #self.wfile.write(json.dumps(respond).encode())
                return
        respond={"respondType":RES_LOGIN_FAIL}
        self.finish(respond)'''
    
    def handleLogin(self):
        global userlist
        global flag
        flag = 1
        #print(flag)
        print("login")
        self._writerheaders()
        #userlist[self.get_argument("phone")]={"name":self.get_argument("phone"),"password":self.get_argument("password")}
        for item in userlist.keys():
            print("index:"+item+"name:"+item+"password:"+userlist[item]["password"])
            if self.get_argument("phone") == item and self.get_argument("password") == userlist[item]["password"]:
                respond={"respondType":RES_LOGIN_OK,"phone":item,"name":userlist[item]["name"]}
                self.finish(respond)
                return
        respond={"respondType":RES_LOGIN_FAIL}
        self.finish(respond)
    
    def handleSign(self):
        global userlist
        print("sign")
        print(self.get_argument("phone"))
        print(self.get_argument("name"))
        print(self.get_argument("password"))
        self._writerheaders()
        userlist[self.get_argument("phone")]={"name":self.get_argument("name"),"password":self.get_argument("password")}
        respond={"respondType":RES_SIGN_OK}
        self.finish(respond)
        #self.wfile.write(json.dumps(respond).encode())

    def handleFriending(self):
        print("friending")
    def handleDelFriend(self):
        print("delete friend")
    def handleGetPersonInfo(self):
        global userlist
        print("Get person infomation")
        for item in userlist.keys():
            if userlist[item]["name"] == self.get_argument("name"):
                print("user "+self.get_argument("name")+" exists and return")
                respond = {"respondType":RES_GET_PINFO_OK,"phone":item,"name":userlist[item]["name"]}
                self.finish(respond)
                return
        respond = {"respondType":RES_USER_NOT_EXIT,"name":self.get_argument("name")}
        self.finish(respond)
        
    def handleGetFriendList(self):
        print("get friend list")
    def handleFirstAid(self):
        print("first aid")
    def handleForHelp(self):
        print("for help")
    def handleRaiseQuestion(self):
        print("raise question")
    def handleGetQuestionList(self):
        print("get question")
    def handleAppraise(self):
        print("appraise")


    #deal with http request of POST method
    @tornado.web.asynchronous
    def post(self):
        print("http request")
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
        requestType = self.get_argument("requestType")
        type = int(requestType)
        if type == KEEP_ALIVE:
            #tornado.ioloop.IOLoop.instance().add_timeout(time.time()+2,callback = self.wait)
            self.wait()
        else:
            handlerDic[type]()
application = tornado.web.Application([
    (r"/", MainHandler),
])
        #print(self.vertify_sms_code(86,13632459709,code))
        #password=form.get_argument("password")
        #print(password)
            #print("request:"+form.list[0].name+" "+form.list[0].value)
        
        #self._writerheaders()
        #respond={"respondType":1,"msg":"login ok"}
        #self.wfile.write(json.dumps(respond).encode())

if __name__ == "__main__":
    application.listen(9334)
    print("running")
    tornado.ioloop.IOLoop.instance().start()
