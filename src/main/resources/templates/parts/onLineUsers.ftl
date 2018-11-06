<#if onLineUsers??>

    <div class="online" style="max-width: 18rem; margin-bottom: 20px">
        <small><i>
            <a href="/user/online" style="color: green">Online users:</a>
                <#list onLineUsers as user>
                    <a href="/user-profile/${user.id}/1" <#if user.admin>style="color: blueviolet"</#if>>${user.username}</a>,
                </#list>
        </i></small>
    </div>
</#if>