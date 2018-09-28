<#if onLineUsers??>

    <div class="online" style="max-width: 18rem; margin-left: 60px; margin-top: 20px">
        <small><i>
            Online users:
                <#list onLineUsers as user>
                    <a href="/user-messages/${user.id}/1" <#if user.admin>style="color: blueviolet"</#if>>${user.username}</a>,
                </#list>
        </i></small>
    </div>
</#if>