<div class="form-group mt-3">
    <form method="post">
        <div class="form-group col-md-3">
            <#if isAdmin>
            <input type="text" class="form-control ${(branchError??)?string('is-invalid', '')}"
                   value="<#if branch??>${branch.name}</#if>" name="name" placeholder="Enter topic name"/>
                <#if branchError??>
                    <div class="invalid-feedback">
                        ${branchError}
                    </div>
                </#if>
                <div class="form-group mt-2">
                    <input type="text" class="form-control ${(descriptionError??)?string('is-invalid', '')}"
                           value="<#if branch??>${branch.description}</#if>" name="description" placeholder="Description"/>
                <#if descriptionError??>
                    <div class="invalid-feedback">
                        ${descriptionError}
                    </div>
                </#if>
                </div>
            <div class="form-group mt-2">
                <button type="submit" class="btn btn-outline-secondary"><#if branch??>Update topic
                <#else>Add new topic</#if></button>
            </div>
             </#if>
        </div>

        <input type="hidden" name="_csrf" value="${_csrf.token}"/>
        <input type="hidden" name="id" value="<#if branch??>${branch.id}<#else>-1</#if>"/>
    </form>
</div>