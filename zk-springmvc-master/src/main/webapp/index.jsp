<html>
<header>
    <script src="https://code.jquery.com/jquery-3.3.1.js" integrity="sha256-2Kok7MbOyxpgUVvAk/HJ2jigOSYS2auK4Pfzbm7uH60=" crossorigin="anonymous"></script>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
        crossorigin="anonymous">
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <script src="./index.js"></script>
    <style>
.table {
  table-layout: fixed;
  word-wrap: break-word;
  font-size: 13px;
  margin : 20px 0px;
  background-color: #fff;
}

.format {
  white-space: pre;
}
textarea {
    border: none;
    width: 100%;
    -webkit-box-sizing: border-box; /* <=iOS4, <= Android  2.3 */
       -moz-box-sizing: border-box; /* FF1+ */
            box-sizing: border-box; /* Chrome, IE8, Opera, Safari 5.1*/
}

body {
  font-family: apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Fira Sans", "Droid Sans", "Helvetica Neue", Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji",
    "Segoe UI Symbol";
  background-color: #ddd;
}
th {
    background-color: #999;
    color: white;
} 
</style>
</header>
<body>
    <div style="padding: 10px 30px;">
        <div class="row">
            <div class="col-md-12">
                <h2>Jersey RESTful Web Application!</h2>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <h4>AuthenticationResource</h4>
                    </div>
                    <div class="panel-body">
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th style="width: 5%">Type</th>
                                    <th style="width: 10%">Url</th>
                                    <th style="width: 20%">Data</th>
                                    <th style="width: 20%">Authorization(Bearer )</th>
                                    <th style="width: 35%">Response</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>
                                        <button id="auth_01" class="btn btn-info">Send</button> POST
                                    </td>
                                    <td>/auth</td>
                                    <td class="data">{ "username" : "bob", "password" : "password" }</td>
                                    <td><textarea class="auth" rows="5"></textarea></td>
                                    <td><button class="btn btn-warning clear">Clear</button>
                                        <div class="result"></div></td>
                                </tr>
                                <tr>
                                    <td>
                                        <button id="auth_02" class="btn btn-info">Send</button> POST
                                    </td>
                                    <td>/auth/refresh</td>
                                    <td class="data"></td>
                                    <td><textarea class="auth"></textarea></td>
                                    <td><button class="btn btn-warning clear">Clear</button>
                                        <div class="result"></div></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-body">
                        <ul class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#t1"><h4>UserResource</h4></a></li>
                            <li><a data-toggle="tab" href="#t2"><h4>ArticleResource</h4></a></li>
                            <li><a data-toggle="tab" href="#t3"><h4>CommentResource</h4></a></li>
                        </ul>
                        <div class="tab-content">
                            <div id="t1" class="tab-pane fade in active">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th style="width: 5%">Type</th>
                                            <th style="width: 10%">Url</th>
                                            <th style="width: 20%">Data</th>
                                            <th style="width: 20%">Authorization(Bearer )</th>
                                            <th style="width: 35%">Response</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <button id="user_01" class="btn btn-info">Send</button> GET
                                            </td>
                                            <td>/users</td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="user_02" class="btn btn-info">Send</button> GET
                                            </td>
                                            <td>/users/{userId} <input class="userId" placeholder="userId" /></td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="user_03" class="btn btn-info">Send</button> POST
                                            </td>
                                            <td>/users</td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="user_04" class="btn btn-info">Send</button> PUT
                                            </td>
                                            <td>/users/{username}<input class="username" placeholder="username" /></td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="user_05" class="btn btn-info">Send</button> DELETE
                                            </td>
                                            <td>/users/{username}<input class="username" placeholder="username" /></td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="t2" class="tab-pane fade">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th style="width: 5%">Type</th>
                                            <th style="width: 10%">Url</th>
                                            <th style="width: 20%">Data</th>
                                            <th style="width: 20%">Authorization(Bearer )</th>
                                            <th style="width: 35%">Response</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <button id="article_01" class="btn btn-info">Send</button> GET
                                            </td>
                                            <td>/articles</td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="article_02" class="btn btn-info">Send</button> GET
                                            </td>
                                            <td>/articles/{articleId} <input class="articleId" placeholder="articleId" /></td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="article_03" class="btn btn-info">Send</button> POST
                                            </td>
                                            <td>/articles</td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="article_04" class="btn btn-info">Send</button> PUT
                                            </td>
                                            <td>/articles/{articleId}<input class="articleId" placeholder="articleId" /></td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="article_05" class="btn btn-info">Send</button> DELETE
                                            </td>
                                            <td>/articles/{articleId}<input class="articleId" placeholder="articleId" /></td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="t3" class="tab-pane fade">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th style="width: 5%">Type</th>
                                            <th style="width: 20%">Url</th>
                                            <th style="width: 20%">Data</th>
                                            <th style="width: 20%">Authorization(Bearer )</th>
                                            <th style="width: 25%">Response</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <button id="comment_01" class="btn btn-info">Send</button> GET
                                            </td>
                                            <td>/articles/{articleId}/comments<input class="articleId" placeholder="articleId" /></td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="comment_02" class="btn btn-info">Send</button> GET
                                            </td>
                                            <td>/articles/{articleId}/comments/{commentId}<input class="articleId" placeholder="articleId" /><input class="commentId" placeholder="commentId" /></td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="comment_03" class="btn btn-info">Send</button> POST
                                            </td>
                                            <td>/articles/{articleId}/comments<input class="articleId" placeholder="articleId" /></td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="comment_04" class="btn btn-info">Send</button> PUT
                                            </td>
                                            <td>/articles/{articleId}/comments/{commentId}<input class="articleId" placeholder="articleId" /><input class="commentId" placeholder="commentId" /></td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <button id="comment_05" class="btn btn-info">Send</button> DELETE
                                            </td>
                                            <td>/articles/{articleId}/comments/{commentId}<input class="articleId" placeholder="articleId" /><input class="commentId" placeholder="commentId" /></td>
                                            <td class="data format"></td>
                                            <td><textarea class="auth"></textarea></td>
                                            <td><button class="btn btn-warning clear">Clear</button>
                                                <div class="result format"></div></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>