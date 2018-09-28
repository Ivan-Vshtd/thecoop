<#include "security.ftl">
<#if (messages?size > 0)>
<table class="table table-striped mb-5 mt-3">
    <thead>
    <tr>
        <th scope="col" width="150"></th>
        <th scope="col" width="170"></th>
        <th scope="col"></th>
        <th scope="col" width="170"></th>
        <th scope="col" width="200"></th>
    </tr>
    </thead>
    <tbody>
    <#list messages as message>
    <tr>
        <th scope="row">
            <div class="card text-center" style="width: 100px; height: 155px;">
                <div class="class-text">
                    <img class="card-img-top mt-3"
                         src="<#if message.authorAvatar??>/avatar/${message.authorAvatar}<#else>/static/images/logopng.png</#if>"
                         style="width: 60px; height: 60px center" alt="avatar"/>
                </div>
                <p class="card-text" style="margin-bottom:8px; line-height: 17px;">
                    <a href="/user-messages/${message.author.id}/1"
                       <#if message.author.admin>style="color: blueviolet" </#if>>${message.authorName}
                    </a><br/>
                    <i>
                        <small>messages: ${message.authorMessagesCount}</small>
                    </i>
                    <i>
                        <small>
                            <small>registered: ${message.authorRegisterDate?string('dd.MM.yy')}</small>
                        </small>
                    </i>
                    <small>
                        <small>
                        <#if message.authorStatus><i style="color: green">online</i>
                        <#else><i style="color: red">offline</i>
                        </#if>
                        </small>
                    </small>
                </p>
            </div>
        </th>
        <td><i> at: ${message.date?string('dd.MM.yyyy HH:mm:ss')}</i></td>
        <td>
        <#if !message.deleted>
            <span>
            <#if message.answerMessage??>
  <footer class="blockquote-footer">
      <cite title="Source Title">
          <#if message.answerMessage.author??>${message.answer}
          <#else>${message.answerMessage.text}
          </#if>
      </cite>
  </footer>
            </#if>${message.text}
            </span><br/>
            <div class="d-flex align-items-baseline">
            <#if message.filename??>
                <img src="/img/${message.filename}" style="max-width: 200px; max-height: 200px;"/>
            </#if>
            </div>
            <i>
                <small><a href="/main?filter=${message.tag}">#${message.tag}</a></small>
            </i>
        <#else>
            <i style="color: dimgray">${message.text}</i>
        </#if>
            <#if message.updates??>
                <div>
                    <small><i style="color: dimgray">modified: ${message.updates?string('dd.MM.yyyy HH:mm:ss')}</i></small>
                </div>
            </#if>
        </td>
        <td>
            <a href="<#if message.dialog>/privates/<#else>/branches/</#if>${message.branchName}/1">to ${message.branchName} topic</a>
        </td>
        <td>
            <#if isAdmin>
                <div class="btn-group" role="group">
                    <button id="btnGroupDrop1" type="button" class="btn btn-outline-secondary btn-sm dropdown-toggle"
                            data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Activity
                    </button>
                    <div class="dropdown-menu" aria-labelledby="btnGroupDrop1">
                    <#if message.author.id == currentUserId>
                    <a class="dropdown-item" href="/user-messages/${message.author.id}/1?message=${message.id}">Edit</a>
                    </#if>
                        <a class="dropdown-item" href="/answer/${name}/${message.id}">Answer</a>
                        <a class="dropdown-item" href="/message-del=${message.id}">Delete</a>
                        <a class="dropdown-item" href="/message-delete=${message.id}">dbDelete</a>
                    </div>
                </div>
            <#else>
                <#if message.author.id == currentUserId && !message.deleted>
            <a class="btn btn-outline-secondary btn-sm" href="/user-messages/${message.author.id}/1?message=${message.id}">
                Edit
            </a>
            <a class="btn btn-outline-secondary btn-sm" href="/message-del=${message.id}">
                Delete
            </a>
                </#if>
            <a class="btn btn-outline-secondary btn-sm" href="/answer/${name}/${message.id}">
                Answer
            </a>
            </#if>
        </td>
    </tr>
    </#list>
    </tbody>
</table>
<#else>
<div><i>no messages yet</i></div>
</#if>