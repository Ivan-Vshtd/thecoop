<#import "parts/common.ftl" as c>
<@c.page>
<small style="color: grey"><i>Here you may change your info</i></small>

<form class="mt-3" method="post" enctype="multipart/form-data">  <#--https://getbootstrap.com-->

    <#assign user = user>
    <#include "parts/userInfo.ftl" />
    ${message?ifExists}

    <div style="width: 63%">
    <div class="form-group row">
        <label class="col-sm-1 col-form-label"> Password:</label>
        <div class="col-sm-2.5">
            <input type="password" name="password" class="form-control" placeholder="password"/>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-1 col-form-label"> Email:</label>
        <div class="col-sm-2.5">
            <input type="email" name="email" class="form-control" placeholder="email@mail.com" value="${email!''}"/>
        </div>
    </div>
    <div class="form-group row">
        <label class="col-sm-1 col-form-label"> Location:</label>
        <div class="col-sm-2.5">
            <input type="text" name="location" class="form-control" placeholder="where are you from?" value="<#if info??>${info.location!''}</#if>"/>
        </div>
    </div>

    <div class="form-group row">
        <label class="col-sm-1 col-form-label"> Birthday:</label>
        <div class="input-group mb-3 col-sm-2">
            <div class="input-group-prepend">
                <label class="input-group-text" for="inputGroupSelect">Year</label>
            </div>
            <select class="custom-select" id="inputGroupSelect" name="year">
                <option value=${info.birthday?date?string.yyyy}>${info.birthday?date?string.yyyy}</option>
                        <#list 1971..2018 as year>
                            <option value=${year}>${year?string}</option>
                        </#list>
            </select>
        </div>
        <div class="input-group mb-3 col-sm-2">
            <div class="input-group-prepend">
                <label class="input-group-text" for="inputGroupSelect">Month</label>
            </div>
            <select class="custom-select" id="inputGroupSelect" name="month">
                <option value=${info.birthday?date?string["MM"]}>${info.birthday?date?string["MM"]}</option>
                        <#list 1..12 as month>
                            <option value=${month}>${month?string}</option>
                        </#list>
            </select>
        </div>
        <div class="input-group mb-3 col-sm-2">
            <div class="input-group-prepend">
                <label class="input-group-text" for="inputGroupSelect">Day</label>
            </div>
            <select class="custom-select" id="inputGroupSelect" name="day">
                <option value=${info.birthday?date?string["dd"]}>${info.birthday?date?string["dd"]}</option>
                        <#list 1..31 as day>
                            <option value=${day}>${day?string}</option>
                        </#list>
            </select>
        </div>
    </div>
    <input type="hidden" name="_csrf" value="${_csrf.token}"/>
    <div>
        <button class="btn btn-outline-secondary" type="submit">Save</button>
    </div>
</form>
</@c.page>
