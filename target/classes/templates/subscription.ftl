<#import "parts/common.ftl" as c>

<@c.page>
<h3>${userChannel.username}</h3>
<div>${type}</div>
<table class="table mt-2" style="width: 500px">
    <thead>
    </thead>
    <tbody>
    <#list users as user>
    <tr>
        <td><a href="/user-profile/${user.id}/1">${user.getUsername()}</a>

    </tr>
    </#list>
    </tbody>
</table>
</@c.page>