Certificate Authority - 数字证书认证机构，经常被缩写为CA

用户登录后，服务端会发行一个有效时间的access token，同时也会发行一个长生命期的refresh token。用户在进行其他网络请求时，会把access token加入请求体中（并不需要加入refresh token）。如果在请求过程中，access token过期，返回相应的状态码。这时就回调用一个回调方法，在回调方法体中将原来的access token和refresh token发送给服务端，获取新的access token。然后把新的access token加入刚才的请求体中，重新加载网络请求。

基于token的鉴权机制

基于token的鉴权机制类似于http协议也是无状态的，它不需要在服务端去保留用户的认证信息或者会话信息。这就意味着基于token认证机制的应用不需要去考虑用户在哪一台服务器登录了，这就为应用的扩展提供了便利。

流程上是这样的：

用户使用用户名密码来请求服务器
服务器进行验证用户的信息
服务器通过验证发送给用户一个token
客户端存储token，并在每次请求时附送上这个token值
服务端验证token值，并返回数据
这个token必须要在每次请求时传递给服务端，它应该保存在请求头里， 另外，服务端要支持CORS(跨来源资源共享)策略，一般我们在服务端这么做就可以了Access-Control-Allow-Origin: *。