"use strict";
$(document).ready(function() {

  var myModalBody = $("#my-modal-body");
  var myModal = $('#myModal');

  $('tr td:nth-child(3), tr td:nth-child(4), tr th:nth-child(3), tr th:nth-child(4)').each(function() {
    $(this).addClass('hidden-xs hidden-sm');
  });

  $('input, textarea').each(function() {
    $(this).addClass('form-control input-sm');
  });

  $('button').each(function() {
    $(this).addClass('btn-xs');
  });

  $('.clear').each(function() {
    $(this).click(function() {
      $(this).parent('td').find('.result').html('');
    });
  });

  $.extend({
    __ajax : $.ajax,
    ajax : function(s) {
      return $.__ajax($.extend({
        contentType : "application/json"
      }, s, {
        success : function(data, statusText, xhr) {
          var statusMsg = xhr.status + ' ' + xhr.statusText + '<br/>';
          var dataStr = data ? syntaxHighlight(data) : '';
          s.self.parents('tr').find('.result').html(statusMsg + dataStr);
        },
        error : function(xhr, statusText, e) {
          alert(xhr.responseText);
          var statusMsg = xhr.status + ' ' + xhr.statusText + '<br/>';
          s.self.parents('tr').find('.result').html(statusMsg + e);
        },
        beforeSend : function(xhr, settings) {
          var auth = s.self.parents('tr').find('.auth').val();
          xhr.setRequestHeader('Authorization', auth);
        },
        data : JSON.stringify(s.data)
      }));
    }
  });

  //=============================================================================

  $("#auth_01_curl").click(function() {
    readTextFile('auth_01_curl.txt', function(allText) {
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#auth_02_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('auth_02_curl.txt', function(allText) {
      myModalBody.html(allText.replace("{Authorization}", tr.find('.auth').val()));
      myModal.modal('toggle');
    });
  });

  $("#auth_01").click(function() {
    var self = $(this);
    $.ajax({
      url : url("webapi/auth"),
      type : "POST",
      data : {
        "password" : "password",
        "username" : "bob"
      },
      self : self
    });
  });

  $("#auth_02").click(function() {
    var self = $(this);
    $.ajax({
      url : url("webapi/auth/refresh"),
      type : "POST",
      self : self
    });
  });

  // =============================================================================

  $("#user_01_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('user_01_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#user_02_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('user_02_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      allText = allText.replace("{userId}", tr.find('.userId').val());
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#user_03_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('user_03_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#user_04_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('user_04_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      allText = allText.replace("{username}", tr.find('.username').val())
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#user_05_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('user_05_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val())
      allText = allText.replace("{username}", tr.find('.username').val())
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });
  
  $("#user_06_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('user_06_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val())
      allText = allText.replace("{username}", tr.find('.username').val())
      allText = allText.replace("{password}", tr.find('.password').val())
      allText = allText.replace("{newPassword}", tr.find('.newPassword').val())
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#user_01").click(function() {
    var self = $(this);
    $.ajax({
      url : url("webapi/users"),
      type : "GET",
      self : self
    });
  });

  $("#user_02").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/users/" + tr.find(".userId").val()),
      type : "GET",
      self : self
    });
  });

  var data_user_03 = {
    authority : "admin,user",
    password : "password",
    username : "rudy"
  };
  $("#user_03").parents('tr').find('.data').html(syntaxHighlight(data_user_03));
  $("#user_03").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/users"),
      type : "POST",
      data : data_user_03,
      self : self
    });
  });

  var data_user_04 = {
    authority : "{authority}"
  };
  //"admin,user"
  $("#user_04").parents('tr').find('.data').html(syntaxHighlight(data_user_04));
  $("#user_04").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/users/" + tr.find(".username").val()),
      type : "PUT",
      data : {
        authority : tr.find(".authority").val(),
      },
      self : self
    });
  });

  $("#user_05").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/users/" + tr.find(".username").val()),
      type : "DELETE",
      self : self
    });
  });

  var data_user_06 = {
    password : "{password}",
    newPassword : "{newPassword}"
  };
  $("#user_06").parents('tr').find('.data').html(syntaxHighlight(data_user_06));
  $("#user_06").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/users/" + tr.find(".username").val() + "/changePwd"),
      type : "POST",
      data : {
        password : tr.find(".password").val(),
        newPassword : tr.find(".newPassword").val()
      },
      self : self
    });
  });

  // =============================================================================

  $("#article_01_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('article_01_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#article_02_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('article_02_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      allText = allText.replace("{articleId}", tr.find('.articleId').val());
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#article_03_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('article_03_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#article_04_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('article_04_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      allText = allText.replace("{articleId}", tr.find('.articleId').val())
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#article_05_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('article_05_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val())
      allText = allText.replace("{articleId}", tr.find('.articleId').val())
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#article_01").click(function() {
    var self = $(this);
    $.ajax({
      url : url("webapi/articles"),
      type : "GET",
      self : self
    });
  });

  $("#article_02").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/articles/" + tr.find(".articleId").val()),
      type : "GET",
      self : self
    });
  });

  var data_article_03 = {
    type : "article",
    links : [],
    content : "CONTENT_3",
    createTime : "2009-02-01T02:00:00+08:00",
    status : 0,
    title : "TITLE_3",
    userId : 1000
  };
  $("#article_03").parents('tr').find('.data').html(syntaxHighlight(data_article_03));
  $("#article_03").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/articles"),
      type : "POST",
      data : data_article_03,
      self : self
    });
  });

  var data_article_04 = {
    type : "article",
    links : [],
    content : "CONTENT_3_modified",
    createTime : "2009-02-01T02:00:00+08:00",
    status : 0,
    title : "TITLE_3_modified",
    userId : 1000
  };
  $("#article_04").parents('tr').find('.data').html(syntaxHighlight(data_article_04));
  $("#article_04").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/articles/" + tr.find(".articleId").val()),
      type : "PUT",
      data : data_article_04,
      self : self
    });
  });

  $("#article_05").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/articles/" + tr.find(".articleId").val()),
      type : "DELETE",
      self : self
    });
  });

  // =============================================================================
  $("#comment_01_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('comment_01_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      allText = allText.replace("{articleId}", tr.find('.articleId').val());
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#comment_02_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('comment_02_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      allText = allText.replace("{articleId}", tr.find('.articleId').val());
      allText = allText.replace("{commentId}", tr.find('.commentId').val());
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#comment_03_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('comment_03_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      allText = allText.replace("{articleId}", tr.find('.articleId').val());
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#comment_04_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('comment_04_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val());
      allText = allText.replace("{articleId}", tr.find('.articleId').val())
      allText = allText.replace("{commentId}", tr.find('.commentId').val())
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#comment_05_curl").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    readTextFile('comment_05_curl.txt', function(allText) {
      allText = allText.replace("{Authorization}", tr.find('.auth').val())
      allText = allText.replace("{articleId}", tr.find('.articleId').val())
      allText = allText.replace("{commentId}", tr.find('.commentId').val())
      myModalBody.html(allText);
      myModal.modal('toggle');
    });
  });

  $("#comment_01").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/articles/" + tr.find(".articleId").val() + "/comments"),
      type : "GET",
      self : self
    });
  });

  $("#comment_02").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/articles/" + tr.find(".articleId").val() + "/comments/" + tr.find(".commentId").val()),
      type : "GET",
      self : self
    });
  });

  var data_comment_03 = {
    "content" : "CONTENT_2",
    "createTime" : "2009-01-01T01:00:00+08:00",
    "status" : 0,
    "userId" : 1000
  };
  $("#comment_03").parents('tr').find('.data').html(syntaxHighlight(data_comment_03));
  $("#comment_03").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/articles/" + tr.find(".articleId").val() + "/comments"),
      type : "POST",
      data : data_comment_03,
      self : self
    });
  });

  var data_comment_04 = {
    "content" : "CONTENT_2_modified",
    "createTime" : "2009-01-01T01:00:00+08:00",
    "status" : 0,
    "userId" : 1000
  };
  $("#comment_04").parents('tr').find('.data').html(syntaxHighlight(data_comment_04));
  $("#comment_04").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/articles/" + tr.find(".articleId").val() + "/comments/" + tr.find(".commentId").val()),
      type : "PUT",
      data : data_comment_04,
      self : self
    });
  });

  $("#comment_05").click(function() {
    var self = $(this);
    var tr = self.parents('tr');
    $.ajax({
      url : url("webapi/articles/" + tr.find(".articleId").val() + "/comments/" + tr.find(".commentId").val()),
      type : "DELETE",
      self : self
    });
  });

  // =============================================================================

  function readTextFile(file, callback) {
    var rawFile = new XMLHttpRequest();
    rawFile.open("GET", "./static/curl/" + file, false);
    rawFile.onreadystatechange = function() {
      if (rawFile.readyState === 4) {
        if (rawFile.status === 200 || rawFile.status == 0) {
          var allText = rawFile.responseText;
          callback && callback(allText);
        }
      }
    }
    rawFile.send(null);
  }

  function url(url) {
    return "http:///localhost:8085/zk-springmvc/" + url;
  }

  function syntaxHighlight(json) {
    if (typeof json != 'string') {
      json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function(match) {
      var cls = 'number';
      if (/^"/.test(match)) {
        if (/:$/.test(match)) {
          cls = 'key';
        } else {
          cls = 'string';
        }
      } else if (/true|false/.test(match)) {
        cls = 'boolean';
      } else if (/null/.test(match)) {
        cls = 'null';
      }
      return '<span class="' + cls + '">' + match + '</span>';
    });
  }

});