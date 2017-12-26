<!DOCTYPE html>
<html>
<head>
    <meta http-equiv=content-type content="text/html; charset=UTF-8"/>
    <title>壹化云链统一登录中心</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <style>
        body, div, dl, dt, dd, ul, ol, li, h1, h2, h3, h4, h5, h6, pre, code, form, fieldset, legend, input, textarea, p, blockquote, th, td {
            margin: 0;
            padding: 0
        }

        h1, h2, h3, h4, h5, h6 {
            font-size: 100%
        }

        body {
            font: normal 14px/1.5 Tahoma, Helvetica, arial, sans-serif;
            color: #000
        }

        a {
            color: #399;
            text-decoration: none
        }

        a:hover {
            text-decoration: underline
        }

        h1 {
            background: url(/static/img/logo.png) no-repeat;
            height: 50px;
            text-indent: -9999px
        }

        #hdw {
            width: 960px;
            margin: 0 auto;
            padding: 20px 0 6px
        }

        #bdw {
            background: #4a74b5
        }

        #login-form {
            width: 380px;
            background: #b8c7e1;
            margin: 0 auto;
            border: 1px #d9e2ef solid;
            border-radius: 4px;
            padding: 20px;
            box-shadow: 1px 1px 10px #FFF;
            text-shadow: 1px 1px 1px #FFF
        }

        #login-form h2 {
            font-size: 20px;
            border-bottom: 1px #4a74b5 solid;
            padding-bottom: 6px
        }

        #login-form a {
            text-shadow: none;
            color: #1e5494
        }

        .copyright {
            text-align: center;
            padding: 10px 0
        }
    </style>
</head>
<body id="login">
<div id="doc">
    <div id="hdw">
        <div id="hd">
            <h1>壹化云链统一登录中心</h1>
        </div>
    </div>
    <div id="bdw">
        <div id="bd" class="cf">
            <div id="login-form">
                <h2>下述访问来源不属于壹化云链网</h2>

                <h3>${from!''}</h3>
                <a href="http://www.sinochem.com">点击跳转至主页</a>
            </div>
        </div>
    </div>
</div>
<div class="copyright" style="color:#D0DCE0">
    <p> © <span>2013</span> fengjr.com </p>
</div>
</body>
</html>
