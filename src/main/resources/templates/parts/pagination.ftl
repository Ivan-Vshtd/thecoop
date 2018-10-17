<#assign page = nnls(current!1)/>
<#assign sum = nnls(total!1)/>
<#if (sum > 1) >
<nav aria-label="Page navigation">
    <ul class="pagination pagination-sm justify-content-center">


        <#if (page > 4)> <li class="page-item">
            <a class="page-link" href="${path}${1}">first</a>
        </li></#if>

    <#if page != 1> <li class="page-item">
<a class="page-link" href="${path}${page - 1}">
    <span aria-hidden="true">&laquo;</span>
    <span class="sr-only">Previous</span></a>
        </li></#if>

        <#list 3..1 as index>
            <#if ((page - index) > 0)>
                <li class="page-item">
                    <a class="page-link" href="${path}${page - index}">${page - index}</a>
                </li>
            </#if>
        </#list>

        <li class="page-item">
            <a class="page-link"
               href="${path}${page}" style="color: black">${page}</a>
        </li>

<#list 1..3 as index>
    <#if ((page + index) <= sum)>
    <li class="page-item">
        <a class="page-link" href="${path}${page + index}">${page + index}</a>
    </li>
    </#if>
</#list>

<#if (page < sum)>
        <li class="page-item">
            <a class="page-link" href="${path}${page + 1}">
                <span aria-hidden="true">&raquo;</span>
                <span class="sr-only">Next</span></a>
        </li>
</#if>
        <#if (page <= (sum - 4))> <li class="page-item">
            <a class="page-link" href="${path}${sum}">last</a>
        </li></#if>

    </ul>
</nav>
</#if>

<#function nnls x>
    <#if x??><#return x><#else><#return 1></#if>
</#function>