<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <#if answerTo??>
    <div style="margin-bottom: 30px">Type the answer to:
      <footer class="blockquote-footer">
          <cite title="Source Title">${answerTo}</cite>
      </footer>
    </div>

    </#if>
    <#include "parts/messageEdit.ftl" />
</@c.page>