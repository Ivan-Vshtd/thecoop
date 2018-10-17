<#if !isCurrentUser!false || answerTo??>
<a data-toggle="collapse" href="#collapsePart" role="button" aria-expanded="false" aria-controls="collapseExample">
    <i class="far fa-comments add"></i>
</a>
</#if>
<div class="collapse <#if message?? || answerTo??>show</#if>" id="collapsePart">
    <div class="form-group mt-3 col-sm-5">
        <form method="post" enctype="multipart/form-data">
            <div class="form-group">
                <input type="text" class="form-control ${(textError??)?string('is-invalid', '')}"
                       value="<#if message??>${message.text}</#if>" name="text" placeholder="Enter your message"/>
                <#if textError??>
                    <div class="invalid-feedback">
                        ${textError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <input type="text" class="form-control"
                       value="<#if message??>${message.tag}</#if>" name="tag" placeholder="Tag"/>
                <#if tagError??>
                    <div class="invalid-feedback">
                        ${tagError}
                    </div>
                </#if>
            </div>
            <div class="form-group">
                <div class="custom-file">
                    <input type="file" name="file" id="customFile"/>
                    <label class="custom-file-label" for="customFile">Choose file</label>
                </div>
            </div>
             <#if message?? && !message.dialog>
                 <#if branches??>
            <div class="form-group">
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <label class="input-group-text" for="inputGroupSelect">Move to:</label>
                    </div>
                    <select class="custom-select" id="inputGroupSelect" name="branch">
                        <option value=${message.branch.id}>${message.branch.name} - current topic</option>
                        <#list branches as branch>
                        <option value=${branch.id}>${branch.name}</option>
                        </#list>
                    </select>
                </div>
                <#if branchError??>
                    <div class="invalid-feedback">
                        ${branchError}
                    </div>
                </#if>
            </div>
             </#if>
             </#if>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="hidden" name="id" value="<#if message??>${message.id}<#else>-1</#if>"/>
            <#if path??><input type="hidden" name="path" value="${path?string}"/></#if>
            <div class="form-group mb-5">
                <button type="submit" class="btn btn-outline-secondary"><#if message??>Update<#else>Save</#if></button>
            </div>
        </form>
    </div>
</div>
