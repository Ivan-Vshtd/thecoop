<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
<div class="card text-center" style="width: 100px; height: 120px;">
    <div class="class-text">
        <img class="card-img-top mt-2"
             src="<#if userChannel.avatarFilename??>/avatar/${userChannel.avatarFilename}<#else>/static/images/logopng.png</#if>"
             style="width: 80px; height: 80px center" alt="avatar">
    </div>
    <i style="line-height: 12px">
        <small><small>
                <#if userChannel.online>
                    <i style="color: green">online</i>
                <#else>Last visit:<br/> ${userChannel.lastVisit?string('dd.MM.yyyy HH:mm:ss')}
                </#if>
        </small></small>
    </i>
</div>
<#if !isCurrentUser>${userChannel.username}'s<#else>my</#if> messages

<div class="container my-2" style="padding-left: 1px">
    <p><a href="/user/subscriptions/${userChannel.id}/list">Subscriptions: ${subscriptionsCount}</a></p>
    <p><a href="/user/subscribers/${userChannel.id}/list">Subscribers: ${subscribersCount}</a></p>
</div>
    <#if !isCurrentUser>
    <p><a href="/privates/${name}-${userChannel.username}/1">Send private message</a></p>
        <#if isSubscriber>
<a class="btn btn-outline-secondary" href="/user/unsubscribe/${userChannel.id}">UnSubscribe</a>
        <#else>
<a class="btn btn-outline-secondary" href="/user/subscribe/${userChannel.id}">Subscribe</a>
        </#if>
    </#if>
    <#if isCurrentUser>
    <p><a href="/privates/${userChannel.id}/branches">Open private dialogues</a></p>
        <#include "parts/messageEdit.ftl" />
    </#if>
    <#include "parts/messagesList.ftl" />
    <#assign path = '/user-messages/${userChannel.id}/'>
    <#include "parts/pagination.ftl" />
</@c.page>