<#if userChannel??>
<#assign user = userChannel>
</#if>
<div class="card text-center bg-light mb-3" style="max-width: 18.6rem;">
    <div class="card-header">
        <div class="form-group">
        ${user.username}
        </div>
    </div>
    <div class="card-body">
        <img src="<#if user.avatarFilename??>/avatar/${user.avatarFilename}<#else>/static/images/logopng.png</#if>"
             style="max-width: 260px; max-height: 260px;"/>
        <#if info??>
        <#if info.birthday??>born: ${info.birthday?string('dd.MM.yy')}</#if>
    <#if info.location??><br/>from: ${info.location}</#if></#if>
        <br/>
        <i>
            <small>registered: ${user.date?string('dd.MM.yyyy HH:mm:ss')}</small>
        </i><br/>
        <i>
            <small>
                <#if !user.active>
                    <b style="color: red">
                        this user has been BANNED!
                    </b>
                <#elseIf user.online>
                    <i><a href="/user/online" style="color: green">online</a></i>
                <#elseIf user.lastVisit??>Last visit: ${user.lastVisit?string('dd.MM.yyyy HH:mm:ss')}
                <#else><i style="color: red">offline</i>
                </#if>
            </small>
        </i>
    </div>
    <#if !isCurrentUser??>
    <div class="form-group col-md-12">
        <div class="custom-file">
            <input type="file" name="file" id="customFile"/>
            <p class="text-left"><label class="custom-file-label" for="customFile">Choose your avatar</label></p>
        </div>
    </div>
    </#if>
</div>