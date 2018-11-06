<#include "security.ftl">
<#if (messages?size > 0)>
<table class="table table-striped mb-5 mt-3">

    <tbody>
    <#list messages as message>
    <tr>
        <th scope="row">
            <div class="card text-center" style="width: 125px; height: 195px;">
                <div class="class-text">
                    <img class="card-img-top mt-3"
                         src="<#if message.authorAvatar??>/avatar/${message.authorAvatar}<#else>/static/images/logopng.png</#if>"
                         style="width: 80px; height: 80px center" alt="avatar"/>
                </div>
                <p class="card-text" style="margin-bottom:8px; line-height: 17px;">
                    <a href="/user-profile/${message.author.id}/1"
                       <#if message.author.admin>style="color: blueviolet" </#if>>${message.authorName}
                    </a><br/>
                    <#if message.author.info??>
                        <small>from: ${message.author.info.location!"here"}</small>
                    </#if>
                    <i>
                        <small>
                            messages: ${message.authorMessagesCount}
                            registered: ${message.authorRegisterDate?string('dd.MM.yy')}
                        </small>
                    </i>
                    <small>
                        <small>
                            <#if !message.author.active>
                                <i><b style="color: red">
                                        BANNED!
                                </b></i>
                        <#elseif message.authorStatus><i><a href="/user/online" style="color: green">online</a></i>
                        <#else><i style="color: red">offline</i>
                        </#if>
                        </small>
                    </small>
                </p>
            </div>
        </th>
        <td><i>
            <i class="fas fa-calendar-alt msg"></i>
            ${message.date?string('dd.MM.yyyy HH:mm:ss')}</i></td>
        <td style="max-width: 50em">
        <#if !message.deleted>
            <span>
            <#if message.answerMessage??>

      <div style="color: dimgray">
         <i class="fas fa-comment msg"></i>
          <i><small>
          <#if message.answerMessage.author??>${message.answer}
          <#else>${message.answerMessage.text}
          </#if>
          </small></i>
      </div>
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
            <i style="color: dimgray">
                <i class="fas fa-eraser msg"></i>
                ${message.text}</i>
        </#if>
            <#if message.updates??>
                <div>
                    <small><i style="color: dimgray">
                        <i class="fas fa-pencil-alt msg"></i>
                        ${message.updates?string('dd.MM.yyyy HH:mm:ss')}</i></small>
                </div>
            </#if>
        </td>
        <td>
            <a href="<#if message.dialog>/privates/<#else>/branches/</#if>${message.branchName}/1">to ${message.branchName} topic</a>
        </td>
        <td>
            <a href="/answer/${name}/${message.id}">
                <i class="far fa-comment"></i>
            </a>
            <#if message.author.id == currentUserId && !message.deleted>
            <a href="/user-profile/${message.author.id}/1?message=${message.id}">
                <i class="fas fa-pencil-alt"></i>
            </a>
            <a href="/message-erase=${message.id}">
                <i class="fas fa-eraser"></i>
            </a>
                </#if>
             <#if isAdmin>
             <a href="/message-delete=${message.id}">
                 <i class="fas fa-trash-alt"></i>
             </a>
             </#if>
        </td>
        <td>
            <a style="color: saddlebrown" class="col align-self-center" href="/${message.id}/like">
            <#if message.meLiked>
            <i class="fas fa-heart"></i>
            <#else>
            <i class="far fa-heart"></i>
            </#if>
            ${message.likes?size}
        </td>
    </tr>
    </#list>
    </tbody>
</table>
<#else>
<div><i>no messages yet</i></div>
</#if>