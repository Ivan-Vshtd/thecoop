<#import "parts/common.ftl" as c>
<@c.page>
<style>
    body {
        height: 100%;
        background: url("/static/images/logopng.png") repeat fixed;
        background-position-x: right;
    }
</style>

<div align="center" style="margin-top: -10px">
<h2>Welcome!!!</h2>
<h4>This is a simple forum for messaging</h4>
<#if user??>
<div></div>
<#else>
<div>Please Sign In</div>
</#if>
</div>
</@c.page>