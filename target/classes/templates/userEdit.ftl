<#import "parts/common.ftl" as c>

<@c.page>
<h4>User editor</h4>

<form action="/user" method="post" enctype="multipart/form-data">

    <div class="card text-center bg-light mb-3" style="max-width: 18.6rem;">
        <div class="card-header">
            <div class="form-group mt-2">
                <input type="text" name="username" value="${user.username}"/>
            </div>
        </div>
        <div class="card-body">
            <img src="<#if user.avatarFilename??>/avatar/${user.avatarFilename}<#else>/static/images/logopng.png</#if>"
                 style="max-width: 260px; max-height: 260px;"/>
            <i><small>registerd: ${user.date?string('dd.MM.yyyy HH:mm:ss')}</small></i>
        </div>
        <div class="form-group col-md-12">
            <div class="custom-file">
                <input type="file" name="file" id="customFile"/>
                <label class="custom-file-label" for="customFile">Choose file</label>
            </div>
        </div>
    </div>
    <#list roles as role>
    <div>
        <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")} />${role}
        </label>
    </div>
    </#list>
    <input type="hidden" value="${user.id}" name="userId"/>
    <input type="hidden" value="${_csrf.token}" name="_csrf"/>
    <button type="submit" class="btn btn-outline-secondary">Save</button>
</form>
</@c.page>