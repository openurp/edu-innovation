[#ftl]
[@b.head/]
[@b.toolbar title="新增/修改评审专家"]bar.addBack();[/@]
[@b.tabs]
  [@b.form theme="list" action=b.rest.save(expert)]
    [@b.textfield name="expert.code" label="账户" value="${expert.code!}" required="true" maxlength="80" style="width:200px;"/]
    [@b.textfield name="expert.name" label="姓名" value="${expert.name!}" required="true" maxlength="80" style="width:200px;"/]
    [@b.textfield name="expert.password" label="密码" value="${expert.password!}" required="true" maxlength="80" style="width:200px;"/]
    [@b.startend name="expert.beginOn,expert.endOn" label="有效期" required="true" start=expert.beginOn end=expert.endOn! /]
    [@b.textarea name="expert.intro" label="介绍" value="${expert.intro!}" required="false" maxlength="500" cols="80" rows="3"/]
    [@b.formfoot]
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
[/@]
[@b.foot/]