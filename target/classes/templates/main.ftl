<#import "parts/common.ftl" as c>

<@c.page>
<div class="form-row">
    <div class="form-group col-md-6">
        <form method="get" action="/main" class="form-inline">
            <input type="text" name="filter" class="form-control mt-2" value="${filter?ifExists}"
                   placeholder="Search by tag"/>
            <button type="submit" class="btn btn-outline-secondary ml-2 mt-2">Search</button>
        </form>
    </div>
</div>

<h5>Last messages of the Coop:</h5>

    <#include "parts/messagesList.ftl" />

</@c.page>