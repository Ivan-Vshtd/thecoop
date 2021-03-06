<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

    <#assign info = userChannel.info>
    <#include "parts/userInfo.ftl" />

<table class="table table-hover" style="max-width: 18.6rem;">
    <tbody>

     <#if !isCurrentUser>
         <#if isSubscriber>
    <tr>
        <td><a class="btn btn-outline-secondary" href="/user/unsubscribe/${userChannel.id}">UnSubscribe</a></td>
    </tr>
         <#else>
    <tr>
        <td><a class="btn btn-outline-secondary" href="/user/subscribe/${userChannel.id}">Subscribe</a></td>
    </tr>
         </#if>
    <tr>
        <td><a href="/privates/${name}-${userChannel.username}/1">Send private message</a></td>
    </tr>
     </#if>

    <tr>
        <td><a href="/user/subscriptions/${userChannel.id}/list">Subscriptions: ${subscriptionsCount}</a></td>
    </tr>
    <tr>
        <td><a href="/user/subscribers/${userChannel.id}/list">Subscribers: ${subscribersCount}</a></td>
    </tr>
    <tr>
    <#if isCurrentUser>
        <td><a href="/privates/${name}/branches">Open private dialogues</a>
        <#include "parts/messageEdit.ftl"/></td>
    </#if>
    </tr>
    <tr>
        <td>
            <div>
            <a data-toggle="collapse"  href="#collapseList" role="button" aria-expanded="false" aria-controls="collapseExample2">
                <#if !isCurrentUser>${userChannel.username}'s<#else>my</#if> messages <small><i class="fas fa-arrows-alt-v msg"></i></small>
            </a>
        </div>
        </td>
    </tr>
    </tbody>
</table>
<div class="collapse show" id="collapseList">
    <#assign path = '/user-messages/${userChannel.id}/'>
    <#include "parts/pagination.ftl" />
    <#include "parts/messagesList.ftl" />
    <#include "parts/pagination.ftl" />
</div>
</@c.page>