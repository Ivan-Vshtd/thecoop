<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">

<@c.page>
<h4>Users<#if isOnline??> online</#if></h4>
<table class="table mt-2" style="width: 50%">
    <thead>
    <tr>
        <th scope="col">Name</th>
        <th scope="col">Groups</th>
        <th scope="col">Status</th>
        <#if isAdmin>
        <th scope="col"></th>
        </#if>
    </tr>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td>
            <img src="<#if user.avatarFilename??>/avatar/${user.avatarFilename}<#else>/static/images/logopng.png</#if>"
                 style="max-width: 45px; max-height: 45px;"/><br/>
            <a href="/user-messages/${user.id}/1 " <#if user.admin>style="color: blueviolet" <#else>style="color: dimgray"</#if>>${user.username}</a>
        </td>
        <td>
            <small><i><#list user.roles as role>${role}<#sep>, </#list></i></small>
        </td>
        <td><i>
            <small>
            <#if user.online><i><a href="/user/online" style="color: green">online</a></i>
            <#elseif user.lastVisit??>Last visit: ${user.lastVisit?string('dd.MM.yyyy HH:mm:ss')}
                <#else><i style="color: red">offline</i>
            </#if></small>
        </i></td>
            <#if isAdmin>
        <td><a href="/user/${user.id}">edit</a></td>
            </#if>
    </tr>
    </#list>
    </tbody>
</table>
</@c.page>