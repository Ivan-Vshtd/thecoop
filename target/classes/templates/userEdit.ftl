<#import "parts/common.ftl" as c>

<@c.page>
<h4>User editor</h4>

<form action="/user" method="post" enctype="multipart/form-data">

 <#assign info = user.info>
    <#include "parts/userInfo.ftl" />
    <#list roles as role>
    <div>
        <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")} /> ${role}
        </label>
    </div>
    </#list>
    <hr>
    <div>
        <label><input type="checkbox" name="active" ${user.active?string("checked", "")} /> ACTIVE
        </label>
    </div>
    <input type="hidden" value="${user.id}" name="userId"/>
    <input type="hidden" value="${_csrf.token}" name="_csrf"/>
    <button type="submit" class="btn btn-outline-secondary">Save</button>
</form>
</@c.page>