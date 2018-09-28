<#include "security.ftl">
<#import "login.ftl" as l>
<div class="navBar">
<nav class="navbar navbar-expand-lg navbar-light" style="background-color: #FFCC99;">
    <a class="navbar-brand" href="/">The Coop</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
            <li class="nav-item">
                <a class="nav-link" href="/">Home</a>
            </li>
            <#if user??>
            <li class="nav-item">
                <a class="nav-link" href="/user">Users</a>
            </li>
                <li class="nav-item">
                    <a class="nav-link" href="/main">Activity</a>
                </li>
            <li class="nav-item">
                <a class="nav-link" href="/branches">Topics</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/user-messages/${currentUserId}/1">My messages</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="/user/profile/${currentUserId}">Profile</a>
            </li>
            </#if>
        </ul>

        <div class="navbar-text mr-3"><#if user??>${name}<#else>Unsigned user</#if></div>
        <@l.logout />
    </div>
</nav>
</div>