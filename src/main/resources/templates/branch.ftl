<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
<h4>${branch.name}</h4>
<#if branch.description??>
<div style="color: grey"><small><i><#if branch.description??>${branch.description}</#if></i></small></div>
</#if>
    <#include "parts/messageEdit.ftl" />
    <#include "parts/messagesList.ftl" />
    <#assign path = '/branches/${branch.name}/'>
    <#include "parts/pagination.ftl" />

</@c.page>