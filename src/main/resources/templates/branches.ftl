<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

    <#include "parts/branchEdit.ftl" />
    <#if !private??>
<h5>What would you like to write? Let's get it started in the best topic!</h5>
    </#if>
<table class="table table-striped mb-5 mt-3" style="width: 80em">
    <tbody style="color: grey">
    <#list branches as branch>

    <tr>
        <th scope="row">
            <a href="<#if branch.dialog>/privates/<#else>/branches/</#if>${branch.name}/1" style="color: darkslategrey">
            <#if branch.dialog> with ${branch.getName()?replace(user.getUsername(), "")?replace("-", "")}
            <#else>${branch.name}
            </#if>
            </a></th>
        <th scope="row">
            <small><i ><#if branch.description??>${branch.description}</#if></i></small>
        </th >
        <th scope="row">
            <small><small><i>topic messages: <#if branch.messages??>${branch.getMessages()?size}</#if></i></small></small>
        </th>
        <th scope="row">
            <#if isAdmin>
                <a class="btn btn-outline-secondary btn-sm" href="/branches/edit/${branch.id}">
                    Edit
                </a>
                <#if branch.id != 1>
                <a class="btn btn-outline-secondary btn-sm" href="/branch-delete=${branch.id}">
                    Delete
                </a>
                </#if>
            </#if>
        </th>
    </tr>
    </#list>
    </tbody>
</table>
</@c.page>