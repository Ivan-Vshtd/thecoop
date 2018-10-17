<#macro login path isRegisterForm>
<form action="${path}" method="post" xmlns="http://www.w3.org/1999/html">  <#--https://getbootstrap.com-->
    <div class="form-group row">                                           <#--https://getbootstrap.com/docs/4.1/components/forms/-->
        <label class="col-sm-1 col-form-label"> User Name:</label>
        <div class="col-sm-1.5">
            <input type="text" name="username" value="<#if user??>${user.username}</#if>"
                   class="form-control ${(usernameError??)?string('is-invalid', '')}" placeholder="username"/>
            <#if usernameError??>
                <div class="invalid-feedback">
                    ${usernameError}
                </div>
            </#if>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-1 col-form-label"> Password:</label>
        <div class="col-sm-1.5">
            <input type="password" name="password"
                   class="form-control ${(passwordError??)?string('is-invalid', '')}"
                   placeholder="password"/>
             <#if passwordError??>
                <div class="invalid-feedback">
                    ${passwordError}
                </div>
             </#if>
        </div>
    </div>
    <#if isRegisterForm>
    <div class="form-group row">
        <label class="col-sm-1 col-form-label"> Confirm Password:</label>
        <div class="col-sm-1.5">
            <input type="password" name="password2"
                   class="form-control ${(password2Error??)?string('is-invalid', '')}" placeholder="confirm password"/>
             <#if password2Error??>
                <div class="invalid-feedback">
                    ${password2Error}
                </div>
             </#if>
        </div>
    </div>
        <div class="form-group row">
            <label class="col-sm-1 col-form-label"> Email:</label>
            <div class="col-sm-1.5">
                <input type="email" name="email" value="<#if user??>${user.email}</#if>"
                       class="form-control ${(emailError??)?string('is-invalid', '')}" placeholder="email@mail.com"/>
             <#if emailError??>
                <div class="invalid-feedback">
                    ${emailError}
                </div>
             </#if>
            </div>
        </div>
        <div class="form-group row ml-1">
            <div class="g-recaptcha" data-sitekey="6Ldfc2gUAAAAAN8OG20tGE3APiWxnSaFzhs349yA"></div>
            <#if captchaError??>
                <div class="alert alert-danger" role="alert">
                    ${captchaError}
                </div>
            </#if>
        </div>
    </#if>
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <div>
        <button class="btn btn-outline-secondary mt-2" type="submit"><#if isRegisterForm>Add<#else>Sign in</#if></button>
    </div>
    <#if !isRegisterForm><a href="/registration">Registration</a><#else><a href="/login">Sign in
    </#if>

</form>
</#macro>

<#macro logout>
 <form action="/logout" method="post">
     <input type="hidden" name="_csrf" value="${_csrf.token}"/>
     <button class="btn btn-secondary" type="submit"><#if user??>Sign out<#else>Sign in</#if></button>
 </form>
</#macro>