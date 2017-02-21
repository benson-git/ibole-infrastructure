Certificate Authority - 数字证书认证机构，经常被缩写为CA

用户登录后，服务端会发行一个有效时间的access token，同时也会发行一个长生命期的refresh token。用户在进行其他网络请求时，会把access token加入请求体中（并不需要加入refresh token）。如果在请求过程中，access token过期，返回相应的状态码。这时就回调用一个回调方法，在回调方法体中将原来的access token和refresh token发送给服务端，获取新的access token。然后把新的access token加入刚才的请求体中，重新加载网络请求。