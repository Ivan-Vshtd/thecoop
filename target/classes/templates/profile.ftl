<#import "parts/common.ftl" as c>


<@c.page>
<small style="color: grey"><i>Here you may change your info</i></small>

<form class="mt-3" method="post" enctype="multipart/form-data">  <#--https://getbootstrap.com-->
    <div class="card text-center bg-light mb-3" style="max-width: 18.6rem;">
        <div class="card-header">
            <div class="form-group">
                ${user.username}
            </div>
        </div>
        <div class="card-body">
            <img src="<#if user.avatarFilename??>/avatar/${user.avatarFilename}<#else>/static/images/logopng.png</#if>"
                 style="max-width: 260px; max-height: 260px;"/>
            <i>
                <small>registered: ${user.date?string('dd.MM.yyyy HH:mm:ss')}</small>
            </i><br/>
            <i>
                <small>
                <#if user.online>
                    <i style="color: green">online</i>
                <#elseIf user.lastVisit??>Last visit: ${user.lastVisit?string('dd.MM.yyyy HH:mm:ss')}
                    <#else><i style="color: red">offline</i>
                </#if></small>
            </i>
        </div>
        <div class="form-group col-md-12">
            <div class="custom-file">
                <input type="file" name="file" id="customFile"/>
                <p class="text-left"><label class="custom-file-label" for="customFile">Choose your avatar</label></p>
            </div>
        </div>
    </div>

    ${message?ifExists}

    <div class="form-group row">
        <label class="col-sm-1 col-form-label"> Password:</label>
        <div class="col-sm-2.5">
            <input type="password" name="password" class="form-control" placeholder="password"/>
        </div>
    </div>

    <div class="form-group row">
        <label class="col-sm-1 col-form-label"> Email:</label>
        <div class="col-sm-2.5">
            <input type="email" name="email" class="form-control" placeholder="email@mail.com" value="${email!''}"/>
        </div>
    </div>
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <div>
        <button class="btn btn-outline-secondary" type="submit">Save</button>
    </div>
</form>
</@c.page>
